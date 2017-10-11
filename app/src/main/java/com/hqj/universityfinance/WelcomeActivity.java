package com.hqj.universityfinance;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.HttpCallbackListener;
import com.hqj.universityfinance.utils.HttpConnectUtils;
import com.hqj.universityfinance.utils.Utils;

import java.util.HashMap;

/**
 * Created by wang on 17-9-21.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    private Handler mHandler;
    private Runnable task = null;

    private boolean loginDone = false;
    private boolean loginSucceed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mHandler = new Handler();
        confirmAccount();
    }

    private void confirmAccount() {

        String account = Utils.getStringFromSharedPreferences(this, "account");
        String password = Utils.getStringFromSharedPreferences(this, "password");

        if (needUserLogin(account, password)) {
            task = new Runnable() {
                @Override
                public void run() {
                    goToActivity(LoginActivity.class);
                }
            };

            mHandler.postDelayed(task, 3000);
        } else {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("account", account);
            params.put("password", password);

            try {
                String urlWithInfo = HttpConnectUtils.getURLWithParams(ConfigUtils.SERVER_URL, params);

                HttpConnectUtils.sendRequestByOKHttp(urlWithInfo, new HttpCallbackListener() {
                    @Override
                    public void onLoadSuccess(String response) {
                        loginDone = true;
                        loginSucceed = response.startsWith(ConfigUtils.LOGIN_PASS);
                    }

                    @Override
                    public void onLoadFailed(int reason) {
                        loginDone = true;
                        loginSucceed = false;
                    }
                });
            } catch (Exception e) {

            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!loginDone) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (loginSucceed) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        task = new Runnable() {
                            @Override
                            public void run() {
                                goToActivity(MainActivity.class);
                            }
                        };
                    } else {
                        task = new Runnable() {
                            @Override
                            public void run() {
                                goToActivity(LoginActivity.class);
                            }
                        };
                    }

                    mHandler.post(task);
                }
            }).start();

        }
    }

    private boolean needUserLogin(String account, String password) {
        return account.equals("") || password.equals("");
    }

    private void goToActivity(Class<?> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
        finish();
    }

}
