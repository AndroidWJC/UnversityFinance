package com.hqj.universityfinance.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wang on 17-9-27.
 */

public class DatabaseUtils extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseUtils";
    Context mContext;

    public DatabaseUtils(Context context, String name, int version) {
        super(context, name, null, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (ConfigUtils.DEBUG) Log.d(TAG, "onCreate: run");

        db.execSQL("create table if not exists "+ ConfigUtils.TABLE_PROJECT +
                "(z_id TEXT primary key, " +
                "z_name TEXT, " +
                "z_status INTEGER, " +
                "z_sum TEXT, " +
                "z_time TEXT, " +
                "z_quota TEXT, " +
                "z_describe TEXT)");

        db.execSQL("create table if not exists "+ ConfigUtils.TABLE_STUDENT +
                "(s_id INTEGER primary key, " +
                "s_password TEXT, " +
                "s_status INTEGER, " +
                "s_id_card TEXT, " +
                "s_name TEXT, " +
                "s_sex TEXT, " +
                "s_political_status TEXT, " +
                "s_college TEXT, " +
                "s_start_year INTEGER, " +
                "s_continue_years INTEGER, " +
                "s_class TEXT, " +
                "s_phone TEXT, " +
                "s_photo TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion < oldVersion) {
            return;
        }

        if (newVersion > 1) {
            db.execSQL("drop table if exists " + ConfigUtils.TABLE_STUDENT);
            db.execSQL("drop table if exists " + ConfigUtils.TABLE_PROJECT);
            onCreate(db);
        }

    }
}
