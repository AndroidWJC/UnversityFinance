package com.hqj.universityfinance.home;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hqj.universityfinance.BannerBean;
import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.NoticeData;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.MyAsyncTask;
import com.hqj.universityfinance.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wang on 17-9-13.
 */

public class NoticeListActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "NoticeListActivity";
    ListView mListView;
    TitleOnlyItemAdapter mAdapter = null;
    SwipeRefreshLayout mRefreshLayout;
    private List<?> mNoticeList;
    private MyAsyncTask mNoticeLoadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

        initView();

        mNoticeLoadTask = new MyAsyncTask(ConfigUtils.TYPE_NOTICE_LIST, mListView, this);
        mNoticeLoadTask.execute();
    }

    private void initView() {
        Utils.showLoadingDialog(this);
        mActionBarTitle.setText(R.string.title_notice_list);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }

    public void setAdapter(List<?> list) {
        mNoticeList = list;
        mAdapter = new TitleOnlyItemAdapter(mNoticeList, this);
        mListView.setAdapter(mAdapter);
        Utils.dismissLoadingDialog();
    }

    private void refreshList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //List<?> data = mNoticeLoadTask.getDataFromBmob();
                //Log.d(TAG, "run: size = "+data.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                        Utils.showToast(NoticeListActivity.this, R.string.toast_refresh_succeed);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setData(Uri.parse(((NoticeData)mNoticeList.get(position)).getNotice_url()));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNoticeLoadTask != null && mNoticeLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
            mNoticeLoadTask.cancel(true);
        }
    }
}
