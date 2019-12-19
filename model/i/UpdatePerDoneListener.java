package com.example.java.algorithm.model.i;

import com.example.java.algorithm.javabean._User;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

/**
 * author: DeDao233.
 * time: 2018/6/1.
 */
public abstract class UpdatePerDoneListener extends BmobListener1<_User> {
    public abstract void done(_User s, BmobException e);

    @Override
    protected void postDone(_User user, BmobException e) {

    }
}
