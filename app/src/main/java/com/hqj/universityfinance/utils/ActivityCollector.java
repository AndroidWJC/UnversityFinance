package com.hqj.universityfinance.utils;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 17-10-13.
 */

public class ActivityCollector {

    public static List<AppCompatActivity> mActivities = new ArrayList<>();

    public static void addActivity(AppCompatActivity activity) {
        mActivities.add(activity);
    }

    public static void removeActivity(AppCompatActivity activity) {
        mActivities.remove(activity);
    }

    public static void  finishAllActivities() {
        for (AppCompatActivity activity : mActivities) {
            activity.finish();
        }
    }
}
