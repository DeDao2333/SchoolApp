package com.example.java.algorithm.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java.algorithm.R;
import com.example.java.algorithm.activity.base.ParentWithNaviActivity;
import com.example.java.algorithm.javabean._User;
import com.example.java.algorithm.javabean.user_type;
import com.example.java.algorithm.model.PostModel;
import com.example.java.algorithm.model.UpdatePerListener;
import com.example.java.algorithm.model.UserModel;
import com.example.java.algorithm.model.i.QueryPostListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class DevelopeAc extends ParentWithNaviActivity {

    @BindView(R.id.layout_change_usertype)
    RelativeLayout mLayoutChangeUsertype;

    private List<user_type> mUs = new ArrayList<>();
    private TypesAdapter mTypesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develope);
        ButterKnife.bind(this);
        initNaviView();
        initView();
    }

    @Override
    protected String title() {
        return "开发者模式";
    }

    private void initList() {
        PostModel.getInstance().commonQuery(new FindListener<user_type>() {
            @Override
            public void done(List<user_type> list, BmobException e) {
                mUs = list;
                mTypesAdapter = new TypesAdapter(list);
//                mTypesAdapter.notifyDataSetChanged();
            }
        }, new QueryPostListener() {
            @Override
            public void addCondition(BmobQuery query) {
                query.setLimit(15);
            }
        });
    }

    @Override
    protected void initView() {
        initList();
    }

    @OnClick(R.id.layout_change_usertype)
    public void onViewClicked() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(
                R.layout.alter_list, null);
        RecyclerView recyclerView = (RecyclerView) textEntryView.findViewById(R.id.rev_types);
        if (recyclerView == null) {
            Log.d("wer", "recyele is null");
        }
        recyclerView.setAdapter(mTypesAdapter);
        Log.d("wer", "adapter:" + mTypesAdapter.toString());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.btn_selector);
        builder.setTitle("类型");
        builder.setView(textEntryView);
        builder.show();
    }

    class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.ViewHolder> {

        List<user_type> mUser_types;

        public TypesAdapter(List<user_type> user_types) {
            mUser_types = user_types;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alter_item,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final user_type u = mUser_types.get(position);
            holder.mTvTypename.setText(u.getType_name());
            holder.mTvTypename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserModel.getInstance().updateUserInfo(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Toast.makeText(DevelopeAc.this,
                                    "类型改变为："+u.getType_name(), Toast.LENGTH_SHORT).show();
                        }
                    }, new UpdatePerListener() {
                        @Override
                        public void updateCondition(_User user) {
                            user.setUser_type(u);
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return mUser_types.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_typename)
            TextView mTvTypename;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


}
