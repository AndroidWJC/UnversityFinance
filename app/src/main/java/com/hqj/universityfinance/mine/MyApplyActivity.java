package com.hqj.universityfinance.mine;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
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

import okhttp3.FormBody;

/**
 * Created by wang on 17-10-11.
 */

public class MyApplyActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<MyApplyBean> mBeanList;
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
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_apply);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        Utils.showLoadingDialog(this);
    }

    private void initDate() {
        FormBody.Builder builder = new FormBody.Builder();
        String account = Utils.getStringFromSharedPreferences(this, "account");
        builder.add("account", account);
        builder.add("type", "2");

        HttpConnectUtils.postRequestByOKHttp(ConfigUtils.SERVER_URL,
                builder,
                new HttpCallbackListener() {
                    @Override
                    public void onLoadSuccess(final String response) {
                        Log.d("MyApplyActivity", "onLoadSuccess: response="+response);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                parseDataFromJson(response);
                                MyApplyAdapter adapter = new MyApplyAdapter(MyApplyActivity.this, mBeanList);
                                mRecyclerView.setAdapter(adapter);
                            }
                        });

                        Utils.dismissLoadingDialog();
                    }

                    @Override
                    public void onLoadFailed(int reason) {
                        Utils.dismissLoadingDialog();
                        Utils.showToast(MyApplyActivity.this, R.string.toast_unknown_error);
                    }
                });
    }

    private void parseDataFromJson(String jsonString) {
        mBeanList = new ArrayList<>();
        JSONObject jsonObject = null;
        MyApplyBean bean = null;

        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("apply_history");
            for (int i = 0; i < jsonArray.length(); i++) {
                bean = new MyApplyBean();
                jsonObject = jsonArray.getJSONObject(i);
                String z_id = jsonObject.getString("z_id");
                bean.setProjectId(z_id);
                bean.setVerifyResult(jsonObject.getInt("verify_result"));
                bean.setTime(jsonObject.getString("a_time"));
                mBeanList.add(bean);
            }

        } catch (JSONException e) {
            Log.d("MyApplyActivity", "parseDataFromJson: e = "+e.toString());
            e.printStackTrace();
        }

    }

}
