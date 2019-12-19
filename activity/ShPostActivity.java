package com.example.java.algorithm.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.BaseActivity;
import com.example.java.algorithm.fragment.ShPostFragment;

public class ShPostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sh_post);

        initView();

    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShPostEditAc.class,null,false);
            }
        });

        ShPostFragment fragment = new ShPostFragment(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_shpost_container, fragment)
                .commit();
    }

}
