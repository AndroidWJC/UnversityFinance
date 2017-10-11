package com.hqj.universityfinance;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqj.universityfinance.explore.ExploreFragment;
import com.hqj.universityfinance.home.HomeFragment;
import com.hqj.universityfinance.mine.MineFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private static final String HOME_PAGE_TAG = "homeFragment";
    private static final String EXPLORE_PAGE_TAG = "exploreFragment";
    private static final String MINE_PAGE_TAG = "mineFragment";

    private RadioButton mHomeBtn;
    private RadioButton mExploreBtn;
    private RadioButton mMineBtn;
    private RadioGroup mRadioGroup;

    private FragmentManager mFragmentManager;

    private HomeFragment mHomeFragment;
    private ExploreFragment mExploreFragment;
    private MineFragment mMineFragment;

    private String mCurrentPageTag = "";

    private int textColor;
    private int focusTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        textColor = getResources().getColor(R.color.color_text);
        focusTextColor = getResources().getColor(R.color.focus_color_text);
        mFragmentManager = getFragmentManager();

        showFragment(HOME_PAGE_TAG);
    }

    private void initView() {
        mHomeBtn = (RadioButton) findViewById(R.id.home_btn);
        mExploreBtn = (RadioButton) findViewById(R.id.explore_btn);
        mMineBtn = (RadioButton) findViewById(R.id.mine_btn);
        mHomeBtn.setOnClickListener(this);
        mExploreBtn.setOnClickListener(this);
        mMineBtn.setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        mRadioGroup.check(R.id.home_btn);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.home_btn:
                showFragment(HOME_PAGE_TAG);
                break;

            case R.id.explore_btn:
                showFragment(EXPLORE_PAGE_TAG);
                break;

            case R.id.mine_btn:
                showFragment(MINE_PAGE_TAG);
                break;
        }
    }

    private void showFragment(String tag) {
        if (mCurrentPageTag.equals(tag)) {
            Log.d(TAG, "showFragment: return");
            return;
        }

        FragmentTransaction beginTransaction= mFragmentManager.beginTransaction();
        hidePreFragment(beginTransaction);

        switch (tag) {
            case HOME_PAGE_TAG:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    beginTransaction.add(R.id.fragment_area, mHomeFragment, tag);
                } else {
                    beginTransaction.show(mHomeFragment);
                }
                mActionBarTitle.setText(R.string.app_name);
                mHomeBtn.setTextColor(focusTextColor);
                break;

            case EXPLORE_PAGE_TAG:
                if (mExploreFragment == null) {
                    mExploreFragment = new ExploreFragment();
                    beginTransaction.add(R.id.fragment_area, mExploreFragment, tag);
                } else {
                    beginTransaction.show(mExploreFragment);
                }
                mActionBarTitle.setText(R.string.bottom_bar_explore);
                mExploreBtn.setTextColor(focusTextColor);
                break;

            case MINE_PAGE_TAG:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    beginTransaction.add(R.id.fragment_area, mMineFragment, tag);
                } else {
                    beginTransaction.show(mMineFragment);
                }
                mActionBarTitle.setText(R.string.bottom_bar_mine);
                mMineBtn.setTextColor(focusTextColor);
                break;
        }

        mCurrentPageTag = tag;
        beginTransaction.addToBackStack(tag);
        beginTransaction.commit();
    }

    private void hidePreFragment(FragmentTransaction transaction) {
        Log.d(TAG, "hidePreFragment: mCurrentPageTag = "+mCurrentPageTag);
        switch (mCurrentPageTag) {
            case HOME_PAGE_TAG:
                transaction.hide(mHomeFragment);
                mHomeBtn.setTextColor(textColor);
                break;

            case EXPLORE_PAGE_TAG:
                transaction.hide(mExploreFragment);
                mExploreBtn.setTextColor(textColor);
                break;

            case MINE_PAGE_TAG:
                transaction.hide(mMineFragment);
                mMineBtn.setTextColor(textColor);
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (mCurrentPageTag.equals(HOME_PAGE_TAG)) {
//            finish();
//        }
//        int backStackCount = mFragmentManager.getBackStackEntryCount();
//        if (backStackCount > 1) {
//            while (mFragmentManager.getBackStackEntryCount() > 1) {
//                mFragmentManager.popBackStackImmediate();
//                mRadioGroup.check(R.id.home_btn);
//                showFragment(HOME_PAGE_TAG);
//            }
//        } else {
//            finish();
//        }
//    }
}
