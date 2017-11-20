package com.hqj.universityfinance.utils;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import com.hqj.universityfinance.javabean.StudentInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wang on 17-11-20.
 */

public class SaveDataService extends IntentService {
    private final static String TAG = "SaveDataService";

    private final static int INVALID_VALUE = -1;
    private int mType;


    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    public SaveDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getWritableDatabase();
        mType = intent.getIntExtra("type", INVALID_VALUE);
        switch (mType) {
            case 1:
                StudentInfo info = (StudentInfo) intent.getSerializableExtra("data");
                savedToStudentDB(info);
                break;
        }
    }

    private void savedToStudentDB(StudentInfo info) {
        ContentValues values = new ContentValues();
        values.put("s_id", info.getId());
        values.put("s_password", info.getPassword());
        values.put("s_status", info.getPoor_status());
        values.put("s_id_card", info.getId_card());
        values.put("s_name", info.getName());
        values.put("s_sex", info.getSex());
        values.put("s_political_status", info.getPolitical_status());
        values.put("s_college", info.getCollege());
        values.put("s_start_year", info.getStart_year());
        values.put("s_continue_years", info.getContinue_year());
        values.put("s_class", info.getS_class());
        values.put("s_phone", info.getTelphone());
        values.put("s_photo", info.getPhoto());
        values.put("s_photo_bytes", getPhotoBytes(info.getPhoto()));
        mDB.insert(ConfigUtils.TABLE_STUDENT, null, values);
    }

    private byte[] getPhotoBytes(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();

            byte[] buffer = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outStream.toByteArray();
    }
}
