package com.hqj.universityfinance.mine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.home.ApplyTableAdapter;
import com.hqj.universityfinance.javabean.ApplyTableInfo;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 17-10-26.
 */

public class ApplyPreviewActivity extends BaseActivity {

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    private ApplyTableInfo mCurrentTable;
    private ListView mListView;
    private String[] mTitles;
    private String[] mContents;
    private String[] mKeys;

    private ImageView mPhotoView;
    private TextView mNameTv;
    private TextView mIdTv;
    private TextView mCollegeTv;
    private TextView mClassTv;
    private TextView mStatusTv;
    private String mCurrentUserName;
    private String mCurrentUserId;
    private String mCurrentUserCollege;
    private String mCurrentUserClass;
    private Bitmap mPhotoBitmap;
    private String mCurrentProjectId;
    private String mCurrentProjectName;
    private String mSelectedItem;
    private Map<String, String> mProjectMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_table);

        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getReadableDatabase();

        initData();
        initView();
    }

    private void initData() {
        List<String> contentList = new ArrayList<>();
        mCurrentTable = (ApplyTableInfo) getIntent().getSerializableExtra("applyInfo");
        mProjectMap = ConfigUtils.getProjectMap();
        mCurrentProjectId = mCurrentTable.getZ_id();
        mCurrentProjectName = mProjectMap.get(mCurrentProjectId);
        if (mCurrentProjectId.startsWith("jxj0")
                || mCurrentProjectId.startsWith("jxj1")
                || mCurrentProjectId.startsWith("jxj2")
                || mCurrentProjectId.startsWith("jxj3")) {
            mTitles = getResources().getStringArray(R.array.apply_table_scholarship_with_time);
        } else if (mCurrentProjectId.startsWith("jxj4")) {
            mTitles = getResources().getStringArray(R.array.apply_table_job_with_time);
        } else if (mCurrentProjectId.startsWith("jxj5")) {
            mTitles = getResources().getStringArray(R.array.apply_table_loan_with_time);
        } else if (mCurrentProjectId.startsWith("jxj6")) {
            mTitles = getResources().getStringArray(R.array.apply_table_assistance_with_time);
        } else if (mCurrentProjectId.startsWith("jxj7")) {
            mTitles = getResources().getStringArray(R.array.apply_table_assistance_with_time);
        } else {
            mTitles = getResources().getStringArray(R.array.apply_table_scholarship_with_time);
        }

        if (mCurrentProjectId.startsWith("jxj0")
                || mCurrentProjectId.startsWith("jxj1")
                || mCurrentProjectId.startsWith("jxj2")
                || mCurrentProjectId.startsWith("jxj3")
                || mCurrentProjectId.startsWith("jxj6")
                || mCurrentProjectId.startsWith("jxj7")) {
            contentList.add(String.valueOf(mCurrentTable.getA_score()));
            contentList.add(mCurrentTable.getA_job());
            contentList.add(mCurrentTable.getA_honor());
            contentList.add(mCurrentTable.getA_prize());
            contentList.add(mCurrentTable.getApply_reason());
            contentList.add(mCurrentTable.getCreatedAt());
        } else if (mCurrentProjectId.startsWith("jxj4")) {
            contentList.add(String.valueOf(mCurrentTable.getA_score()));
            contentList.add(mCurrentTable.getA_job());
            contentList.add(mCurrentTable.getApply_reason());
            contentList.add(mCurrentTable.getCreatedAt());
        } else if (mCurrentProjectId.startsWith("jxj5")) {
            contentList.add(mCurrentTable.getA_loan_sum());
            contentList.add(mCurrentTable.getApply_reason());
            contentList.add(mCurrentTable.getCreatedAt());
        }
        mContents = contentList.toArray(new String[contentList.size()]);

        mCurrentUserId = String.valueOf(mCurrentTable.getS_id());
        Cursor cursor = mDB.rawQuery("select s_name,s_college,s_class,s_photo_bytes from " + ConfigUtils.TABLE_STUDENT +
                " where s_id=?", new String[]{mCurrentUserId});
        if (cursor.moveToFirst()) {
            mCurrentUserName = cursor.getString(cursor.getColumnIndex("s_name"));
            mCurrentUserCollege = cursor.getString(cursor.getColumnIndex("s_college"));
            mCurrentUserClass = cursor.getString(cursor.getColumnIndex("s_class"));
            byte[] data = cursor.getBlob(cursor.getColumnIndex("s_photo_bytes"));
            mPhotoBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        cursor.close();
    }

    private void initView() {
        mActionBarTitle.setText(mCurrentProjectName);
        mActionBarTitle.append(getResources().getString(R.string.title_apply_table));

        mListView = (ListView) findViewById(R.id.listview_apply_table);
        ApplyTableAdapter adapter = new ApplyTableAdapter(this, mTitles, mContents);
        mListView.setAdapter(adapter);

        mNameTv = (TextView) findViewById(R.id.user_name);
        mNameTv.setText(mCurrentUserName);
        mIdTv = (TextView) findViewById(R.id.user_id);
        mIdTv.setText(mCurrentUserId);
        mCollegeTv = (TextView) findViewById(R.id.user_college);
        mCollegeTv.setText(mCurrentUserCollege);
        mClassTv = (TextView) findViewById(R.id.user_class);
        mClassTv.setText(mCurrentUserClass);
        mPhotoView = (ImageView) findViewById(R.id.user_photo);
        mPhotoView.setImageBitmap(mPhotoBitmap);
        mStatusTv = (TextView) findViewById(R.id.status);
        mStatusTv.setVisibility(View.VISIBLE);
        if (mCurrentTable.getVerify_result() == ConfigUtils.STATUS_NOT_VERIFY) {
            mStatusTv.setText(R.string.status_not_verify);
        } else if (mCurrentTable.getVerify_result() == ConfigUtils.STATUS_PASS) {
            mStatusTv.setText(R.string.status_pass);
        } else if (mCurrentTable.getVerify_result() == ConfigUtils.STATUS_REFUSE) {
            mStatusTv.setText(R.string.status_refuse);
        }
    }
}
