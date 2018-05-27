package me.game.poker.socket;

import com.google.gson.Gson;
import me.game.poker.entity.Player;
import me.game.poker.entity.Poker;
import me.game.poker.entity.Room;
import me.game.poker.manager.RoomManager;
import me.game.poker.utils.PokerUtils;
import me.game.poker.utils.SocketRequest;
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

    private String response(Integer code,Object data){
        Gson gson = new Gson();
        SocketResult result = new SocketResult();
        result.setCode(code);
        result.setData(data);
        return gson.toJson(result);
    }
    /**
     * 建立连接的回调方法
     * @param session 与客户端的WebSocket连接会话
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userName") String userName) {
        System.out.println(userName+  " 连接成功");
        livingSessions.put(session.getId(), session);

        String userId = UUID.randomUUID().toString();
        RoomManager.userSessionMap.put(userId,session);

        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        //进入房间成功，向客户端返回当前房间信息
        sendMessage(session,response(RoomManager.Response_InitUserOK,map));
    }

    /**
     * 收到客户端消息的回调方法
     * @param message 客户端传过来的消息
     * @param session 对应的session
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userName") String userName) {
        System.out.println(userName + " : " + message);
        Gson gson = new Gson();
        SocketRequest request = gson.fromJson(message,SocketRequest.class);
        System.out.println("request:" + request);
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
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     *  关闭连接的回调方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userName") String userName) {
        livingSessions.remove(session.getId());
    }
    /**
     * 单独发送消息
     * @param session
     * @param message
     */
    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
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
                    sendMessage(session,response(RoomManager.Response_RoomInfo,map));
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
                            sendMessage(RoomManager.userSessionMap.get(otherPlayer.getId()),response(RoomManager.Response_NewPlayerJoin,map));
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
                        sendMessage(RoomManager.userSessionMap.get(everyOne.getId()),response(RoomManager.Response_Reday,map));
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
                如果所有玩家准备完毕，开始发牌
             */
            if(count == RoomManager.Room_MaxPlayer){
                room.setState(RoomManager.Room_State_InTheGame);
                List<Player> playerList = room.getPlayers();
                //分好的牌
                Map<Integer,List<Poker>> pokers = PokerUtils.getSplitPokers();
                /*
                    给每个玩家发牌
                 */
                for(int index = 0 ; index < playerList.size() ; index ++){
                    Map<String,Object> map = new HashMap<>();
                    map.put("pokers",pokers.get(index));
                    map.put("publicPokers",pokers.get(playerList.size()));
                    sendMessage(RoomManager.userSessionMap.get(playerList.get(index).getId()),response(RoomManager.Response_DealPoker,map));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
