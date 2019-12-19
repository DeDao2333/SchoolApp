package com.example.java.algorithm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.ParentWithNaviActivity;
import com.example.java.algorithm.javabean._User;
import com.example.java.algorithm.model.UserModel;
import com.example.java.algorithm.tool.ImageLoaderFactory;
import com.example.java.algorithm.tool.Tool;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserInfoChangeAc extends ParentWithNaviActivity implements View.OnClickListener{

    _User user;
    @BindView(R.id.iv_avator)
    ImageView mIvAvator;
    @BindView(R.id.layout_head)
    RelativeLayout mLayoutHead;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.layout_nickname)
    RelativeLayout mLayoutNickname;
    @BindView(R.id.tv_reset_password)
    TextView mTvResetPassword;
    @BindView(R.id.layout_reset_password)
    RelativeLayout mLayoutResetPassword;
    @BindView(R.id.layout_all)
    LinearLayout mLayoutAll;
    @BindView(R.id.tv_sno)
    TextView mTvSno;
    @BindView(R.id.layout_sno)
    RelativeLayout mLayoutSno;
    private List<LocalMedia> selectList;

    @Override
    protected String title() {
        return "修改信息";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_change);
        ButterKnife.bind(this);
        //导航栏
        initNaviView();
        //用户
        user = UserModel.getInstance().getCurrentUser();
        //加载头像
        ImageLoaderFactory.getLoader().loadAvator(mIvAvator, user.getAvatar(), R.mipmap.head);
        //显示学号
        mTvSno.setText(user.getSno());
        Log.d("wer", "学号： " + user.getSno());
        //显示昵称
        mTvNickname.setText(user.getNickname());

        mLayoutHead.setOnClickListener(this);
        mLayoutSno.setOnClickListener(this);
        mLayoutNickname.setOnClickListener(this);
        mLayoutResetPassword.setOnClickListener(this);
    }

    //出现更新数据的对话框
    private void changeInfo(String info,
                            View view,
                            DialogInterface.OnClickListener clickListener) {
//        EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle(info)
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(view)
                .setPositiveButton("确定", clickListener)
                .setNegativeButton("取消",null).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_head:
                // 进入相册 以下是例子：用不到的api可以不写
                Tool.selectImage(this);
                break;
            case R.id.layout_sno:
                final EditText et = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入新的学号：")
                        .setIcon(android.R.mipmap.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String sno = et.getText().toString();
                                if (TextUtils.isEmpty(sno)) {
                                    return;
                                }
                                user.setSno(sno);
                                user.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                            mTvSno.setText(sno);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "error:"+e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消",null).show();
                break;
            case R.id.layout_reset_password:
                Bundle bundle = new Bundle();
                bundle.putSerializable("u",UserModel.getInstance().getCurrentUser());
                startActivity(ChangePwAc.class, bundle);
                break;
            case R.id.layout_nickname:
                final EditText et1 = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入新的昵称：")
                        .setIcon(android.R.mipmap.sym_def_app_icon)
                        .setView(et1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String nickname = et1.getText().toString();
                                if (TextUtils.isEmpty(nickname)) {
                                    return;
                                }
                                user.setNickname(nickname);
                                user.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                            mTvNickname.setText(nickname);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "error:"+e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消",null).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);

                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    if (selectList.size() != 0) {
                        //加载头像
                        ImageLoaderFactory.getLoader().loadAvator(mIvAvator,
                                selectList.get(0).getCompressPath(), R.mipmap.head);
                        //上传头像
                        final BmobFile file = new BmobFile(new File(selectList.get(0).getCompressPath()));
                        file.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(UserInfoChangeAc.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                                    //设置头像
                                    user.setAvatar(file.getFileUrl());
                                    user.update(user.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(UserInfoChangeAc.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "失败："+e, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(UserInfoChangeAc.this, "失败..."+e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    break;
            }
        }
    }
}
