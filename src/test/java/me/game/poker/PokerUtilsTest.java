package me.game.poker;

import me.game.poker.entity.Poker;
import me.game.poker.utils.PokerUtils;
import me.game.poker.utils.PokerComparatorDesc;
import org.junit.Test;

import java.util.List;

/**
 * Created by yangkaile on 2018/5/27.
 */
public class PokerUtilsTest {
    public static void main(String[] arges){
//        Map<Integer,List<Poker>> map= PokerUtils.getSplitPokers();
//        System.out.println(map.get(3));
    }
    @Test
    public void testSort(){
        List<Poker> list= PokerUtils.getRandomPokers();
        System.out.println(list);
        System.out.println("===================");
        System.out.println("===================");
        list.sort(new PokerComparatorDesc());
        System.out.println(list);
    }
}
