package com.example.java.algorithm.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.BaseActivity;
import com.example.java.algorithm.fragment.MainPostSubFm;
import com.example.java.algorithm.fragment.ShPostFragment;
import com.example.java.algorithm.javabean.shPost;
import com.example.java.algorithm.model.PostModel;
import com.example.java.algorithm.model.i.QueryPostListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * author: DeDao233.
 * time: 2018/4/18.
 */
public class UserLikePostAc extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener, TabLayout.OnTabSelectedListener {

    public View mView;
    @BindView(R.id.image_mainpost_search)
    ImageView mImageView;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.fab_mainpost)
    FloatingActionButton mFabMainpost;

    private String[] titles = {"主帖", "二手帖"};
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentManager mFragmentManager;

    private MainPostSubFm mMainPostSubFm;
    private ShPostFragment mShPostFragment;

    //initialize adapter
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_post);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mFabMainpost.setVisibility(View.INVISIBLE);
        mPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager,
                                         @Nullable PagerAdapter oldAdapter,
                                         @Nullable PagerAdapter newAdapter) {
            }
        });

        mFragmentManager = getSupportFragmentManager();
        adapter = new MyAdapter(mFragmentManager);
        mPager.setAdapter(adapter);
        mTablayout.addOnTabSelectedListener(this);
        mTablayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));

        PostModel.getInstance().initDataQuery(null,
                new FindListener<shPost>() {
                    @Override
                    public void done(List<shPost> list, BmobException e) {
                        Toast.makeText(UserLikePostAc.this, "1. list size:"+list.size(), Toast.LENGTH_SHORT).show();
                        Log.d("wer", "relation");
                        mShPostFragment.setAdapter(list);
                    }
                }, new QueryPostListener() {
                    @Override
                    public void addCondition(BmobQuery query) {
                        query.addWhereRelatedTo("likes_sh", new BmobPointer(BmobUser.getCurrentUser()));
                    }
                });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int n = tab.getPosition();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRefresh() {

    }

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub

        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub

            return titles[position];
        }

        @Override
        public Fragment getItem(final int arg0) {
            // TODO Auto-generated method stub
            if (arg0 == 0) {
                Log.d("wer", "in like AC mainpost");
                mMainPostSubFm = new MainPostSubFm(UserLikePostAc.this);
                mMainPostSubFm.setDownLis(new QueryPostListener() {
                    @Override
                    public void addCondition(BmobQuery query) {
                        query.addWhereRelatedTo("likes_main", new BmobPointer(BmobUser.getCurrentUser()));
                        Log.d("wer", "1");
                    }
                });
                mMainPostSubFm.setInitLis(new QueryPostListener() {
                    @Override
                    public void addCondition(BmobQuery query) {
                        query.addWhereRelatedTo("likes_main", new BmobPointer(BmobUser.getCurrentUser()));
                        Log.d("wer", "2");
                    }
                });
                mMainPostSubFm.setUpLis(new QueryPostListener() {
                    @Override
                    public void addCondition(BmobQuery query) {
                        query.addWhereRelatedTo("likes_main", new BmobPointer(BmobUser.getCurrentUser()));
                        Log.d("wer", "3");
                    }
                });
                mFragments.add(mMainPostSubFm);
                return mMainPostSubFm;
            } else {
                Log.d("wer", "in like Ac shpost");
                mShPostFragment = new ShPostFragment(UserLikePostAc.this);
                mShPostFragment.setDownLis(new QueryPostListener() {
                    @Override
                    public void addCondition(BmobQuery query) {
                        query.addWhereRelatedTo("likes_sh", new BmobPointer(BmobUser.getCurrentUser()));
                        Log.d("wer", "mshFragment down: addtion");
                    }
                });
                mShPostFragment.setInitLis(new QueryPostListener() {
                    @Override
                    public void addCondition(BmobQuery query) {
                        query.addWhereRelatedTo("likes_sh", new BmobPointer(BmobUser.getCurrentUser()));
                        Log.d("wer", "mshFragment init: addtion");
                    }
                });
                mShPostFragment.setUpLis(new QueryPostListener() {
                    @Override
                    public void addCondition(BmobQuery query) {
                        query.addWhereRelatedTo("likes_sh", new BmobPointer(BmobUser.getCurrentUser()));
                        Log.d("wer", "mshFragment up: addtion");
                    }
                });
                mFragments.add(mShPostFragment);
                return mShPostFragment;
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return titles.length;
        }
    }
}

