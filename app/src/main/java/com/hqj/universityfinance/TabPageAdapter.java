package com.hqj.universityfinance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wang on 17-12-18.
 */

public class TabPageAdapter extends FragmentStatePagerAdapter {

    List<Fragment> mFragments;
    List<String> mTabNames;

    public TabPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> names) {
        super(fm);
        mFragments = fragments;
        mTabNames = names;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames.get(position);
    }

    @Override
    public int getCount() {
        return mTabNames.size();
    }
}
