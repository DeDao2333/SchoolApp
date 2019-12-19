package com.example.java.algorithm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.ParentWithNaviActivity;
import com.example.java.algorithm.fragment.MainPostSubFm;
import com.example.java.algorithm.model.i.QueryPostListener;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;

public class SearchAc extends ParentWithNaviActivity {

    @BindView(R.id.et_find_content)
    EditText mEtFindContent;
    @BindView(R.id.btn_search_content)
    Button mBtnSearchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initNaviView();
        initView();
    }

    @Override
    protected void initView() {

        mBtnSearchContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragment();
            }
        });
    }

    private void initFragment() {
        MainPostSubFm fragment = new MainPostSubFm(this);
        fragment.setDownLis(new QueryPostListener() {
            @Override
            public void addCondition(BmobQuery query) {
                query.addWhereContains("content",mEtFindContent.getText().toString());
            }
        });
        fragment.setInitLis(new QueryPostListener() {
            @Override
            public void addCondition(BmobQuery query) {
                query.addWhereContains("content",mEtFindContent.getText().toString());
            }
        });
        fragment.setUpLis(new QueryPostListener() {
            @Override
            public void addCondition(BmobQuery query) {
                query.addWhereContains("content",mEtFindContent.getText().toString());
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_search_mainpost, fragment)
                .commit();
    }

    @Override
    protected String title() {
        return "搜索文章";
    }

}
