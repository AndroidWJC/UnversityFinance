package com.hqj.universityfinance.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wang on 17-9-20.
 */

public class Utils {

    static ProgressDialog mProgressDialog = null;
    static SharedPreferences mSP = null;
    static SharedPreferences.Editor mEditor = null;

    private static Handler mHandler = new Handler();

    public static void showToast(final Context context, final int resId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    String msg = context.getResources().getString(resId);
                    showToast(context, msg);
                }
            }
        });
    }

    public static void showToast(final Context context, final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void showLoadingDialog(final Context context) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCancelable(false);
                }
                mProgressDialog.show();
            }
        });

    }

    public static void showLoadingDialog(final Context context, final int titleId, final int messageId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCancelable(false);
                    if (titleId != 0) {
                        mProgressDialog.setTitle(titleId);
                    }
                    if (messageId != 0) {
                        mProgressDialog.setTitle(messageId);
                    }
                }
                mProgressDialog.show();
                Log.d("wangjuncheng", "run: showLoadingDialog end");
            }
        });
    }

    public static void dismissLoadingDialog() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        });

    }

    public static void writeToSharedPreferences(Context context, String key, boolean value) {

        mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public static void writeToSharedPreferences(Context context, String key, String value) {

        mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static void writeToSharedPreferences(Context context, String key, int value) {

        mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }


    public static boolean getBooleanFromSharedPreferences(Context context, String key) {

        mSP = PreferenceManager.getDefaultSharedPreferences(context);
        return mSP.getBoolean(key, false);
    }

    public static String getStringFromSharedPreferences(Context context, String key) {

        mSP = PreferenceManager.getDefaultSharedPreferences(context);
        return mSP.getString(key, "");
    }

}
