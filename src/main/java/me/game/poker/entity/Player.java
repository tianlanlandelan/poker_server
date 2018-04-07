package me.game.poker.entity;

/**
 * Created by yangkaile on 2018/4/6.
 * 玩家实体类
 */
public class Player {
    private Long id;
    private String name;
    private int seat;

    public Player(String name) {
        this.name = name;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
