package com.example.java.api25.MyObject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.java.api25.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59575 on 2017/4/20.
 */

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {

    private List<OrderCodeObject> mObjects = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mHead_Image;
        TextView mContent_Text;
        TextView mName_Text;
        public ViewHolder(View itemView) {
            super(itemView);
            mContent_Text = (TextView) itemView.findViewById(R.id.main_content_text);
            mHead_Image = (ImageView) itemView.findViewById(R.id.main_head_image);
            mName_Text = (TextView) itemView.findViewById(R.id.main_name_text);
        }
    }

    public ObjectAdapter(List<OrderCodeObject> objects) {
        this.mObjects = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderCodeObject object = mObjects.get(position);
        holder.mName_Text.setText(object.getName());
        holder.mHead_Image.setImageResource(object.getResId());
        holder.mContent_Text.setText(object.getContent());
    }


    @Override
    public int getItemCount() {
        return mObjects.size();
    }
}
