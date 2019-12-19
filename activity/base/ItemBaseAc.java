package com.example.java.algorithm.activity.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.FindListener;

/**
 * author: DeDao233.
 * time: 2018/5/8.
 */
public abstract class ItemBaseAc<T_adapter extends RecyclerView.Adapter,
        T extends BmobObject> extends BaseActivity {

    public T_adapter mAdapter;
    public List<T> mList = new ArrayList<>();
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public Context mContext;

    //about bmob listeners' setting
    public T theFirst;
    public T theLast;
    public FindListener<T> mListener_up,mListener_down,mListener_init;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NineGridView.setImageLoader(new GlideImageLoader());

        setAdapter();
    }

    public abstract void setAdapter();

    public abstract void setListener();

    public void initView(){
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downRefresh();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (mAdapter.getItemCount() > 3) {
                        upRefresh();
                    }
                }
            }
        });
    }

    public void upRefresh() {
        upRefreshQuery(theLast,mListener_up);
    }

    public void downRefresh() {
        downRefreshQuery(theFirst, mListener_down);
    }

    public void initData() {
        initDataQuery(mListener_init);
    }

    public abstract void initDataQuery(FindListener findListener);

    public abstract void upRefreshQuery(T last, FindListener findListener);

    public abstract void downRefreshQuery(T first, FindListener findListener);

    public class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }



}
