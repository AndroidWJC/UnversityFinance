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
import com.hqj.universityfinance.home.WebViewActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by wang on 17-9-12.
 */

public class MyAsyncTask extends AsyncTask<String, Void, List<BannerBean>> {

    private static final String TAG = "MyAsyncTask";
    private Banner mBanner;
    private ViewFlipper mViewFilpper;
    private SwipeRefreshLayout mRefreshLayout;
    private ListView mListView;
    private Context mContext;
    private NoticeListActivity mNLActivity;

    private int mType = 0;

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
        mNLActivity = (NoticeListActivity) context;
    }

    @Override
    protected List<BannerBean> doInBackground(String... strings) {
        String result = null;
        switch (mType) {
            case ConfigUtils.TYPE_NEWS:
                result = getJSONDataByHttp(strings[0]);
                return parseJSON(result);

            case ConfigUtils.TYPE_NOTICE:
                result = getJSONDataByHttp(strings[0]);
                return parseJSON(result);

            case ConfigUtils.TYPE_NOTICE_LIST:
                result = getJSONDataByHttp(strings[0]);
                return parseJSON(result);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<BannerBean> beenList) {
        super.onPostExecute(beenList);
        List<String> imgList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        final List<String> urlList = new ArrayList<>();
        switch (mType) {
            case ConfigUtils.TYPE_NEWS:
                for (BannerBean list : beenList) {
                    Log.d(TAG, "getJSONData 2: title = "+list.getTitle()
                            +", pic = "+list.getIcon()
                            +", url = "+list.getIntentUrl());
                    imgList.add(list.getIcon());
                    titleList.add(list.getTitle());
                    urlList.add(list.getIntentUrl());
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

                for (BannerBean list : beenList) {
                    mViewFilpper.addView(createNoticeView(list));
                }
                mViewFilpper.startFlipping();

                break;

            case ConfigUtils.TYPE_NOTICE_LIST:
                mNLActivity.setAdapter(beenList);
        }
    }

    private TextView createNoticeView(BannerBean content) {
        TextView view = new TextView(mContext);
        view.setText(content.getTitle());
        view.append("\n");
        view.append(content.getTitle2());
        view.setTextColor(mContext.getResources().getColor(R.color.color_text));
        view.setGravity(Gravity.CENTER_VERTICAL);

        return view;
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
