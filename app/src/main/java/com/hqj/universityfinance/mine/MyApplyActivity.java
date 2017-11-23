package com.hqj.universityfinance.mine;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.ApplyTableInfo;
import com.hqj.universityfinance.javabean.MyApplyBean;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.HttpCallbackListener;
import com.hqj.universityfinance.utils.HttpConnectUtils;
import com.hqj.universityfinance.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import okhttp3.FormBody;

/**
 * Created by wang on 17-10-11.
 */

public class MyApplyActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<MyApplyBean> mBeanList;
    private List<ApplyTableInfo> mApplyList;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);

        mHandler = new Handler();
        initView();
        initDate();
    }

    private void initView() {
        mActionBarTitle.setText(R.string.title_my_apply);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_apply);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        Utils.showLoadingDialog(this);
    }

    private void initDate() {
        BmobQuery<ApplyTableInfo> query = new BmobQuery<>();
        query.setLimit(15);
        query.addWhereEqualTo("s_id", Integer.valueOf(ConfigUtils.getCurrentUserId()));
        query.order("-createdAt");
        query.findObjects(this, new FindListener<ApplyTableInfo>() {
            @Override
            public void onSuccess(List<ApplyTableInfo> list) {
                mApplyList = list;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyApplyAdapter adapter = new MyApplyAdapter(MyApplyActivity.this, mApplyList);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
                Utils.dismissLoadingDialog();
            }

            @Override
            public void onError(int i, String s) {
                Utils.dismissLoadingDialog();
                Utils.showToast(MyApplyActivity.this, R.string.login_failed_net_error);
            }
        });
    }

}
