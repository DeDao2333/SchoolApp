package com.example.java.api25.Test.Recycle_Test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.java.api25.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Msg> mMsgs = new ArrayList<>();

    private Button mSendMsg;
    private EditText mEditText;
    private int STATE=0;

    RecyclerView recyclerView;
    MsgAdapter mMsgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSendMsg = (Button) findViewById(R.id.recycle_button);
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (STATE) {
                    case 0:
                        setSendMsg();
                        mEditText.setText("");
                        recyclerView.scrollToPosition(mMsgs.size()-1);
                        STATE=(STATE+1)%2;
                        break;
                    case 1:
                        recyclerView.scrollToPosition(0);
                        STATE=(STATE+1)%2;
                        break;
                    default:
                        break;
                }
            }
        });
        mEditText = (EditText) findViewById(R.id.recycle_edit);
        initWord();

        mMsgAdapter = new MsgAdapter(mMsgs);
        recyclerView = (RecyclerView) findViewById(R.id.fruit_recycle);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(mMsgAdapter);
    }

    public void initWord() {
        for(int i=0;i<4;i++) {
            mMsgs.add(new Msg("hello", Msg.LEFT_MSG));
        }
    }

    public void setSendMsg() {
        Msg msg = new Msg("" + mEditText.getText().toString(), Msg.RIGHT_MSG);
        mMsgs.add(msg);
        mMsgAdapter.notifyItemInserted(mMsgs.size()-1);
    }
}
