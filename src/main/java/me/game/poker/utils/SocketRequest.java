package me.game.poker.utils;

import java.util.Map;

/**
 * Created by yangkaile on 2018/5/26.
 */
public class SocketRequest {
    private int code;
    private Map<String,Object> data ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SocketRequest{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
