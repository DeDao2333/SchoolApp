package com.example.java.algorithm.adapter.holder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.UserInfoActivity;
import com.example.java.algorithm.adapter.BaseViewHolder;
import com.example.java.algorithm.adapter.OnRecyclerViewListener;
import com.example.java.algorithm.javabean._User;
import com.example.java.algorithm.tool.ImageLoaderFactory;

import butterknife.BindView;

public class SearchUserHolder extends BaseViewHolder {

  @BindView(R.id.avatar)
  public ImageView avatar;
  @BindView(R.id.name)
  public TextView name;
  @BindView(R.id.btn_add)
  public Button btn_add;

  public SearchUserHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_search_user,onRecyclerViewListener);
  }

  @Override
  public void bindData(Object o) {
    final _User user =(_User)o;
    ImageLoaderFactory.getLoader().loadAvator(avatar,user.getAvatar(), R.mipmap.head);
    name.setText(user.getUsername());
    btn_add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {//查看个人详情
          Bundle bundle = new Bundle();
          bundle.putSerializable("u", user);
          startActivity(UserInfoActivity.class,bundle);

        }
    });
  }
}