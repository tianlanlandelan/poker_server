package me.game.poker.entity;

/**
 * Created by yangkaile on 2018/4/6.
 * 扑克牌实体类
 */
public class Poker {

    /**
     * 牌ID
     */
    private int id;

    /**
     * 牌面
     */
    private String name;

    /**
     * 牌大小排序值 0-14  数值越小，表示牌越大
     */
    private int sort;

    public Poker (int id, String name, int sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
    }

    public Poker (int id, int sort) {
        this.id = id;
        this.sort = sort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Poker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
