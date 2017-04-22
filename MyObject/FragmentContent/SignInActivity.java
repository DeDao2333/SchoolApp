package com.example.java.api25.MyObject.FragmentContent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java.api25.MyObject.Buttom_Navigation;
import com.example.java.api25.R;

public class SignInActivity extends AppCompatActivity {

    private TextView mUserName,mPassWord;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mPassWord = (TextView) findViewById(R.id.text_passWord);
        mSwitch = (Switch) findViewById(R.id.switch1);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mPassWord.getText().toString().equals("123546")) {
                    startActivity(new Intent(SignInActivity.this, Buttom_Navigation.class));
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, ""
                            + mPassWord.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
