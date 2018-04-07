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

    public static int Request_IntoRoom = 1001;
    public static int Request_BeReady = 1002;
    public static int Request_CallTheLandlord = 1003;
    public static int Request_Discard = 1004;

    public static int Response_RoomInfo = 2001;
    public static int Response_Reday = 2002;
    public static int Response_DealPoker = 2003;
    public static int Response_ToCallTheLandlord = 2005;
    public static int Response_LandlordAndLastCard = 2006;
    public static int Response_ToDiscard = 2007;


}
