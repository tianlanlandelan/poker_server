package me.game.poker.entity;

/**
 * Created by yangkaile on 2018/4/6.
 * 扑克牌实体类
 */
public class Poker {

    /**
     * 牌面ID
     */
    private short id;

    /**
     * 牌大小字符表示
     */
    private String orderString;

    /**
     * 牌大小排序值 0-14  数值越小，表示牌越大
     */
    private short sort;

    public Poker (short id, String orderString, short sort) {
        this.id = id;
        this.orderString = orderString;
        this.sort = sort;
    }
}
