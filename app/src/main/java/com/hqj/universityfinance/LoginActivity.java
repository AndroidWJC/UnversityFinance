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
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.HttpCallbackListener;
import com.hqj.universityfinance.utils.HttpConnectUtils;
import com.hqj.universityfinance.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("type", ConfigUtils.TYPE_POST_LOGIN);
                params.put("account", mAccountEt.getText().toString());
                params.put("password", mPasswordEt.getText().toString());

                try {
                    String urlWithInfo = HttpConnectUtils.getURLWithParams(ConfigUtils.SERVER_URL, params);
                    HttpConnectUtils.sendRequestByOKHttp(urlWithInfo, new HttpCallbackListener() {
                        @Override
                        public void onLoadSuccess(final String response) {
                            Log.d(TAG, "onLoadSuccess: response = "+response);
                            if (response == null) {
                                Utils.showToast(LoginActivity.this, R.string.login_failed_net_error);
                            } else if (response.equals("wrong")) {
                                Utils.showToast(LoginActivity.this, R.string.toast_login_account_error);
                            } else if (response.startsWith(ConfigUtils.SUCCESSFUL)) {
                                Utils.writeToSharedPreferences(LoginActivity.this, "account", mAccountEt.getText().toString());
                                Utils.writeToSharedPreferences(LoginActivity.this, "password", mPasswordEt.getText().toString());
                                saveDataToDatabase(response);

                                succeedToLogin();
                            }
                            Utils.dismissLoadingDialog();
                            Log.d(TAG, "onLoadSuccess: end ");
                        }

                        @Override
                        public void onLoadFailed(int reason) {

                            Log.d(TAG, "onLoadSuccess: error");
                            if (reason == ConfigUtils.TYPE_LOGIN_NET_ERROR) {
                                Utils.showToast(LoginActivity.this, R.string.login_failed_net_error);
                            } else if (reason == ConfigUtils.TYPE_LOGIN_ACCOUNT_ERROR){
                                Utils.showToast(LoginActivity.this, R.string.toast_login_account_error);
                            }
                            Utils.dismissLoadingDialog();
                        }
                    });
                } catch (Exception e) {

                }

                break;

            case R.id.forget_password:

                break;
        }
    }

    private boolean saveDataToDatabase(String jsonData) {
        Cursor cursor = mDB.rawQuery("select s_password from "+ConfigUtils.TABLE_STUDENT+" "+"where s_id=?",
                new String[]{mAccountEt.getText().toString()});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            ContentValues values = new ContentValues();
            values.put("s_id", jsonObject.getInt("s_id"));
            values.put("s_password", jsonObject.getString("s_password"));
            values.put("s_status", jsonObject.getInt("s_status"));
            values.put("s_id_card", jsonObject.getString("s_id_card"));
            values.put("s_name", jsonObject.getString("s_name"));
            values.put("s_sex", jsonObject.getString("s_sex"));
            values.put("s_political_status", jsonObject.getString("s_political_status"));
            values.put("s_college", jsonObject.getString("s_college"));
            values.put("s_start_year", jsonObject.getInt("s_start_year"));
            values.put("s_continue_years", jsonObject.getInt("s_continue_years"));
            values.put("s_class", jsonObject.getString("s_class"));
            values.put("s_phone", jsonObject.getString("s_phone"));
            values.put("s_photo", jsonObject.getString("s_photo"));
            values.put("s_photo_bytes", getPhotoBytes(jsonObject.getString("s_photo")));
            mDB.insert(ConfigUtils.TABLE_STUDENT, null, values);
            Log.d(TAG, "onLoadSuccess: s_id = "+jsonObject.getInt("s_id")
                    +", s_password = "+jsonObject.getString("s_password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    private byte[] getPhotoBytes(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();

            byte[] buffer = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outStream.toByteArray();
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
