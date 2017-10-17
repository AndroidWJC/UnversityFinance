package com.hqj.universityfinance.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.Utils;

/**
 * Created by wang on 17-10-17.
 */

public class ApplyTableActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private String[] mTitles;
    private String[] mContents;

    private ImageView mPhotoView;
    private TextView mNameTv;
    private TextView mIdTv;
    private String mCurrentUserName;
    private String mCurrentUserId;
    private Bitmap mPhotoBitmap;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

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
        mActionBarTitle.setText(getIntent().getStringExtra("projectName"));
        mActionBarTitle.append(getResources().getString(R.string.title_apply_table));

        mTitles = getResources().getStringArray(R.array.info_apply_table);
        mContents = new String[mTitles.length];
        mCurrentUserId = Utils.getStringFromSharedPreferences(this, "account");
        Cursor cursor = mDB.rawQuery("select s_name,s_photo_bytes from " + ConfigUtils.TABLE_STUDENT +
                " where s_id=?", new String[]{mCurrentUserId});
        if (cursor.moveToFirst()) {
            mCurrentUserName = cursor.getString(cursor.getColumnIndex("s_name"));
            byte[] data = cursor.getBlob(cursor.getColumnIndex("s_photo_bytes"));
            mPhotoBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        cursor.close();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview_apply_table);
        ApplyTableAdapter adapter = new ApplyTableAdapter(this, mTitles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        mNameTv = (TextView) findViewById(R.id.user_name);
        mNameTv.setText(mCurrentUserName);
        mIdTv = (TextView) findViewById(R.id.user_id);
        mIdTv.setText(mCurrentUserId);
        mPhotoView = (ImageView) findViewById(R.id.user_photo);
        mPhotoView.setImageBitmap(mPhotoBitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String content = data.getStringExtra("content");
            mContents[requestCode] = content;
            View itemView = mListView.getChildAt(requestCode);
            TextView contentView = (TextView) itemView.findViewById(R.id.content);
            contentView.setText(content);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("title", mTitles[position]);
        intent.putExtra("content", mContents[position]);
        startActivityForResult(intent, position);
    }
}
