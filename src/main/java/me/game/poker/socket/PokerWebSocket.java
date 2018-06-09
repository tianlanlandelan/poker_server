package me.game.poker.socket;

import com.google.gson.Gson;
import me.game.poker.entity.Player;
import me.game.poker.entity.Poker;
import me.game.poker.entity.Room;
import me.game.poker.manager.RoomManager;
import me.game.poker.utils.PokerCompareUtils;
import me.game.poker.utils.PokerTypeUtils;
import me.game.poker.utils.PokerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * 使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，但在springboot中连容器都是spring管理的。
 * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
 */
@ServerEndpoint("/pokerWebSocket/{userName}")
@Component
public class PokerWebSocket {
    /**
     * 存活的session集合（使用线程安全的map保存）
     */
    private static Map<String, Session> livingSessions = new ConcurrentHashMap<>();
    final Logger logger = LoggerFactory.getLogger(PokerWebSocket.class);
    private String response(Integer code,Object data){
        Gson gson = new Gson();
        SocketResponse result = new SocketResponse(code,data);
        return gson.toJson(result);
    }
    /**
     * 建立连接的回调方法
     * @param session 与客户端的WebSocket连接会话
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userName") String userName) {
        logger.info(userName+  " 连接成功");
        livingSessions.put(session.getId(), session);

        String userId = UUID.randomUUID().toString();
        RoomManager.userSessionMap.put(userId,session);

        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        //进入房间成功，向客户端返回当前房间信息
        sendMessage(session,RoomManager.Response_InitUserOK,map);
    }

    /**
     * 收到客户端消息的回调方法
     * @param message 客户端传过来的消息
     * @param session 对应的session
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userName") String userName) {
        Gson gson = new Gson();
        SocketRequest request = gson.fromJson(message,SocketRequest.class);
        logger.info(userName + "request:" + request);
        switch (request.getCode()){
            case RoomManager.Request_IntoRoom:{
                joinTheRoom(session,request.getData());
            } break;
            /*
             *准备完毕
             */
            case RoomManager.Request_BeReady: {
                ready(session,request.getData());
            } break;
            /*
             *叫地主
             */
            case RoomManager.Request_CallTheLandlord:{

            } break;
            /*
             *出牌
             */
            case RoomManager.Request_Discard:{
                disCard(session,request.getData());
            } break;

            default:break;
        }
    }
    /**
     *
     * 发生错误的回调方法
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }

    /**
     *  关闭连接的回调方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userName") String userName) {
        logger.error(userName + "：连接断开");
        livingSessions.remove(session.getId());
    }
    /**
     * 单独发送消息
     * @param userId
     * @param code
     * @param data
     */
    public void sendMessage(String userId, int code ,Object data) {
        Session session = RoomManager.userSessionMap.get(userId);
        sendMessage(session,code,data);
    }

    /**
     *
     * @param session
     * @param code
     * @param data
     */
    public void sendMessage(Session session, int code ,Object data) {
        try {
            logger.info("response:" + response(code,data));
            session.getBasicRemote().sendText(response(code,data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(Session session,String message){
        try {
            logger.info("response:" + message);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendError(Session session , int errorCode , String message){
        try {
            SocketResponseError responseError = new SocketResponseError(errorCode,message);
            logger.info("response:" + response(RoomManager.Response_ERROR,responseError));
            session.getBasicRemote().sendText(response(RoomManager.Response_ERROR,responseError));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发消息
     * @param message
     */
    public void sendMessageToAll(String message) {
        livingSessions.forEach((sessionId, session) -> {
            sendMessage(session, message);
        });
    }

    /**
     * 将用户加入房间
     * 1.解析出用户Id和用户名
     * 2.找出空闲房间，将用户加入房间
     * 3.
     * @param session   用户session
     * @param userData  用户数据
     */
    public void joinTheRoom(Session session ,Map<String,Object> userData){
        try {
            String userId = userData.get("userId").toString();
            String userName = userData.get("userName").toString();

            Player player = new Player(userId,userName);
            synchronized(RoomManager.room){
                if(RoomManager.room == null) {
                    RoomManager.room = new Room();
                }
                Room room = RoomManager.room;
                player.setRoomId(room.getId());
                int seat = room.getSeat();
                player.setSeat(seat);
                /*
                    抢到位置，将玩家加入房间
                */
                if(seat > 0){
                    room.getPlayers().add(player);
                    Map<String,Object> map = new HashMap<>();
                    map.put("roomId",room.getId());
                    map.put("seat",seat);
                    map.put("players",room.getPlayers());
                    //进入房间成功，向客户端返回当前房间信息
                    sendMessage(session,RoomManager.Response_RoomInfo,map);
                }
                /*
                    房间内还有其他玩家，通知其他玩家有用户加入房间
                 */
                if(seat > 1){
                    //取出房间内其他玩家的Session
                    for(Player otherPlayer : room.getPlayers()){
                        if(!otherPlayer.getId().equals(userId)){
                            Map<String,Object> map = new HashMap<>();
                            map.put("player",player);
                            //通知其他玩家：该房间内有新玩家加入
                            sendMessage(otherPlayer.getId(),RoomManager.Response_NewPlayerJoin,map);
                        }
                    }
                }

                /*
                    如果当前房间已满，将当前房间状态设置为准备中，并通知房间内所有玩家准备
                */
                if(seat == 3){
                    room.setState(RoomManager.Room_State_Reday);
                    for(Player everyOne : room.getPlayers()){
                        Map<String,Object> map = new HashMap<>();
                        map.put("roomId",room.getId());
                        //通知房间内所有玩家准备
                        sendMessage(everyOne.getId(),RoomManager.Response_Reday,map);
                    }
                    RoomManager.roomMap.put(room.getId(),room);
                    RoomManager.room = new Room();
                }
            }//synchronized
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备
     * 1.将用户状态设置为准备中
     * 2.如果房间内所有玩家都准备了，将房间状态设置为开始游戏，并开始发牌
     * @param session
     * @param userData
     */
    public void ready(Session session ,Map<String,Object> userData){
        try {
            String userId = userData.get("userId").toString();
            String roomId = userData.get("roomId").toString();

            Room room = RoomManager.roomMap.get(roomId);
            if(room == null){
                //TODO 报错
                return ;
            }
            int count = 0;
            for(Player player : room.getPlayers()){
                if(player.getId().equals(userId)){
                    player.setState(RoomManager.Player_state_Reday);
                }
                if(player.getState() == RoomManager.Player_state_Reday){
                    count ++;
                }
            }
            /*
                如果所有玩家准备完毕，房间状态设置为游戏中，
                并开始发牌
             */
            if(count == RoomManager.Room_MaxPlayer){
                room.setState(RoomManager.Room_State_InTheGame);
                List<Player> playerList = room.getPlayers();
                //获取分好的牌
                Map<Integer,List<Integer>> pokers = PokerUtils.getSplitPokerIds();
                /*
                    给每个玩家发牌
                 */
                for(int index = 0 ; index < playerList.size() ; index ++){
                    List<Integer> pokerIdList = pokers.get(index);
                    List<Poker> pokerList = PokerUtils.parsePokers(pokerIdList);

                    //保存玩家的牌
                    Player player = playerList.get(index);
                    player.setPokerIds(pokerIdList);

                    Map<String,Object> map = new HashMap<>();
                    map.put("pokers",pokerList);
                    sendMessage(player.getId(),RoomManager.Response_DealPoker,map);
                }
                //TODO 叫/抢地主的逻辑待完善，暂时使用系统直接指定地主的方式实现 发完牌5秒钟后随机生成一个地主，给房间内所有玩家发送地主的座位号和底牌
                Thread.sleep(5 * 1000);
                int landlordSeat = (int)Math.floor(Math.random() * 3) + 1;
                //保存房间里地主的座位号
                room.setLandlordSeat(landlordSeat);
                room.setActivitySeat(landlordSeat);

                //获取底牌
                List<Integer> publicPokerIds = pokers.get(3);
                List<Poker> publicPokers = PokerUtils.parsePokers(publicPokerIds);

                for(Player player : playerList){
                    //保存地主的牌
                    if(player.getSeat() == landlordSeat){
                        player.getPokerIds().addAll(publicPokerIds);
                        logger.info("===========地主的牌数：" + player.getPokerIds().size());
                    }
                    //将地主座位号和底牌通知到所有玩家
                    Map<String,Object> map = new HashMap<>();
                    map.put("landlordSeat",landlordSeat);
                    map.put("publicPokers",publicPokers);
                    sendMessage(player.getId(),RoomManager.Response_LandlordAndLastCard,map);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 玩家出牌
     * @param session
     * @param userData
     */
    public void disCard(Session session ,Map<String,Object> userData){
        try {
            String userId = userData.get("userId").toString();
            String roomId = userData.get("roomId").toString();
            Object poker = userData.get("pokers");
            Room room = RoomManager.roomMap.get(roomId);
            Player player = room.getPlayerById(userId);

            if(room == null){
                sendError(session,1,"room == null");
                return;
            }
            if(player == null){
                sendError(session,1,"player == null");
                return;
            }
            //下一个出牌人不是自己
            if(player.getSeat() != room.getActivitySeat()){
                sendError(session,1,"下一个出牌人不是自己");
                return;
            }

            /*
            用户不出牌
             */
            if(poker == null){
                if(room.getActivityPlayerSeat() == player.getSeat()){
                    sendError(session,1,"必须出牌");
                    return;
                }
                room.setActivitySeat(room.getNextActivityPlayerSeat());
                for(Player p : room.getPlayers()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("seat",player.getSeat());
                    map.put("nextSeat",room.getActivitySeat());
                    sendMessage(p.getId(),RoomManager.Response_Discard,map);
                }
                return;
            }
            /*
            用户出牌
             */
            String[] pokers = userData.get("pokers").toString().split(",");
            List<Integer> pokerIdList = new ArrayList<>();
            for(String str : pokers){
                if(str != null && str.trim() != ""){
                    pokerIdList.add(Integer.parseInt(str));
                }
            }
            //出的牌不是自己的
            if(!player.getPokerIds().containsAll(pokerIdList)){
                sendError(session,1,"出的牌不是自己的");
                return;
            }
            //牌型不合法
            if(PokerTypeUtils.getType(pokerIdList) == null){
                sendError(session,1,"牌型不合法");
                return;
            }
            //房间内出的有牌,且不是自己出的，且选择的牌没有已出的牌大
            if(room.getActivityPoker() != null && room.getActivityPoker().size() > 0
                    && room.getActivityPlayerSeat() != player.getSeat()
                    && !PokerCompareUtils.comparePukers(pokerIdList,room.getActivityPoker()))
            {
                sendError(session,1,"选择的牌没有已出的牌大");
                return;
            }else{
                /*
                从玩家的牌中移除已经出的牌
                刷新当前出的牌和出牌人座位号
                */
                player.getPokerIds().removeAll(pokerIdList);

                /*
                将出牌人和出的牌通知到所有玩家
                 */
                room.setActivityPoker(pokerIdList);
                room.setActivityPlayerSeat(player.getSeat());
                room.setActivitySeat(room.getNextActivityPlayerSeat());
                for(Player p : room.getPlayers()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("seat",player.getSeat());
                    map.put("pokers",PokerUtils.parsePokers(pokerIdList));
                    map.put("nextSeat",room.getActivitySeat());
                    sendMessage(p.getId(),RoomManager.Response_Discard,map);
                }
                /*
                玩家牌出完，通知所有人游戏结束
                 */
                if(player.getPokerIds() == null || player.getPokerIds().size() == 0) {
                    String victory;
                    if(player.getSeat() == room.getLandlordSeat()){
                        victory = RoomManager.Victory_Landlord;
                    }else{
                        victory = RoomManager.Victory_Farmer;
                    }
                    for(Player p : room.getPlayers()){
                        Map<String,Object> map = new HashMap<>();
                        map.put("victory", victory);
                        sendMessage(p.getId(),RoomManager.Response_GameOver,map);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
