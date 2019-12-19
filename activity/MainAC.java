package com.example.java.algorithm.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.BaseActivity;
import com.example.java.algorithm.event.RefreshEvent;
import com.example.java.algorithm.fragment.CommunityFragment;
import com.example.java.algorithm.fragment.ConversationFragment;
import com.example.java.algorithm.fragment.MainPostFragment;
import com.example.java.algorithm.fragment.OnFragmentInteractionListener;
import com.example.java.algorithm.fragment.SetFragment;
import com.example.java.algorithm.javabean._User;
import com.example.java.algorithm.util.IMMLeaks;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * 主界面
 */
public class MainAC extends BaseActivity implements
        OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ConversationFragment mConversationFragment;
    private SetFragment mSetFragment;
    private MainPostFragment mMainPostFragment;
    private CommunityFragment mCommunityFragment;
    private Fragment[] fragments;
    private int index=0;
    private int currentTabIndex=0;

    //initialize views
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ac);
        Log.d("wer", "main oncreate");
        initView();

        final _User user = BmobUser.getCurrentUser(_User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        toast(e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    toast(status.getMsg());
                    Logger.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());
    }

    public void initView() {

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        initFragments();

    }

    private void initFragments() {
        mConversationFragment = new ConversationFragment();
        mSetFragment = new SetFragment();
        mMainPostFragment = new MainPostFragment();
        mCommunityFragment = new CommunityFragment();
        fragments = new Fragment[]{mMainPostFragment,mConversationFragment,
                mCommunityFragment,mSetFragment};
        Log.d("wer", "add");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_mainac, mMainPostFragment)
                .add(R.id.fragment_container_mainac, mConversationFragment)
                .add(R.id.fragment_container_mainac, mCommunityFragment)
                .add(R.id.fragment_container_mainac, mSetFragment)
                .hide(mSetFragment)
                .hide(mConversationFragment)
                .hide(mCommunityFragment)
                .show(mMainPostFragment)
                .commit();
    }

    public static void ToMainAc(Activity activity) {
        Intent intent = new Intent(activity, MainAC.class);
        activity.startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //fragment中点击事件
    @Override
    public void onItemClick(int itemID) {
        switch (itemID) {
            case R.id.team:
                startActivity(TeamActivity.class,null,false);
                break;
            case R.id.shand:
                startActivity(ShPostActivity.class,null,false);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                index=0;
                onFragmentIndex(index);
                return true;
            case R.id.navigation_message:
                index=1;
                onFragmentIndex(index);
                return true;
            case R.id.navigation_community:
                index=2;
                onFragmentIndex(index);
                return true;
            case R.id.navigation_user:
                index=3;
                onFragmentIndex(index);
                return true;
        }
        return false;
    }

    private void onFragmentIndex(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        currentTabIndex = index;
    }

}
