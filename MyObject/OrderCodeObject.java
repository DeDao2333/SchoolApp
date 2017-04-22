package com.example.java.api25.MyObject;

/**
 * Created by 59575 on 2017/4/20.
 */

public class OrderCodeObject {
    private int ResId;
    private String name;
    private String content;

    public OrderCodeObject(int ResId, String name, String content) {
        this.content=content;
        this.ResId = ResId;
        this.name = name;
    }

    public int getResId() {
        return ResId;
    }

    public void setResId(int resId) {
        ResId = resId;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
