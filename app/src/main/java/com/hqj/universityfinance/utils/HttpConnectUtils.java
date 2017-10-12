package com.hqj.universityfinance.utils;

import android.os.Handler;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wang on 17-9-21.
 */

public class HttpConnectUtils {

    private static Handler mHandler = new Handler();

    public static void sendRequestByOKHttp(final String url, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();

                    final String responseData = response.body().string();
                    Log.i("wangjuncheng", "responseData = "+responseData);
                    if (listener != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onLoadSuccess(responseData.trim());
                            }
                        });
                    }
                } catch (Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLoadFailed(ConfigUtils.TYPE_LOGIN_NET_ERROR);
                        }
                    });

                    e.printStackTrace();
                }

            }
        }).start();

    }

    public static String getURLWithParams(String address, HashMap<String,String> params) throws UnsupportedEncodingException {
        //设置编码
        final String encode = "UTF-8";
        StringBuilder url = new StringBuilder(address);
        url.append("?");
        //将map中的key，value构造进入URL中
        int index = 1;
        for(Map.Entry<String, String> entry : params.entrySet()) {
            if (index > 1) {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(), encode));
            index++;
        }

        return url.toString();
    }
}
