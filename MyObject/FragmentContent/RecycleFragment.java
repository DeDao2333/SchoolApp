package com.example.java.api25.MyObject.FragmentContent;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.java.api25.MyObject.ObjectAdapter;
import com.example.java.api25.MyObject.OrderCodeObject;
import com.example.java.api25.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecycleFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<OrderCodeObject> mObjects = new ArrayList<>();
    private ObjectAdapter mObjectAdapter;
    private Context mContext;

    public RecycleFragment(Context context,List<OrderCodeObject> objects) {
        this.mContext = context;
        this.mObjects = objects;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recycle, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_page_recycle);
        mObjectAdapter = new ObjectAdapter(mObjects);
        RecyclerView.LayoutManager linear = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linear);
        mRecyclerView.setAdapter(mObjectAdapter);
        // Inflate the layout for this fragment
        return view;
    }

}
