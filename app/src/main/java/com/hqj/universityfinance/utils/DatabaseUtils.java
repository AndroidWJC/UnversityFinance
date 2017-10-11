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

        db.execSQL("create table if not exists "+ ConfigUtils.TABLE_STUDENT +
                "(s_id integer primary key, " +
                "s_password text, " +
                "s_status integer, " +
                "s_id_card text, " +
                "s_name text, " +
                "s_sex text, " +
                "s_political_status text, " +
                "s_college text, " +
                "s_start_year integer, " +
                "s_continue_years integer, " +
                "s_class text, " +
                "s_phone text, " +
                "s_photo text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion < oldVersion) {
            return;
        }

        if (newVersion > 1) {
            db.execSQL("drop table if exists " + ConfigUtils.TABLE_STUDENT);
            onCreate(db);
        }

    }
}
