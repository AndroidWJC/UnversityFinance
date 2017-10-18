package com.hqj.universityfinance.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hqj.universityfinance.BannerBean;
import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 17-10-12.
 */

public class ProjectIntroduceActivity extends BaseActivity implements View.OnClickListener{

    private ListView mListView;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    private List<Map<String, String>> mDataList;
    private boolean mEnabled = true;
    private String mProjectId;
    private String mProjectName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_introduce);

        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getReadableDatabase();
        mDataList = new ArrayList<>();

        initData();
        initView();
    }

    private void initData() {
        mProjectId = getIntent().getStringExtra("projectId");

        Cursor cursor = mDB.rawQuery("select * from " + ConfigUtils.TABLE_PROJECT + " "
                + "where z_id=?", new String[]{mProjectId});

        String[] titleArray = getResources().getStringArray(R.array.project_info_title);
        String[] contentKeyArray = getResources().getStringArray(R.array.project_info_content_key);

        Map<String, String> data = null;
        if (cursor.moveToFirst()) {
            mProjectName = cursor.getString(cursor.getColumnIndex("z_name"));
            for (int i = 0; i < titleArray.length; i++) {
                data = new HashMap<>();
                data.put("title", titleArray[i]);
                if (i == 0) {
                    data.put("content", Utils.getStringFromSharedPreferences(this, "school_year"));
                } else {
                    data.put("content", cursor.getString(cursor.getColumnIndex(contentKeyArray[i])));
                }
                mDataList.add(data);
            }
        }
        cursor.close();
    }

    private void initView() {
        mActionBarTitle.setText(mProjectName);
        mActionBarRightBtn.setText(R.string.btn_apply);
        mActionBarRightBtn.setVisibility(View.VISIBLE);

        if (mEnabled) {
            mActionBarRightBtn.setOnClickListener(this);
        } else {
            mActionBarRightBtn.setEnabled(false);
            mActionBarRightBtn.setTextColor(0xffffff);
        }

        mListView = (ListView) findViewById(R.id.listview_p_introduce);
        ProjectIntroduceAdapter adapter = new ProjectIntroduceAdapter(this, mDataList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_btn:
                Intent intent = new Intent(this, ApplyTableActivity.class);
                intent.putExtra("projectId", mProjectId);
                intent.putExtra("projectName", mProjectName);
                startActivity(intent);
                break;
        }
    }
}
