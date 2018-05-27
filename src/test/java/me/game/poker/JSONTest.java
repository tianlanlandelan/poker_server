package me.game.poker;

import com.google.gson.Gson;
import me.game.poker.socket.SocketResult;

/**
 * Created by yangkaile on 2018/4/6.
 */
public class JSONTest {
    public static void main(String[] arges){
        // 简单的json测试字符串

        Gson gson = new Gson();
        SocketResult result = new SocketResult();
        result.setCode(10000);
        result.setData("123456");

        System.out.println(gson.toJson(result));
    }
}
