package com.example.java.algorithm.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.ParentWithNaviActivity;
import com.example.java.algorithm.javabean._User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePwAc extends ParentWithNaviActivity {

    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_password_again)
    EditText mEtPasswordAgain;
    @BindView(R.id.btn_change)
    Button mBtnChange;

    _User mUser;

    @Override
    protected String title() {
        return "更改密码";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        ButterKnife.bind(this);
        initNaviView();
        mUser = BmobUser.getCurrentUser(_User.class);
    }

    private void changePw(String password, String pwdagain, final LogInListener listener) {
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(1000, "请填写密码"));
            return;
        }
        if (TextUtils.isEmpty(pwdagain)) {
            listener.done(null, new BmobException(1000, "请填写确认密码"));
            return;
        }
        if (!password.equals(pwdagain)) {
            listener.done(null, new BmobException(1000, "两次输入的密码不一致，请重新输入"));
            return;
        }
        mUser.setPassword(password);
        mUser.update(mUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ChangePwAc.this, "error:" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_change)
    public void onViewClicked() {
        changePw(mEtPassword.getText().toString(),
                mEtPasswordAgain.getText().toString(),
                new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "更改密码成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "更改失败...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
