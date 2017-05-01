package com.example.java.api25.Test.Recycle_Test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.java.api25.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59575 on 2017/4/13.
 */

class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgs = new ArrayList<>();

    MsgAdapter(List<Msg> msgs) {
        this.mMsgs = msgs;
    }

    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgs.get(position);
        if (msg.getDirection() == Msg.LEFT_MSG) {
            holder.mTextViewleft.setVisibility(View.VISIBLE);
            holder.mTextViewright.setVisibility(View.GONE);
            holder.mTextViewleft.setText(msg.getContent());
        } else {
            holder.mTextViewleft.setVisibility(View.GONE);
            holder.mTextViewright.setVisibility(View.VISIBLE);
            holder.mTextViewright.setText(msg.getContent());
        }

    }

    @Override
    public int getItemCount() {
        return mMsgs.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewleft;
        TextView mTextViewright;
        private ViewHolder(View itemView) {
            super(itemView);
            mTextViewleft = (TextView) itemView.findViewById(R.id.msg_text_left);
            mTextViewright = (TextView) itemView.findViewById(R.id.msg_text_right);
        }
    }
}
