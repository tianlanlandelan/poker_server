package me.game.poker.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yangkaile on 2018/4/6.
 * 游戏房间实体类
 */
public class Room {
    /**
     * 房间号
     */
    private Long id = System.currentTimeMillis();
    private final int MAX_PLAYER = 3;
    /**
     * 房间内的所有玩家
     */
    private List<Player> players = Collections.synchronizedList(new ArrayList());

    public Long getId() {
        return id;
    }

    public Room() {
    }



    /**
     * 添加一位玩家并返回玩家的座位号
     * @param player
     * @return
     */
    public int setPlayer(Player player){
        if(players.size() >= MAX_PLAYER){
            return -1;
        }else{
            players.add(player);
            return players.size();
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
