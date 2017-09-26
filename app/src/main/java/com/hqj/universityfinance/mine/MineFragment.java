package com.hqj.universityfinance.mine;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-9-12.
 */

public class MineFragment extends Fragment implements View.OnClickListener{

    TextView mMyApplyTV;
    TextView mMessageTV;
    TextView mHelpTV;
    TextView mSettingTV;
    LinearLayout mStudentInfo;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        initView(view);

        return view;
    }

    private void initView(View parent) {
        mMyApplyTV = (TextView) parent.findViewById(R.id.my_apply_tv);
        mMyApplyTV.setOnClickListener(this);
        mMessageTV = (TextView) parent.findViewById(R.id.message_tv);
        mMessageTV.setOnClickListener(this);
        mHelpTV = (TextView) parent.findViewById(R.id.help_tv);
        mHelpTV.setOnClickListener(this);
        mSettingTV = (TextView) parent.findViewById(R.id.setting_tv);
        mSettingTV.setOnClickListener(this);
        mStudentInfo = (LinearLayout) parent.findViewById(R.id.student_info);
        mStudentInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.student_info:
                break;

            case R.id.my_apply_tv:
                break;

            case R.id.message_tv:
                break;

            case R.id.help_tv:
                break;

            case R.id.setting_tv:
                intent = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intent);
                break;
        }

    }
}
