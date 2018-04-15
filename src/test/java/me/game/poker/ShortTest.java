package me.game.poker;

public class ShortTest {

    public static void testType(short foo, short bar) {
        int result = foo * bar;

        System.out.println( ((Object)result).getClass().getName() );
    }

    public static void main(String[] args) {
        short f1 = 3;
        short f2 = 2;
        testType(f1, f2);
    }
}
