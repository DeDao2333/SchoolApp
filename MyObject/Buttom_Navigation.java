package com.example.java.api25.MyObject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.java.api25.MyObject.FragmentContent.PersonFragment;
import com.example.java.api25.MyObject.FragmentContent.RecycleFragment;
import com.example.java.api25.R;

import java.util.ArrayList;
import java.util.List;

public class Buttom_Navigation extends AppCompatActivity {

    private List<OrderCodeObject> mObjects = new ArrayList<>();
    private FragmentManager frag;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    SwitchToHome();
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(Buttom_Navigation.this, "hello2", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_notifications:
                    SwitchToPerson();
                    return true;
                case R.id.navigation_caa:
                    Toast.makeText(Buttom_Navigation.this, "My text", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool);
        setSupportActionBar(toolbar);
        
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        initList();
        RecycleFragment ReFragment = new RecycleFragment(this, mObjects);
        frag = getSupportFragmentManager();
        FragmentTransaction tran = frag.beginTransaction();
        tran.add(R.id.recycle_content, ReFragment);
        tran.commit();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "click menu", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void initList() {
        for(int i=0;i<10;i++) {
            OrderCodeObject oco = new OrderCodeObject(R.mipmap.ic_launcher_round,
                    "女神异闻录", "这是一款每个人都不应该错过的大作！");
            mObjects.add(oco);
        }
    }

    public void SwitchToPerson() {
        PersonFragment personFragment = new PersonFragment();
        FragmentTransaction tran = frag.beginTransaction();
        tran.replace(R.id.recycle_content, personFragment);
        tran.commit();
    }

    public void SwitchToHome() {
        RecycleFragment fragment = new RecycleFragment(this, mObjects);
        FragmentTransaction tran = frag.beginTransaction();
        tran.replace(R.id.recycle_content, fragment);
        tran.commit();
    }
}
