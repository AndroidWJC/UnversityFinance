package com.hqj.universityfinance.mine;

import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.ChatPeopleData;
import com.hqj.universityfinance.javabean.MessageData;
import com.hqj.universityfinance.javabean.TeacherInfo;
import com.hqj.universityfinance.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wang on 17-11-26.
 */

public class MyMessageActivity extends BaseActivity {

    RecyclerView mRecyclerView;

    private List<ChatPeopleData> mPeopleList;
    private ChatPeopleListAdapter mAdapt;
    private Handler mHandler;

    private int mReceiverId;
    private int mSenderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_recycler_view);

        mHandler = new Handler();
        initView();
        initData();
    }

    private void initView() {
        mActionBarTitle.setText(R.string.title_message);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        Utils.showLoadingDialog(this);
    }

    private void initData() {
        mPeopleList = new ArrayList<>();
        mReceiverId = 129061289;
        mSenderId = Integer.valueOf(Utils.getStringFromSharedPreferences(this, "account"));

        BmobQuery<TeacherInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("t_id", mReceiverId);
        query.findObjects(this, new FindListener<TeacherInfo>() {
            @Override
            public void onSuccess(List<TeacherInfo> list) {
                if (list.size() > 0) {
                    ChatPeopleData data = new ChatPeopleData();
                    data.setPhoto_url(list.get(0).getPhoto().getUrl());
                    data.setName(list.get(0).getName());
                    queryMsgInfo(data);
                }
            }

            @Override
            public void onError(int i, String s) {
                Utils.showToast(MyMessageActivity.this, "error " + s);
            }
        });

    }

    private void queryMsgInfo(final ChatPeopleData data) {

        BmobQuery<MessageData> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("send_id", mSenderId);

        BmobQuery<MessageData> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("receive_id", mSenderId);

        List<BmobQuery<MessageData>> list = new ArrayList<>();
        list.add(query1);
        list.add(query2);

        BmobQuery<MessageData> orQuery = new BmobQuery<>();
        orQuery.or(list);
        orQuery.order("-createdAt");
        orQuery.setLimit(1);
        orQuery.findObjects(this, new FindListener<MessageData>() {
            @Override
            public void onSuccess(List<MessageData> list) {
                if (list.size() > 0) {
                    data.setSummary(list.get(0).getMsg_centent());
                    data.setTime(list.get(0).getCreatedAt());
                    mPeopleList.add(data);
                    setAdapter();
                }
                Utils.dismissLoadingDialog();
            }

            @Override
            public void onError(int i, String s) {
                Utils.dismissLoadingDialog();
                Utils.showToast(MyMessageActivity.this, "error " + s);
            }
        });
    }

    private void setAdapter() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mAdapt = new ChatPeopleListAdapter(MyMessageActivity.this, mPeopleList);
                mRecyclerView.setAdapter(mAdapt);
            }
        });
    }
}
