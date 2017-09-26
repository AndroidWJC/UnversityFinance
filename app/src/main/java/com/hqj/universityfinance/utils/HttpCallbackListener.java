package com.hqj.universityfinance.utils;

/**
 * Created by wang on 17-7-31.
 */

public interface HttpCallbackListener {

    void onLoadSuccess(String response);

    void onLoadFailed(int reason);
}
