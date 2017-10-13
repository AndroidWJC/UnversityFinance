package com.hqj.universityfinance.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hqj.universityfinance.BannerBean;
import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
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
    private List<BannerBean> mBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

        initView();

        new MyAsyncTask(ConfigUtils.TYPE_NOTICE_LIST, mListView, this)
                .execute(ConfigUtils.NOTICE_LIST_JSON_URL);

    }

    private void initView() {
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

    public void setAdapter(List<BannerBean> list) {
        mBeanList = list;
        mAdapter = new TitleOnlyItemAdapter(mBeanList, this);
        mListView.setAdapter(mAdapter);
    }

    private void refreshList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String result = MyAsyncTask.getJSONDataByHttp(ConfigUtils.NOTICE_LIST_JSON_URL);
                List<BannerBean> list = parseJson(result);
                Log.d(TAG, "run: result = "+result+ ", size = "+list.size());
                if (needUpdate(list)) {
                    mBeanList.clear();
                    mBeanList.addAll(list);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                        Utils.showToast(NoticeListActivity.this, R.string.toast_refresh_succeed);
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
        return beanList;
    }

    private boolean needUpdate(List<BannerBean> newList) {
        Log.d(TAG, "needUpdate: newList.size = "+newList.size());
        return !mBeanList.get(0).getTitle()
                .equals(newList.get(0).getTitle());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setData(Uri.parse(mBeanList.get(position).getIntentUrl()));
        startActivity(intent);
    }
}
