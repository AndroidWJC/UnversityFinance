package com.hqj.universityfinance.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.ProjectBean;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 17-10-13.
 */

public class SimilarProjectNavigationActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "SimilarProject";
    private ListView mListView;
    private List<ProjectBean> mProjectList;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    private String mSimilarProjectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_pj_navigation);

        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getReadableDatabase();
        mProjectList = new ArrayList<>();

        initData();
        initView();
    }

    private void initData() {
        mSimilarProjectId = getIntent().getStringExtra("projectId");

        ProjectBean projectBean = null;
        Cursor cursor = mDB.rawQuery("select * from " + ConfigUtils.TABLE_PROJECT + " "
                + "where z_id like ?", new String[]{mSimilarProjectId+"%"});

        while (cursor.moveToNext()) {
            Log.d(TAG, "initData: count = "+cursor.getCount());
            projectBean = new ProjectBean();
            projectBean.setProjectId(cursor.getString(cursor.getColumnIndex("z_id")));
            projectBean.setProjectName(cursor.getString(cursor.getColumnIndex("z_name")));
            projectBean.setProjectStatus(cursor.getInt(cursor.getColumnIndex("z_status")));
            projectBean.setProjectSum(cursor.getString(cursor.getColumnIndex("z_sum")));
            projectBean.setProjectTime(cursor.getString(cursor.getColumnIndex("z_time")));
            projectBean.setProjectQuota(cursor.getString(cursor.getColumnIndex("z_quota")));
            projectBean.setProjectDescribe(cursor.getString(cursor.getColumnIndex("z_describe")));
            mProjectList.add(projectBean);
        }

        cursor.close();
    }

    private void initView() {
        mActionBarTitle.setText(R.string.title_project_list);

        mListView = (ListView) findViewById(R.id.listview_similar_pj_navigation);
        mListView.setOnItemClickListener(this);
        TitleOnlyItemAdapter adapter = new TitleOnlyItemAdapter(mProjectList, this);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, ProjectIntroduceActivity.class);
        intent.putExtra("projectId", mProjectList.get(position).getProjectId());
        startActivity(intent);
    }
}