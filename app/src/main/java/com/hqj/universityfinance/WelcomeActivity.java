package com.hqj.universityfinance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.hqj.universityfinance.javabean.ProjectInfo;
import com.hqj.universityfinance.javabean.StudentInfo;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wang on 17-9-21.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    private static final String BMOB_APP_ID = "783ba84369a8f449a32581d085ea0094";
    private Handler mHandler;
    private Runnable task = null;

    private boolean mLoginDone = false;
    private boolean mLoginSucceed = false;
    private boolean mPullDone = false;
    private boolean mPullSucceed = false;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setSystemUI();
        initMemberVariable();
        initThirdService();

        updateSchoolYear();
        confirmAccount();
    }

    private void setSystemUI() {
        if (Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initMemberVariable() {
        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getWritableDatabase();
        mHandler = new Handler();
    }

    private void initThirdService() {
        Bmob.initialize(getApplicationContext(), BMOB_APP_ID);
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
        BmobQuery<ProjectInfo> query = new BmobQuery<>();
        query.setLimit(20);
        query.findObjects(this, new FindListener<ProjectInfo>() {
            @Override
            public void onSuccess(List<ProjectInfo> list) {
                Log.d(TAG, "wjc: onSuccess: project size = "+list.size());
                if (list.size() > 0) {
                    mPullSucceed = saveDataToDB(list);
                }
                mPullDone = true;
                Log.d(TAG, "wjc: onSuccess: pull project done");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "wjc: onSuccess: pull project error "+s);
                mPullDone = true;
                mPullSucceed = false;
            }
        });

    }

    private boolean saveDataToDB(List<ProjectInfo> infos) {
        List<ProjectInfo> data = infos;

        ContentValues value = new ContentValues();
        ProjectInfo info;
        String projectId;
        try {
            for (int i = 0; i < data.size(); i++) {
                info = infos.get(i);
                projectId = info.getZ_id();
                if (ifProjectExist(projectId)) {
                    continue;
                }
                value.put("z_id", projectId);
                value.put("z_name", info.getZ_name());
                value.put("z_status", info.getZ_status());
                value.put("z_sum", info.getZ_sum());
                value.put("z_time", info.getZ_time());
                value.put("z_quota", info.getZ_quota());
                value.put("z_describe", info.getZ_describe());
                mDB.insert(ConfigUtils.TABLE_PROJECT, null, value);
                value.clear();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean ifProjectExist(String projectId) {
        Cursor cursor = mDB.rawQuery("select z_name from " + ConfigUtils.TABLE_PROJECT + " where z_id=?",
                new String[]{projectId});
        boolean exist = cursor.moveToFirst();
        cursor.close();
        return exist;
    }

    private void automaticLogin(String account, String password) {
        BmobQuery<StudentInfo> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("id", Integer.parseInt(account));
        BmobQuery<StudentInfo> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("password", password);

        List<BmobQuery<StudentInfo>> andQuery = new ArrayList<>();
        andQuery.add(query1);
        andQuery.add(query2);

        BmobQuery<StudentInfo> query = new BmobQuery<>();
        query.and(andQuery);
        query.findObjects(this, new FindListener<StudentInfo>() {
            @Override
            public void onSuccess(List<StudentInfo> list) {
                mLoginDone = true;
                mLoginSucceed = (list.size() > 0);
            }

            @Override
            public void onError(int i, String s) {
                mLoginDone = true;
                mLoginSucceed = false;
            }
        });
    }

    private void waitLoginAndPullDone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(mLoginDone && mPullDone)) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (mLoginSucceed) {

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
                while (!mPullDone) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!mPullSucceed) Utils.showToast(WelcomeActivity.this, "pull project failed");

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
