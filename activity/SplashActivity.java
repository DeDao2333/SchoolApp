package com.example.java.algorithm.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.BaseActivity;
import com.example.java.algorithm.javabean._User;
import com.example.java.algorithm.model.UserModel;

/**
 * 启动界面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class,null,true);
                }else{
                    Toast.makeText(getApplicationContext(), ""+user.getNickname(), Toast.LENGTH_SHORT).show();
                    startActivity(MainAC.class,null,true);
                }
            }
        },1000);

    }
}
