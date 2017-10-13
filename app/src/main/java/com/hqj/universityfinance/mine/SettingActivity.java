package com.hqj.universityfinance.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.LoginActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ActivityCollector;
import com.hqj.universityfinance.utils.Utils;


public class SettingActivity extends BaseActivity implements View.OnClickListener{

    Button mSignOutBtn;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_out_btn:
                Utils.writeToSharedPreferences(this, "account", "");
                Utils.writeToSharedPreferences(this, "password", "");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                ActivityCollector.finishAllActivities();
                break;
        }
    }
}
