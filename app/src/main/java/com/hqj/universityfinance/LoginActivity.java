package com.hqj.universityfinance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hqj.universityfinance.customview.ClearEditText;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.HttpCallbackListener;
import com.hqj.universityfinance.utils.HttpConnectUtils;
import com.hqj.universityfinance.utils.Utils;

import java.util.HashMap;

/**
 * Created by wang on 17-9-20.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private ClearEditText mAccountEt;
    private ClearEditText mPasswordEt;
    private TextView mForgetPasswordTv;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
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
                params.put("account", mAccountEt.getText().toString());
                params.put("password", mPasswordEt.getText().toString());

                try {
                    String urlWithInfo = HttpConnectUtils.getURLWithParams(ConfigUtils.SERVER_URL, params);
                    Log.d(TAG, "onClick: urlWithInfo = "+urlWithInfo);
                    HttpConnectUtils.sendRequestByOKHttp(urlWithInfo, new HttpCallbackListener() {
                        @Override
                        public void onLoadSuccess(String response) {
                            Log.d(TAG, "onLoadSuccess: response = "+response);
                            if (response == null) {
                                Utils.showToast(LoginActivity.this, R.string.login_failed_net_error);
                            } else if (response.equals("wrong")) {
                                Utils.showToast(LoginActivity.this, R.string.toast_login_account_error);
                            } else if (response.equals("right")) {
                                Utils.writeToSharedPreferences(LoginActivity.this, "account", mAccountEt.getText().toString());
                                Utils.writeToSharedPreferences(LoginActivity.this, "password", mPasswordEt.getText().toString());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                                finish(); //该界面不加入回退栈中
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
}
