package me.game.poker.entity;

import me.game.poker.manager.RoomManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by yangkaile on 2018/4/6.
 * 游戏房间实体类
 */
public class Room {
    /**
     * 房间号
     */
    private String id = UUID.randomUUID().toString();

    /**
     * 房间内的所有玩家
     */
    private List<Player> players = new ArrayList();

    /**
     * 房间类型
     */
    private Integer type = 0;

    /**
     * 房间创建时间
     */
    private Long createTime = System.currentTimeMillis();

    /**
     * 房间状态 0:可加入 1:准备中 2:游戏中
     */
    private Integer state = RoomManager.Room_State_Available;

    /**
     * 当前出的牌
     */
    private Poker[] activityPoker;

    /**
     * 底牌
     */
    private Poker[] publicPoker;



    /**
     * 当前出牌人
     */
    private Integer activityPlayerSeat;


    public Room() {
    }

    public int getSeat(){
        if(players.size() >= RoomManager.Room_MaxPlayer){
            return -1;
        }else{
            return players.size() + 1;
        }
    }

    public Integer getNextActivityPlayerSeat(){
        if(players == null || players.size() != 3){
            return null;
        }else{
            return activityPlayerSeat % 3 + 1;
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Poker[] getActivityPoker() {
        return activityPoker;
    }

    public void setActivityPoker(Poker[] activityPoker) {
        this.activityPoker = activityPoker;
    }

    public Poker[] getPublicPoker() {
        return publicPoker;
    }

    public void setPublicPoker(Poker[] publicPoker) {
        this.publicPoker = publicPoker;
    }

    public Integer getActivityPlayerSeat() {
        return activityPlayerSeat;
    }

    public void setActivityPlayerSeat(Integer activityPlayerSeat) {
        this.activityPlayerSeat = activityPlayerSeat;
    }
}
