package com.example.java.algorithm.model;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.example.java.algorithm.javabean._User;
import com.example.java.algorithm.model.i.QueryPostListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * author: DeDao233.
 * time: 2018/5/24.
 */
public class PostModel extends BaseModel {

    private static PostModel instance = new PostModel();

    public static PostModel getInstance() {
        return instance;
    }

    private PostModel() {

    }

    /**
     * 上拉刷新
     * @param last  当页最后一个
     * @param mSwRefreshMainPost    刷新View
     * @param findListener  查询结果回调
     * @param queryPostListener 查询条件附加
     * @param <T>
     */
    public <T extends BmobObject> void upRefreshQuery(T last, SwipeRefreshLayout mSwRefreshMainPost,
                               FindListener<T> findListener,
                               QueryPostListener queryPostListener){
        if (last != null) {
            mSwRefreshMainPost.setRefreshing(true);
            BmobQuery<T> query = new BmobQuery<>();
            String start = last.getCreatedAt();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (queryPostListener != null) {
                queryPostListener.addCondition(query);
            }
            query.addWhereLessThan("createdAt", new BmobDate(date));
            query.order("-createdAt");
            query.include("author");
            query.setLimit(3);
            query.findObjects(findListener);
        } else {
            Toast.makeText(getContext(), "没有内容", Toast.LENGTH_SHORT).show();
            mSwRefreshMainPost.setRefreshing(false);
        }
    }

    /**
     * 下拉刷新
     * @param first 当前页第一个
     * @param mSwRefreshMainPost    刷新View
     * @param findListener  结果回调
     * @param queryPostListener 查询条件附加
     * @param <T>
     */
    public <T extends BmobObject> void downRefreshQuery(T first,SwipeRefreshLayout mSwRefreshMainPost,
                                 FindListener<T> findListener,
                                 QueryPostListener queryPostListener){
        BmobQuery<T> query = new BmobQuery<>();
        //查询时间大于当前页面第一个文章的创建时间的
        if (first != null) {
            mSwRefreshMainPost.setRefreshing(true);
            String start = first.getCreatedAt();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            query.addWhereGreaterThan("createdAt", new BmobDate(date));
            if (queryPostListener != null) {
                queryPostListener.addCondition(query);
            }
            query.order("createdAt");
            //内部查询
            query.include("author");
            query.findObjects(findListener);
        } else {
            Toast.makeText(getContext(), "没有内容", Toast.LENGTH_SHORT).show();
            mSwRefreshMainPost.setRefreshing(false);
        }
    }


    /**
     * 初始化
     * @param mSwRefreshMainPost 刷新View
     * @param findListener  查询结果回调
     * @param queryPostListener 查询条件附加
     * @param <T>
     */
    public <T extends BmobObject> void initDataQuery(SwipeRefreshLayout mSwRefreshMainPost,
                              FindListener<T> findListener,
                              QueryPostListener queryPostListener) {
        if (mSwRefreshMainPost != null) {
            mSwRefreshMainPost.setRefreshing(true);
        }
        BmobQuery<T> query = new BmobQuery<>();
        if (queryPostListener != null) {
            queryPostListener.addCondition(query);
        }
        query.setLimit(5);
        query.order("-createdAt");
        query.include("author");
        query.findObjects(findListener);
    }

    public <T extends BmobObject>void relationUserQuery(FindListener<T> findListener,
                                                        String likeKey) {
        // search the all of posts which the user likes
        // so we should seek in post table
        BmobQuery<T> query = new BmobQuery<>();
        _User user = BmobUser.getCurrentUser(_User.class);
        // likes is the Token in user table, containing all of the post liked
        query.addWhereRelatedTo(likeKey, new BmobPointer(user));
        query.findObjects(findListener);
    }

    public <T extends BmobObject> void commonQuery(FindListener<T> findListener,
                                                   QueryPostListener queryPostListener) {
        BmobQuery<T> query = new BmobQuery<>();
        if (queryPostListener != null) {
            queryPostListener.addCondition(query);
        }
        query.findObjects(findListener);
    }
}
