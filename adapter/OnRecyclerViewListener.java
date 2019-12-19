package com.example.java.algorithm.adapter;

/**
 * author: DeDao233.
 * time: 2018/5/12.
 */
public interface OnRecyclerViewListener {
    public void onItemClick(int adapterPosition);

    public boolean onItemLongClick(int adapterPosition);
}
