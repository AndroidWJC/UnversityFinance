package com.hqj.universityfinance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hqj.universityfinance.customview.ClearEditText;
import com.hqj.universityfinance.javabean.StudentInfo;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.HttpCallbackListener;
import com.hqj.universityfinance.utils.HttpConnectUtils;
import com.hqj.universityfinance.utils.SaveDataService;
import com.hqj.universityfinance.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wang on 17-9-20.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private ClearEditText mAccountEt;
    private ClearEditText mPasswordEt;
    private TextView mForgetPasswordTv;
    private Button mLoginBtn;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getWritableDatabase();
    }

    private void initView() {
        mBackBtn.setVisibility(View.GONE);
        mAccountEt = (ClearEditText) findViewById(R.id.login_account);
        mPasswordEt = (ClearEditText) findViewById(R.id.login_password);
        mForgetPasswordTv = (TextView) findViewById(R.id.forget_password);
        mForgetPasswordTv.setOnClickListener(this);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);

        mActionBarTitle.setText(R.string.title_action_bar_login);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                Utils.showLoadingDialog(this, 0, R.string.loading_text_login);
                confirmAccount();
                break;

            case R.id.forget_password:
                break;
        }
    }

    private void confirmAccount() {
        int account;
        String password;
        try {
            account = Integer.parseInt(mAccountEt.getText().toString());
            password = mPasswordEt.getText().toString();
        } catch (Exception e) {
            Utils.showToast(LoginActivity.this, R.string.toast_login_account_error);
            Utils.dismissLoadingDialog();
            return;
        }

        BmobQuery<StudentInfo> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("id", account);

        BmobQuery<StudentInfo> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("password", password);
        Log.d(TAG, "wjc: confirmAccount: " + account + " " + password);

        List<BmobQuery<StudentInfo>> andQuery = new ArrayList<>();
        andQuery.add(query1);
        andQuery.add(query2);

        BmobQuery<StudentInfo> query = new BmobQuery<>();
        query.and(andQuery);
        query.findObjects(this, new FindListener<StudentInfo>() {
            @Override
            public void onSuccess(List<StudentInfo> list) {
                Log.d(TAG, "wjc: onSuccess: 3 size = "+list.size());
                Utils.showToast(LoginActivity.this, "login succeed");
                StudentInfo info = list.get(0);
                if (info != null) {
                    Log.d(TAG, "wjc: onSuccess: save begin");
                    Utils.writeToSharedPreferences(LoginActivity.this, "account", mAccountEt.getText().toString());
                    Utils.writeToSharedPreferences(LoginActivity.this, "password", mPasswordEt.getText().toString());
                    ConfigUtils.setCurrentUserId(mAccountEt.getText().toString());
                    saveDataToDatabase(info);

                    succeedToLogin();
                    Log.d(TAG, "wjc: onSuccess: save end");
                }
                Utils.dismissLoadingDialog();
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "wjc: onError: "+i+" "+s);
                Utils.showToast(LoginActivity.this, R.string.toast_login_account_error);
                Utils.dismissLoadingDialog();
            }
        });
    }

    private void saveDataToDatabase(StudentInfo info) {
        Intent intent = new Intent(this, SaveDataService.class);
        intent.putExtra("data", info);
        intent.putExtra("type", 1);
        startService(intent);
    }

    private void succeedToLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });

    }
}
