package com.hqj.universityfinance.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.hqj.universityfinance.utils.HttpCallbackListener;
import com.hqj.universityfinance.utils.HttpConnectUtils;
import com.hqj.universityfinance.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by wang on 17-10-17.
 */

public class ApplyTableActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private ListView mListView;
    private String[] mTitles;
    private String[] mContents;
    private String[] mKeys;

    private ImageView mPhotoView;
    private TextView mNameTv;
    private TextView mIdTv;
    private TextView mCollegeTv;
    private TextView mClassTv;
    private String mCurrentUserName;
    private String mCurrentUserId;
    private String mCurrentUserCollege;
    private String mCurrentUserClass;
    private Bitmap mPhotoBitmap;
    private String mCurrentProjectId;
    private String mCurrentProjectName;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    private final static String KEY_S_ID = "s_id";
    private final static String KEY_Z_ID = "z_id";
    private final static String KEY_A_STATUS = "a_status";
    private final static String KEY_A_SCORE = "a_score";
    private final static String KEY_A_HONOR = "a_honor";
    private final static String KEY_A_PRIZE = "a_prize";
    private final static String KEY_A_REASON = "apply_reason";
    private final static String KEY_A_TIME = "a_time";
    private final static String KEY_VERIFY_T_ID = "verify_t_id";
    private final static String KEY_VERIFY_RESULT = "verify_result";

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
        mCurrentProjectId = getIntent().getStringExtra("projectId");
        mCurrentProjectName = getIntent().getStringExtra("projectName");
        if (mCurrentProjectId.startsWith("jxj0")
                || mCurrentProjectId.startsWith("jxj1")
                || mCurrentProjectId.startsWith("jxj2")
                || mCurrentProjectId.startsWith("jxj3")) {
            mTitles = getResources().getStringArray(R.array.apply_table_scholarship);
        } else if (mCurrentProjectId.startsWith("jxj4")) {
            mTitles = getResources().getStringArray(R.array.apply_table_job);
        } else if (mCurrentProjectId.startsWith("jxj5")) {
            mTitles = getResources().getStringArray(R.array.apply_table_loan);
        } else if (mCurrentProjectId.startsWith("jxj6")) {
            mTitles = getResources().getStringArray(R.array.apply_table_assistance);
        } else if (mCurrentProjectId.startsWith("jxj7")) {
            mTitles = getResources().getStringArray(R.array.apply_table_assistance);
        } else {
            mTitles = getResources().getStringArray(R.array.apply_table_scholarship);
        }

        mContents = new String[mTitles.length];
        mCurrentUserId = Utils.getStringFromSharedPreferences(this, "account");
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
        mActionBarRightBtn.setText(R.string.btn_submit);
        mActionBarRightBtn.setVisibility(View.VISIBLE);
        mActionBarRightBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listview_apply_table);
        ApplyTableAdapter adapter = new ApplyTableAdapter(this, mTitles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_btn:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(R.string.dialog_confirm);
                dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.showLoadingDialog(ApplyTableActivity.this);
                        submitApplyTable();
                    }
                });
                dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

    private void submitApplyTable() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeHms = format.format(date);

        RequestBody requestBody = null;
        if (mCurrentProjectId.startsWith("jxj0")
                || mCurrentProjectId.startsWith("jxj1")
                || mCurrentProjectId.startsWith("jxj2")
                || mCurrentProjectId.startsWith("jxj3")) {
            requestBody = new FormBody.Builder()
                    .add("type", "1")
                    .add(KEY_S_ID, mCurrentUserId)
                    .add(KEY_Z_ID, mCurrentProjectId)
                    .add(KEY_A_STATUS, "0")
                    .add(KEY_A_SCORE, mContents[0])
                    .add(KEY_A_HONOR, mContents[1])
                    .add(KEY_A_PRIZE, mContents[2])
                    .add(KEY_A_REASON, mContents[3])
                    .add(KEY_A_TIME, timeHms)
                    .add(KEY_VERIFY_T_ID, "123456789")
                    .add(KEY_VERIFY_RESULT, "0")
                    .build();

        } else if (mCurrentProjectId.startsWith("jxj4")) {
            mKeys = getResources().getStringArray(R.array.apply_table_scholarship_key);
        } else if (mCurrentProjectId.startsWith("jxj5")) {
            mKeys = getResources().getStringArray(R.array.apply_table_scholarship_key);
        } else if (mCurrentProjectId.startsWith("jxj6")) {
            mKeys = getResources().getStringArray(R.array.apply_table_scholarship_key);
        } else if (mCurrentProjectId.startsWith("jxj7")) {
            mKeys = getResources().getStringArray(R.array.apply_table_scholarship_key);
        } else {
            mKeys = getResources().getStringArray(R.array.apply_table_scholarship_key);
        }

        HttpConnectUtils.postRequestByOKHttp(ConfigUtils.SERVER_URL, requestBody, new HttpCallbackListener() {
            @Override
            public void onLoadSuccess(String response) {
                if (response == null) {
                    Utils.showToast(ApplyTableActivity.this, R.string.login_failed_net_error);
                } else if (response.equals(ConfigUtils.ERROR)) {
                    Utils.showToast(ApplyTableActivity.this, R.string.login_failed_net_error);
                } else if (response.startsWith(ConfigUtils.SUCCESS)) {
                    Utils.showToast(ApplyTableActivity.this, R.string.title_apply_table_succeed);
                }
                Utils.dismissLoadingDialog();
            }

            @Override
            public void onLoadFailed(int reason) {
                Utils.showToast(ApplyTableActivity.this, R.string.login_failed_net_error);
                Utils.dismissLoadingDialog();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialog_warning);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
