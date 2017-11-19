package com.hqj.universityfinance;

import android.content.ContentValues;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
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
    private boolean pullDone = false;
    private boolean pullSucceed = false;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getWritableDatabase();
        mHandler = new Handler();

        updateSchoolYear();
        confirmAccount();
    }

    private void confirmAccount() {

        String account = Utils.getStringFromSharedPreferences(this, "account");
        String password = Utils.getStringFromSharedPreferences(this, "password");

        pullProjectDataToDB();
        if (needUserLogin(account, password)) {
            waitPullDone();
        } else {
            automaticLogin(account, password);
            waitLoginAndPullDone();
        }
    }

    private void pullProjectDataToDB() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", ConfigUtils.TYPE_POST_GET_PROJECT);

        String urlWithInfo = null;
        try {
            urlWithInfo = HttpConnectUtils.getURLWithParams(ConfigUtils.SERVER_URL, params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpConnectUtils.sendRequestByOKHttp(urlWithInfo, new HttpCallbackListener() {
            @Override
            public void onLoadSuccess(String response) {
                Log.d(TAG, "pullProjectDataToDB: response = "+response);
                if (response.startsWith(ConfigUtils.SUCCESSFUL)) {
                    pullSucceed = saveDataToDB(response);
                }
                pullDone = true;
            }

            @Override
            public void onLoadFailed(int reason) {
                pullDone = true;
                pullSucceed = false;
            }
        });
    }

    private boolean saveDataToDB(String jsonData) {
        JSONObject object = null;
        JSONArray array = null;
        Log.d(TAG, "saveDataToDB: jsonData = "+jsonData);

        try {
            object = new JSONObject(jsonData);
            array = object.getJSONArray("project");

            ContentValues value = new ContentValues();
            for (int i = 0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                String projectId = object.getString("z_id");
                if (ifProjectExist(projectId)) {
                    continue;
                }
                value.put("z_id", object.getString("z_id"));
                value.put("z_name", object.getString("z_name"));
                value.put("z_status", object.getInt("z_status"));
                value.put("z_sum", object.getString("z_sum"));
                value.put("z_time", object.getString("z_time"));
                value.put("z_quota", object.getString("z_quota"));
                value.put("z_describe", object.getString("z_describe"));
                mDB.insert(ConfigUtils.TABLE_PROJECT, null, value);
                value.clear();
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean ifProjectExist(String projectId) {
        Cursor cursor = mDB.rawQuery("select z_name from " + ConfigUtils.TABLE_PROJECT + " where z_id=?",
                new String[]{projectId});
        boolean exist = cursor.moveToFirst();
        cursor.close();
        return exist;
    }

    private void automaticLogin(String account, String password) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", ConfigUtils.TYPE_POST_LOGIN);
        params.put("account", account);
        params.put("password", password);

        String urlWithInfo = null;
        try {
            urlWithInfo = HttpConnectUtils.getURLWithParams(ConfigUtils.SERVER_URL, params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpConnectUtils.sendRequestByOKHttp(urlWithInfo, new HttpCallbackListener() {
            @Override
            public void onLoadSuccess(String response) {
                loginDone = true;
                loginSucceed = response.startsWith(ConfigUtils.SUCCESSFUL);
            }

            @Override
            public void onLoadFailed(int reason) {
                loginDone = true;
                loginSucceed = false;
            }
        });
    }

    private void waitLoginAndPullDone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(loginDone && pullDone)) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (loginSucceed) {

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

    private void waitPullDone() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!pullDone) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!pullSucceed) Utils.showToast(WelcomeActivity.this, "pull project failed");

                        goToActivity(LoginActivity.class);
                    }
                });
            }
        }).start();
    }

    private boolean needUserLogin(String account, String password) {
        return account.equals("") || password.equals("");
    }

    private void goToActivity(Class<?> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
        finish();
    }

    private void updateSchoolYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String schoolYear;

        if (month >= 9) {
            schoolYear = year + "-" + (year + 1) + "学年";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年";
        }
        Log.d(TAG, "updateSchoolYear: schoolYear = "+schoolYear+", year = "+year+", month = "+month);
        Utils.writeToSharedPreferences(this, "school_year", schoolYear);

        Utils.writeToSharedPreferences(this, "year", year);
        Utils.writeToSharedPreferences(this, "month", month);
        Utils.writeToSharedPreferences(this, "day", dayOfMonth);
    }
}
