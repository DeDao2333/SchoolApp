package com.example.java.algorithm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.java.algorithm.adapter.holder.SearchUserHolder;
import com.example.java.algorithm.javabean._User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :smile
 * @project:SearchUserAdapter
 * @date :2016-01-22-14:18
 */
public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<_User> users = new ArrayList<>();

    public SearchUserAdapter() {
    }

    public void setDatas(List<_User> list) {
        users.clear();
        if (null != list) {
            users.addAll(list);
        }
    }

    /**获取用户
     * @param position
     * @return
     */
    public _User getItem(int position){
        return users.get(position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchUserHolder(parent.getContext(), parent, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(users.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
