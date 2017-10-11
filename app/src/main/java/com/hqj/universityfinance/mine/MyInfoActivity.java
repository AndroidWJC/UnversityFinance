package com.hqj.universityfinance.mine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 17-9-18.
 */

public class MyInfoActivity extends BaseActivity {

    private ListView mListView;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        mdbHelper = new DatabaseUtils(this, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getReadableDatabase();

        initView();
    }

    private void initView() {
        mActionBarTitle.setText(R.string.title_my_info);

        mListView = (ListView) findViewById(R.id.listview_info);
        SimpleAdapter adapter = initAdapter();
        mListView.setAdapter(adapter);
    }


    private SimpleAdapter initAdapter() {
        SimpleAdapter adapter = null;

        List<Map<String, String>> dataList = getData();
        adapter = new SimpleAdapter(
                this,
                dataList,
                R.layout.my_info_item,
                new String[]{"title", "content"},
                new int[]{R.id.title_info, R.id.content_info}
        );

        return adapter;
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data;
        String userId = Utils.getStringFromSharedPreferences(this, "account");
        Cursor cursor = mDB.rawQuery("select * from " + ConfigUtils.TABLE_STUDENT + " "
                + "where s_id=?", new String[]{userId});

        if (cursor.moveToFirst()) {
            String[] info_title = getResources().getStringArray(R.array.info_item);
            String[] info_content = {
                    cursor.getString(cursor.getColumnIndex("s_name")),
                    cursor.getString(cursor.getColumnIndex("s_sex")),
                    userId,
                    cursor.getString(cursor.getColumnIndex("s_id_card")),
                    cursor.getString(cursor.getColumnIndex("s_status")).equals("1") ? "是" : "否",
                    cursor.getString(cursor.getColumnIndex("s_political_status")),
                    cursor.getString(cursor.getColumnIndex("s_college")),
                    cursor.getString(cursor.getColumnIndex("s_class")),
                    cursor.getString(cursor.getColumnIndex("s_start_year")),
                    cursor.getString(cursor.getColumnIndex("s_continue_years")),
                    cursor.getString(cursor.getColumnIndex("s_phone")),

            };

            for (int i=0; i<info_title.length;i++) {
                data = new HashMap<>();
                data.put("title", info_title[i]);
                data.put("content", info_content[i]);
                dataList.add(data);
            }

        }
        cursor.close();

        return dataList;
    }
}
