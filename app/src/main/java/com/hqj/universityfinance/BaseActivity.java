package com.hqj.universityfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hqj.universityfinance.utils.ActivityCollector;

/**
 * Created by wang on 17-9-18.
 */

public class BaseActivity extends AppCompatActivity {

    protected ActionBar mActionBar;
    protected TextView mActionBarTitle;
    protected TextView mActionBarRightBtn;
    protected ImageButton mBackBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setCustomView(R.layout.actionbar_custom_view);
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionBarTitle = (TextView) mActionBar.getCustomView().findViewById(R.id.action_bar_title);
            mActionBarRightBtn = (TextView) mActionBar.getCustomView().findViewById(R.id.right_btn);

            mBackBtn = (ImageButton) mActionBar.getCustomView().findViewById(R.id.back_btn);
            mBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
