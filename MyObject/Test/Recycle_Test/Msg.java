package com.example.java.api25.Test.Recycle_Test;

/**
 * Created by 59575 on 2017/4/13.
 */

public class Msg {
    public static int LEFT_MSG=0;
    public static int RIGHT_MSG = 1;

    private String content;
    private int direction;

    public Msg(String content, int direction) {
        this.content=content;
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public String getContent() {
        return content;
    }

}
