package com.hqj.universityfinance.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by wang on 17-9-20.
 */

public class Utils {

    static ProgressDialog mProgressDialog = null;
    static SharedPreferences mSP = null;
    static SharedPreferences.Editor mEditor = null;

    public static void showToast(Context context, int resId) {
        if (context != null) {
            String msg = context.getResources().getString(resId);
            showToast(context, msg);
        }
    }

    public static void showToast(Context context, String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLoadingDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public static void showLoadingDialog(Context context, int titleId, int messageId) {
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
    }

    public static void dismissLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
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

    public static boolean getBooleanFromSharedPreferences(Context context, String key) {

        mSP = PreferenceManager.getDefaultSharedPreferences(context);
        return mSP.getBoolean(key, false);
    }

    public static String getStringFromSharedPreferences(Context context, String key) {

        mSP = PreferenceManager.getDefaultSharedPreferences(context);
        return mSP.getString(key, "");
    }

}
