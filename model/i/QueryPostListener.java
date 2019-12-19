package com.example.java.algorithm.model.i;

import cn.bmob.v3.BmobQuery;

/**
 * author: DeDao233.
 * time: 2018/5/24.
 */
public abstract class QueryPostListener {
    public abstract void addCondition(BmobQuery query);
}
