package me.game.poker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangkaile on 2018/4/6.
 */
public class SocketResult {
    private int code;
    private Object data ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
