package com.hqj.universityfinance.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.LoginActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ActivityCollector;
import com.hqj.universityfinance.utils.Utils;


public class SettingActivity extends BaseActivity implements View.OnClickListener{

    Button mSignOutBtn;
    TextView mModifyPasswordTv;
    TextView mAboutTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        intView();
    }

    private void intView() {
        mActionBarTitle.setText(R.string.title_setting);

        mSignOutBtn = (Button) findViewById(R.id.sign_out_btn);
        mSignOutBtn.setOnClickListener(this);

        mModifyPasswordTv = (TextView) findViewById(R.id.modify_password_tv);
        mModifyPasswordTv.setOnClickListener(this);
        mAboutTv = (TextView) findViewById(R.id.app_tv);
        mAboutTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_out_btn:
                Utils.writeToSharedPreferences(this, "account", "");
                Utils.writeToSharedPreferences(this, "password", "");
                goToActivity(LoginActivity.class);
                ActivityCollector.finishAllActivities();
                break;

            case R.id.modify_password_tv:
                goToActivity(ModifyPasswordActivity.class);
                break;

            case R.id.app_tv:
                goToActivity(AboutActivity.class);
                break;
        }
    }

    private void goToActivity(Class<?> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }
}
