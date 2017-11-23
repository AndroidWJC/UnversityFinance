package com.hqj.universityfinance.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.hqj.universityfinance.BannerBean;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.home.NoticeListActivity;
import com.hqj.universityfinance.home.TitleOnlyItemAdapter;
import com.hqj.universityfinance.home.WebViewActivity;
import com.hqj.universityfinance.javabean.NewsData;
import com.hqj.universityfinance.javabean.NoticeData;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by wang on 17-9-12.
 */

public class MyAsyncTask extends AsyncTask<String, Void, List<?>> {

    private static final String TAG = "MyAsyncTask";
    private Banner mBanner;
    private ViewFlipper mViewFilpper;
    private SwipeRefreshLayout mRefreshLayout;
    private ListView mListView;
    private Context mContext;
    private NoticeListActivity mNoticeListActivity;

    private int mType = 0;

    private boolean mDone;
    private List<?> mDataList = new ArrayList<>();

    public MyAsyncTask(int type, Banner banner, Context context) {
        mType = type;
        mBanner = banner;
        mContext = context;
    }

    public MyAsyncTask(int type, ViewFlipper viewFlipper, Context context) {
        mType = type;
        mViewFilpper = viewFlipper;
        mContext = context;
    }

    public MyAsyncTask(int type, ListView listView, Context context) {
        mType = type;
        mListView = listView;
        mNoticeListActivity = (NoticeListActivity) context;
    }

    @Override
    protected List<?> doInBackground(String... strings) {
        return getDataFromBmob();
    }

    @Override
    protected void onPostExecute(List<?> objects) {
        super.onPostExecute(objects);
        Log.d(TAG, "onPostExecute: wjc: onPostExecute type = "+mType);
        switch (mType) {
            case ConfigUtils.TYPE_NEWS:
                List<String> imgList = new ArrayList<>();
                List<String> titleList = new ArrayList<>();
                final List<String> urlList = new ArrayList<>();
                NewsData data;
                for (int i = 0; i < objects.size(); i++) {
                    data = (NewsData) objects.get(i);
                    imgList.add(data.getPicture().getUrl());
                    titleList.add(data.getTitle());
                    urlList.add(data.getNew_url());
                }
                //设置图片加载器
                mBanner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                });
                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.setData(Uri.parse(urlList.get(position)));
                        mContext.startActivity(intent);
                    }
                });
                mBanner.setImages(imgList);
                mBanner.setBannerTitles(titleList);
                mBanner.start();

                break;

            case ConfigUtils.TYPE_NOTICE:
                TextView view;
                NoticeData notice;
                for (int i = 0; i < objects.size(); i = i + 2) {
                    notice = (NoticeData) objects.get(i);
                    view = new TextView(mContext);
                    view.setText(notice.getTitle());
                    view.append("\n");

                    notice = (NoticeData) objects.get(i+1);
                    view.append(notice.getTitle());
                    view.setTextColor(mContext.getResources().getColor(R.color.color_text));
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    mViewFilpper.addView(view);
                }
                mViewFilpper.startFlipping();

                break;

            case ConfigUtils.TYPE_NOTICE_LIST:
                mNoticeListActivity.setAdapter(objects);
                break;
        }
    }

    public List<?> getDataFromBmob(){
        mDone = false;
        mDataList.clear();

        switch (mType) {
            case ConfigUtils.TYPE_NEWS:
                BmobQuery<NewsData> query1 = new BmobQuery<>();
                query1.setLimit(5);
                query1.findObjects(mContext, new FindListener<NewsData>() {
                    @Override
                    public void onSuccess(List<NewsData> list) {
                        mDataList = list;
                        mDone = true;
                    }

                    @Override
                    public void onError(int i, String s) {
                        mDone = true;
                    }
                });
                break;

            case ConfigUtils.TYPE_NOTICE:
                BmobQuery<NoticeData> query2 = new BmobQuery<>();
                query2.order("-createdAt"); //descending
                query2.setLimit(6);
                query2.findObjects(mContext, new FindListener<NoticeData>() {
                    @Override
                    public void onSuccess(List<NoticeData> list) {
                        mDataList = list;
                        mDone = true;
                    }

                    @Override
                    public void onError(int i, String s) {
                        mDone = true;
                    }
                });
                break;

            case ConfigUtils.TYPE_NOTICE_LIST:
                BmobQuery<NoticeData> query3 = new BmobQuery<>();
                query3.order("-createdAt"); //descending
                query3.setLimit(15);
                query3.findObjects(mNoticeListActivity, new FindListener<NoticeData>() {
                    @Override
                    public void onSuccess(List<NoticeData> list) {
                        Log.d(TAG, "onSuccess: wjc: size "+list.size());
                        mDataList = list;
                        mDone = true;
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.d(TAG, "onError: wjc: s "+s);
                        mDone = true;
                    }
                });
                break;
        }

        waitDone();

        return mDataList;
    }

    private void waitDone() {
        while (!mDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mDone = false;
    }


    public static String getJSONDataByHttp(String url){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        String result = null;
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getJSONDataByHttp: result = "+result);
        return result;
    }

    private List<BannerBean> parseJSON(String result) {
        List<BannerBean> beanList = new ArrayList<>();
        BannerBean bannerBean = null;

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            switch (mType) {
                case ConfigUtils.TYPE_NEWS:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bannerBean = new BannerBean();
                        jsonObject = jsonArray.getJSONObject(i);
                        Log.d(TAG, "getJSONData: title = " + jsonObject.getString("title")
                                + ", pic = " + jsonObject.getString("img")
                                + ", url = " + jsonObject.getString("url"));
                        bannerBean.setTitle(jsonObject.getString("title"));
                        bannerBean.setIcon(jsonObject.getString("img"));
                        bannerBean.setIntentUrl(jsonObject.getString("url"));
                        beanList.add(bannerBean);
                    }
                    break;

                case ConfigUtils.TYPE_NOTICE:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bannerBean = new BannerBean();
                        jsonObject = jsonArray.getJSONObject(i);
                        bannerBean.setTitle(jsonObject.getString("title"));
                        bannerBean.setTitle2(jsonObject.getString("title2"));
                        beanList.add(bannerBean);
                    }
                    break;

                case ConfigUtils.TYPE_NOTICE_LIST:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bannerBean = new BannerBean();
                        jsonObject = jsonArray.getJSONObject(i);
                        bannerBean.setTitle(jsonObject.getString("title"));
                        bannerBean.setIntentUrl(jsonObject.getString("url"));
                        beanList.add(bannerBean);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            beanList.clear();
            bannerBean = new BannerBean();
            bannerBean.setTitle("网络异常");
            bannerBean.setTitle2("请检查网络");
            beanList.add(bannerBean);
            return beanList;
        }

        return beanList;
    }
}
