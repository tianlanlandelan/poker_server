package me.game.poker;

import me.game.poker.entity.Poker;
import me.game.poker.entity.PokerType;
import me.game.poker.utils.PokerTypeUtils;
import me.game.poker.utils.PokerUtils;
import me.game.poker.utils.PokerComparatorDesc;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void testGetPokerType(){
        List<Poker> list = PokerUtils.getRandomPokers();
        List<Poker> subList;
        int time = 0;
        while (true){
            subList = new ArrayList<>();
            for(int i = 0 ; i < 6 ; i ++){
                int index = (int)Math.floor(Math.random() * 54);
                subList.add(list.get(index));
            }
            subList.sort(new PokerComparatorDesc());
            PokerType type = PokerTypeUtils.getType(subList);
            if(type != null) {
                System.out.println(subList + "---" + type);
                time ++;
            }
            if(time >= 30) break;
        }
    }

}
