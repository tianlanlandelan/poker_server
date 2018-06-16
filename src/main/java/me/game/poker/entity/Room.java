package me.game.poker.entity;

import me.game.poker.manager.RoomManager;

import java.util.*;

/**
 * Created by yangkaile on 2018/4/6.
 * 游戏房间实体类
 */
public class Room {
    /**
     * 房间Id
     */
    private String id = UUID.randomUUID().toString();
    /**
     * 房间号 6位数字组成，可以通过输入房间号进入房间
     */
    private Integer num;

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
     * 当前出的牌 记录当前房间出的牌
     */
    private List<Integer> activityPoker;

    /**
     * 当前出的牌的人的座位号 记录当前房间出的牌所属的座位号
     */
    private Integer activityPlayerSeat;

    /**
     * 底牌
     */
    private List<Integer> publicPoker;

    /**
     * 地主座位号
     */
    private Integer landlordSeat;

    /**
     * 当前出牌人 记录本轮该谁出牌了
     */
    private Integer activitySeat;

    public Room() {
    }

    public int getSeat(){
        if(players.size() >= RoomManager.Room_MaxPlayer){
            return -1;
        }else{
            return players.size() + 1;
        }
    }

    /**
     * 获取下一个出牌人座位号
     * @return
     */
    public Integer getNextActivityPlayerSeat(){
        if(players == null || players.size() != 3){
            return null;
        }else if(activitySeat == null){
            return landlordSeat;
        }else{
            return activitySeat % 3 + 1;
        }
    }
    public Player getPlayerById(String playerId){
        for(Player player : players){
            if(player.getId().equals(playerId))
                return  player;
        }
        return null;
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

    public List<Integer> getActivityPoker() {
        return activityPoker;
    }

    public void setActivityPoker(List<Integer> activityPoker) {
        this.activityPoker = activityPoker;
    }

    public List<Integer> getPublicPoker() {
        return publicPoker;
    }

    public void setPublicPoker(List<Integer> publicPoker) {
        this.publicPoker = publicPoker;
    }

    public Integer getActivityPlayerSeat() {
        return activityPlayerSeat;
    }

    public void setActivityPlayerSeat(Integer activityPlayerSeat) {
        this.activityPlayerSeat = activityPlayerSeat;
    }

    public Integer getLandlordSeat() {
        return landlordSeat;
    }

    public void setLandlordSeat(Integer landlordSeat) {
        this.landlordSeat = landlordSeat;
    }

    public Integer getActivitySeat() {
        return activitySeat;
    }

    public void setActivitySeat(Integer activitySeat) {
        this.activitySeat = activitySeat;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", num=" + num +
                ", players=" + players +
                ", type=" + type +
                ", createTime=" + createTime +
                ", state=" + state +
                ", activityPoker=" + activityPoker +
                ", activityPlayerSeat=" + activityPlayerSeat +
                ", publicPoker=" + publicPoker +
                ", landlordSeat=" + landlordSeat +
                ", activitySeat=" + activitySeat +
                '}';
    }
}
