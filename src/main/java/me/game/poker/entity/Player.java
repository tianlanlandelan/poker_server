package me.game.poker.entity;

import me.game.poker.manager.RoomManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangkaile on 2018/4/6.
 * 玩家实体类
 */
public class Player {
    private String id;
    private String name;

    /**
     * 玩家状态 等待中/已准备/游戏中
     */
    private Integer state;
    private String roomId;
    private int seat;


    private List<Integer> pokerIds = new ArrayList<>();


    public Player(String id ,String name) {
        this.id = id;
        this.name = name;
        this.state = RoomManager.Player_State_Waitting;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Integer> getPokerIds() {
        return pokerIds;
    }

    public void setPokerIds(List<Integer> pokerIds) {
        this.pokerIds = pokerIds;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", roomId='" + roomId + '\'' +
                ", seat=" + seat +
                ", pokerIds=" + pokerIds +
                '}';
    }
}
