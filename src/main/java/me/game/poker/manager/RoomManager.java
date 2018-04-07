package me.game.poker.manager;

import me.game.poker.entity.Room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangkaile on 2018/4/6.
 */
public class RoomManager {
    public static Map<Long,Room> usedRoom = new ConcurrentHashMap<>();
    public static Room room = null;

    /**
     * Client请求进入房间
     */
    public static int Request_IntoRoom = 1001;
    /**
     * Client准备就绪
     */
    public static int Request_BeReady = 1002;
    /**
     * Client叫/抢地主
     */
    public static int Request_CallTheLandlord = 1003;
    /**
     * Client出牌
     */
    public static int Request_Discard = 1004;

    /**
     * Server返回房间和房间里的其他玩家信息
     */
    public static int Response_RoomInfo = 2001;
    /**
     * Server通知客户端准备
     */
    public static int Response_Reday = 2002;
    /**
     * Server发牌
     */
    public static int Response_DealPoker = 2003;
    /**
     * Server通知玩家叫/抢地主
     */
    public static int Response_ToCallTheLandlord = 2005;
    /**
     * Server通知玩家地主归属和底牌
     */
    public static int Response_LandlordAndLastCard = 2006;
    /**
     * Server通知玩家出牌
     */
    public static int Response_Discard = 2007;


}
