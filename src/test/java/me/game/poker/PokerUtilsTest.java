package me.game.poker;

import me.game.poker.entity.Poker;
import me.game.poker.utils.PokerUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by yangkaile on 2018/5/27.
 */
public class PokerUtilsTest {
    public static void main(String[] arges){
        Map<Integer,List<Poker>> map= PokerUtils.getSplitPokers();
        System.out.println(map.get(3));
    }
}
