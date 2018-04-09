package me.game.poker.entity;

/**
 * 牌型对象类
 * 该类定义一个牌型对象，对象属性包含牌型、牌型大小排序值
 */
public class PokerType {

    /**
     * 牌型
     */
    private String type;
    /**
     * 牌型大小排序值
     */
    private short sort;
    /**
     * type 牌型
     * sort 牌型大小排序值
     */

    public PokerType (String type, short sort) {
        this.type = type;
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public int getSort() {
        return sort;
    }


}
