package com.hqj.universityfinance.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.MessageData;
import com.hqj.universityfinance.javabean.ProjectInfo;
import com.hqj.universityfinance.javabean.TeacherInfo;
import com.hqj.universityfinance.utils.SaveDataService;
import com.hqj.universityfinance.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wang on 17-11-26.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private List<MessageData> mMsgList;
    private ChatMsgAdapter mMsgAdapter;

    private int mReceiverId;
    private String mReceiverName;
    private int mSenderId;
    private Handler mHandler;

    private Button mSendBtn;
    private EditText mSendContentEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mHandler = new Handler();

        initView();
        initData();
    }

    private void initView() {
        mSendBtn = (Button) findViewById(R.id.send);
        mSendBtn.setOnClickListener(this);
        mSendContentEt = (EditText) findViewById(R.id.send_content);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        Utils.showLoadingDialog(this);
    }

    private void initData() {
        mMsgList = new ArrayList<>();
        mReceiverId = 129061289;
        mSenderId = Integer.valueOf(Utils.getStringFromSharedPreferences(this, "account"));

        BmobQuery<TeacherInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("t_id", mReceiverId);
        query.findObjects(this, new FindListener<TeacherInfo>() {
            @Override
            public void onSuccess(List<TeacherInfo> list) {
                if (list.size() > 0) {
                    mReceiverName = list.get(0).getName();
                    setReceiverName();
                }
            }

            @Override
            public void onError(int i, String s) {
                Utils.showToast(ChatActivity.this, "error " + s);
            }
        });

        BmobQuery<MessageData> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("send_id", mSenderId);

        BmobQuery<MessageData> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("receive_id", mSenderId);

        List<BmobQuery<MessageData>> list = new ArrayList<>();
        list.add(query1);
        list.add(query2);

        BmobQuery<MessageData> orQuery = new BmobQuery<>();
        orQuery.or(list);
        orQuery.order("createdAt");
        orQuery.findObjects(this, new FindListener<MessageData>() {
            @Override
            public void onSuccess(List<MessageData> list) {
                mMsgList = list;
                setAdapter();
                Utils.dismissLoadingDialog();
            }

            @Override
            public void onError(int i, String s) {
                Utils.dismissLoadingDialog();
                Utils.showToast(ChatActivity.this, "error " + s);
            }
        });
    }


    private void setReceiverName() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mActionBarTitle.setText(mReceiverName);
            }
        });
    }

    private void setAdapter() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMsgAdapter = new ChatMsgAdapter(ChatActivity.this, mMsgList);
                mRecyclerView.setAdapter(mMsgAdapter);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                if (!mSendContentEt.getText().toString().equals("")) {
                    updateMsg();
                    mSendContentEt.setText("");
                }
                break;

        }
    }

    private void updateMsg() {
        MessageData msg = new MessageData();
        msg.setMsg_content(mSendContentEt.getText().toString());
        msg.setSend_id(mSenderId);
        msg.setReceive_id(mReceiverId);
        mMsgList.add(msg);
        mMsgAdapter.notifyItemInserted(mMsgList.size() - 1);
        mRecyclerView.scrollToPosition(mMsgList.size() - 1);
        saveMsgDataToServer(msg);

        MessageData msg1 = new MessageData();
        msg1.setMsg_content("对方的状态为忙碌，请稍等");
        msg1.setSend_id(mReceiverId);
        msg1.setReceive_id(mSenderId);
        mMsgList.add(msg1);
        mMsgAdapter.notifyItemInserted(mMsgList.size() - 1);
        mRecyclerView.scrollToPosition(mMsgList.size() - 1);
    }

    private void saveMsgDataToServer(MessageData data) {
        Intent intent = new Intent(this, SaveDataService.class);
        intent.putExtra("data", data);
        intent.putExtra("type", 3);
        startService(intent);
    }

}
