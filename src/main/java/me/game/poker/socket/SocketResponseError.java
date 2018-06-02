package me.game.poker.socket;

/**
 * Created by yangkaile on 2018/4/6.
 */
public class SocketResponseError {
    private int errorCode;
    private String data ;

    public SocketResponseError(int errorCode, String data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SocketResponseError{" +
                "errorCode=" + errorCode +
                ", data='" + data + '\'' +
                '}';
    }
}
