package com.hqj.universityfinance.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hqj.universityfinance.R;
import com.hqj.universityfinance.customview.LoadingView;

/**
 * Created by wang on 17-9-20.
 */

public class Utils {

    private static Dialog mProgressDialog = null;
    private static LoadingView mLoadingView = null;
    private static SharedPreferences mSP = null;
    private static SharedPreferences.Editor mEditor = null;

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
        if (context == null) {
            return;
        }

        showLoadingDialog(context, 0, 0);
    }

    public static void showLoadingDialog(final Context context, final int titleId, final int messageId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    RelativeLayout dialogView = (RelativeLayout) LayoutInflater.from(context).inflate(
                            R.layout.dialog_progress_view, null, false);
                    mLoadingView = (LoadingView) dialogView.findViewById(R.id.loading_view);
                    mProgressDialog = new Dialog(context, ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setContentView(dialogView);
                    mProgressDialog.setCancelable(false);
                    if (titleId != 0) {
                        mProgressDialog.setTitle(titleId);
                    }
                    if (messageId != 0) {
                        TextView progressMessage = (TextView) dialogView
                                .findViewById(R.id.dialog_progress_message_text);
                        progressMessage.setText(messageId);
                    }
                }
                mProgressDialog.show();
                mLoadingView.startLoading();
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
                    mLoadingView.clearAllAnimator();
                    mProgressDialog = null;
                    mLoadingView = null;
                }
            }
        });

    }

    public static void dismissLoadingDialog(final boolean succeed,
                                            @Nullable final Animator.AnimatorListener listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    if (succeed) {
                        mLoadingView.loadSucceed(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                mProgressDialog.dismiss();
                                mLoadingView.clearAllAnimator();
                                mProgressDialog = null;
                                mLoadingView = null;
                                if (listener != null) {
                                    listener.onAnimationEnd(animator);
                                }
                            }
                        });
                    } else {
                        mLoadingView.loadFailed(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mProgressDialog.dismiss();
                                mLoadingView.clearAllAnimator();
                                mProgressDialog = null;
                                mLoadingView = null;
                                if (listener != null) {
                                    listener.onAnimationEnd(animation);
                                }

                            }
                        });
                    }
                }
            }
        });

    }

    public static void writeToSharedPreferences(Context context, String key, boolean value) {
        if (mEditor == null) {
            mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        }

        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public static void writeToSharedPreferences(Context context, String key, String value) {
        if (mEditor == null) {
            mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        }

        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static void writeToSharedPreferences(Context context, String key, int value) {
        if (mEditor == null) {
            mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        }

        mEditor.putInt(key, value);
        mEditor.commit();
    }


    public static boolean getBooleanFromSharedPreferences(Context context, String key) {
        if (mSP == null) {
            mSP = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return mSP.getBoolean(key, false);
    }

    public static String getStringFromSharedPreferences(Context context, String key) {
        if (mSP == null) {
            mSP = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return mSP.getString(key, "");
    }
}
