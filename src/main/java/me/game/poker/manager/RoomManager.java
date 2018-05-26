package me.game.poker.manager;

import me.game.poker.entity.Room;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangkaile on 2018/4/6.
 */
public class RoomManager {
    /**
     * 存储所有的房间列表
     */
    public static Map<String,Room> roomMap = new ConcurrentHashMap<>();

    /**
     * 存储所有的用户ID和房间号对应关系
     */
    public static Map<String,Long> userRoomMap = new ConcurrentHashMap<>();
    public static Map<String,Session>  userSessionMap = new ConcurrentHashMap<>();

    /**
     * 可加入的房间
     */
    public static Room room = new Room();

    /**
     * 玩家状态：等待中
     */
    public static int Player_State_Waitting = 0;

    /**
     * 玩家状态：准备完毕
     */
    public static int Player_state_Reday = 1;

    /**
     * 玩家状态：托管
     */
    public static int Player_State_Deposit = 2;


    /**
     * 房间状态：可加入
     */
    public static int Room_State_Available = 0;

    /**
     * 房间状态：准备中
     */
    public static int Room_State_Reday = 1;

    /**
     * 房间状态：游戏中
     */
    public static int Room_State_InTheGame = 2;

    /**
     * Client请求进入房间
     */
    public static final int Request_IntoRoom = 1001;
    /**
     * Client准备就绪
     */
    public static final int Request_BeReady = 1002;
    /**
     * Client叫/抢地主
     */
    public static final int Request_CallTheLandlord = 1003;
    /**
     * Client出牌
     */
    public static final int Request_Discard = 1004;

    /**
     * Server通知客户端连接建立成功
     */
    public static final int Response_SocketCreateOK = 2000;

    /**
     * Server返回房间和房间里的其他玩家信息
     */
    public static final int Response_RoomInfo = 2001;
    /**
     * Server通知客户端准备
     */
    public static final int Response_Reday = 2002;
    /**
     * Server发牌
     */
    public static final int Response_DealPoker = 2003;
    /**
     * Server通知玩家叫/抢地主
     */
    public static final int Response_ToCallTheLandlord = 2005;
    /**
     * Server通知玩家地主归属和底牌
     */
    public static final int Response_LandlordAndLastCard = 2006;
    /**
     * Server通知玩家出牌
     */
    public static final int Response_Discard = 2007;


}
