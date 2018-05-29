package me.game.poker.utils;

import me.game.poker.entity.Poker;

import java.util.*;

public class PokerUtils {
    /**
     * 牌大小
     */
    private static final List<Integer> pokerSortValues = Arrays.asList(
            12, 12, 12, 12,//A
            13, 13, 13, 13,//2
            1, 1, 1, 1,//3
            2, 2, 2, 2,//4
            3, 3, 3, 3,//5
            4, 4, 4, 4,//6
            5, 5, 5, 5,//7
            6, 6, 6, 6,//8
            7, 7, 7, 7,//9
            8, 8, 8, 8,//10
            9, 9, 9, 9,//J
            10, 10, 10, 10,//Q
            11, 11, 11, 11,//K
            14, 15//King
    );

    private static final List<String> pokerNames = Arrays.asList(
             "黑桃A","红桃A","梅花A", "方块A", //A
                 "黑桃2","红桃2","梅花2", "方块2", //2
                 "黑桃3","红桃3","梅花3", "方块3", //3
                 "黑桃4","红桃4","梅花4", "方块4", //4
                 "黑桃5","红桃5","梅花5", "方块5", //5
                 "黑桃6","红桃6","梅花6", "方块6", //6
                 "黑桃7","红桃7","梅花7", "方块7", //7
                 "黑桃8","红桃8","梅花8", "方块8", //8
                 "黑桃9","红桃9","梅花9", "方块9", //9
                 "黑桃10","红桃10", "梅花10", "方块10", //10
                 "黑桃J","红桃J", "梅花J","方块J", //J
                 "黑桃Q","红桃Q", "梅花Q","方块Q", //Q
                 "黑桃K","红桃K", "梅花K","方块K", //K
                "小王", "大王");//King

    /**
     * A的大小值
     */
    public static final Integer AValue = 12;
    public static final Integer BigKingValue = 15;
    public static final Integer SmallKingValue = 14;


    /**
     * 牌面的id
     */
    private static final List<Integer> pokerIds = Arrays.asList(
            1, 2, 3, 4,//A
            5, 6, 7, 8,//2
            9, 10, 11, 12,//3
            13, 14, 15, 16,//4
            17, 18, 19, 20,//5
            21, 22, 23, 24,//6
            25, 26, 27, 28,//7
            29, 30, 31, 32,//8
            33, 34, 35, 36,//9
            37, 38, 39, 40,//10
            41, 42, 43, 44,//J
            45, 46, 47, 48,//Q
            49, 50, 51, 52,//K
            53, 54);//King


    /**
     * 随机生成一副牌
     */
    public static List<Poker> getRandomPokers(){
        List<Poker> pokers = new ArrayList<>();
        int length = pokerIds.size();
        List<Integer> activityIdList = new ArrayList<>(pokerIds);
        List<Integer> idList = new ArrayList<>();

        for(int i = 0 ; i < pokerIds.size() ; i ++){
            int index = (int)Math.floor(Math.random() * length);
            int pokerIndex = activityIdList.get(index);
            idList.add(pokerIndex);
            List<Integer> list = new ArrayList<>();

            for (int j = 0 ; j < activityIdList.size() ; j ++){
                if(j != index){
                    list.add(activityIdList.get(j));
                }
            }
            activityIdList = new ArrayList<>(list);
            length --;
        }
        for(Integer index : idList){
            pokers.add(new Poker(
                    pokerIds.get(index -1),
                    pokerNames.get(index -1),
                    pokerSortValues.get(index -1)
                    ));
        }
        return pokers;
    }

    /**
     * 获取分配好的一副扑克牌
     * @return
     */
    public static Map<Integer,List<Poker>> getSplitPokers(){
        List<Poker> list = getRandomPokers();
        Map<Integer,List<Poker>> map = new HashMap<>();
        map.put(0,new ArrayList<>(list.subList(0,17)));
        map.put(1,new ArrayList<>(list.subList(17,34)));
        map.put(2,new ArrayList<>(list.subList(34,51)));
        map.put(3,new ArrayList<>(list.subList(51,54)));
        return map;
    }

}
