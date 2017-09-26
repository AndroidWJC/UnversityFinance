package com.hqj.universityfinance.home;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;

import com.hqj.universityfinance.BannerBean;
import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.MyAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wang on 17-9-13.
 */

public class NoticeListActivity extends BaseActivity{

    private static final String TAG = "NoticeListActivity";
    ListView mListView;
    NoticeAdapter mAdapter = null;
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

        initView();

        new MyAsyncTask(ConfigUtils.TYPE_NOTICE_LIST, mListView, mAdapter, this)
                .execute(ConfigUtils.NOTICE_LIST_JSON_URL);

    }

    private void initView() {
        mActionBarTitle.setText(R.string.title_notice_list);

        mListView = (ListView) findViewById(R.id.list_view);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }

    private void refreshList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mAdapter != null) {
                    Log.d(TAG, "run: wangjuncheng mAdapter != null");
                    mAdapter.notifyDataSetChanged();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private List<BannerBean> parseJson(String data) {
        List<BannerBean> beanList = new ArrayList<>();
        BannerBean bannerBean = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                bannerBean = new BannerBean();
                jsonObject = jsonArray.getJSONObject(i);
                bannerBean.setTitle(jsonObject.getString("title"));
                bannerBean.setIntentUrl(jsonObject.getString("url"));
                beanList.add(bannerBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
