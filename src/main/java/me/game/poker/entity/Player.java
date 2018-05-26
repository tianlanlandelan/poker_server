package me.game.poker.entity;

import me.game.poker.manager.RoomManager;

/**
 * Created by yangkaile on 2018/4/6.
 * 玩家实体类
 */
public class Player {
    private String id;
    private String name;
    private Integer state;
    private String roomId;
    private int seat;

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
}
