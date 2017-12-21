package com.hqj.universityfinance;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wang on 17-9-20.
 */

public class LoginActivity extends BaseActivity{

    private static final String TAG = "LoginActivity";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    private List<String> mTabNames;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();
        initView();

        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getWritableDatabase();
    }

    private void initData() {
        String[] names = getResources().getStringArray(R.array.login_tab_names);
        mTabNames = Arrays.asList(names);

        mFragments = new ArrayList<>();
        mFragments.add(LoginFragment.newInstance(LoginFragment.MODE_STUDENT));
        mFragments.add(LoginFragment.newInstance(LoginFragment.MODE_TEACHER));
    }

    private void initView() {
        mBackBtn.setVisibility(View.GONE);
        mActionBarTitle.setText(R.string.title_action_bar_login);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabPageAdapter adapter =
                new TabPageAdapter(getSupportFragmentManager(), mFragments, mTabNames);
        mViewPager.setAdapter(adapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
