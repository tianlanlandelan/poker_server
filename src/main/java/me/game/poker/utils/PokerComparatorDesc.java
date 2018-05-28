package me.game.poker.utils;

import me.game.poker.entity.Poker;

import java.util.Comparator;

/**
 * Created by yangkaile on 2018/5/28.
 */
public class PokerComparatorDesc implements Comparator<Poker> {

    public int compare(Poker p1,Poker p2){
        if(p1.getSort() > p2.getSort())
            return -1;
        else if(p1.getSort() < p2.getSort())
            return 1;
        else
            return 0;
    }
}
