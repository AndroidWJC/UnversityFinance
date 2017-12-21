package com.hqj.universityfinance.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.customview.WheelView;
import com.hqj.universityfinance.javabean.ApplyTableInfo;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.HttpCallbackListener;
import com.hqj.universityfinance.utils.HttpConnectUtils;
import com.hqj.universityfinance.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.listener.SaveListener;
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
    private String mSelectedItem;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    private final static String KEY_S_ID = "s_id";
    private final static String KEY_Z_ID = "z_id";
    private final static String KEY_A_STATUS = "a_status";
    private final static String KEY_A_SCORE = "a_score";
    private final static String KEY_A_JOB = "a_job";
    private final static String KEY_A_HONOR = "a_honor";
    private final static String KEY_A_PRIZE = "a_prize";
    private final static String KEY_A_REASON = "apply_reason";
    private final static String KEY_A_TIME = "a_time";
    private final static String KEY_VERIFY_T_ID = "verify_t_id";
    private final static String KEY_VERIFY_RESULT = "verify_result";
    private final static String KEY_A_SITUATION = "a_situation";
    private final static String KEY_A_LOAN_SUM = "a_loan_sum";
    private final static String KEY_A_SKILL = "a_skill";

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
        if (position == 0) {
            if (needScoreInput()) {
                String[] score = getResources().getStringArray(R.array.score_list);
                createWheelView(score);
            } else if (needLoanInput()) {
                String[] loanSum = getResources().getStringArray(R.array.loan_num_list);
                createWheelView(loanSum);
            } else {
                goToEditActivity(position);
            }
        } else {
            goToEditActivity(position);
        }
    }

    private void goToEditActivity(int position) {
        Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("title", mTitles[position]);
        intent.putExtra("content", mContents[position]);
        startActivityForResult(intent, position);
    }

    private void createWheelView(String[] dataList) {
        View customView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        WheelView wheelView = (WheelView) customView.findViewById(R.id.wheel_view);
        wheelView.setOffset(2);
        wheelView.setItems(Arrays.asList(dataList));
        wheelView.setSeletion(3);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mSelectedItem = item;
            }
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("选择");
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mContents[0] = mSelectedItem;
                View itemView = mListView.getChildAt(0);
                TextView contentView = (TextView) itemView.findViewById(R.id.content);
                contentView.setText(mSelectedItem);
            }
        });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setView(customView).show();
    }

    private boolean needScoreInput() {
        return (mCurrentProjectId.startsWith("jxj0")
                || mCurrentProjectId.startsWith("jxj1")
                || mCurrentProjectId.startsWith("jxj2")
                || mCurrentProjectId.startsWith("jxj3")
                || mCurrentProjectId.startsWith("jxj4")
                || mCurrentProjectId.startsWith("jxj6")
                || mCurrentProjectId.startsWith("jxj7")
        );
    }

    private boolean needLoanInput() {
        return mCurrentProjectId.startsWith("jxj5");
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
                        submitApplyTableByBmob();
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

    private void submitApplyTableByBmob() {
        if (checkSomeIsNull()){
            Utils.dismissLoadingDialog();
            return;
        }

        ApplyTableInfo applyTable = new ApplyTableInfo();
        applyTable.setS_id(Integer.valueOf(mCurrentUserId));
        applyTable.setZ_id(mCurrentProjectId);
        applyTable.setA_status(0);
        applyTable.setVerify_t_id(129061289);
        applyTable.setVerify_result(0);

        if (mCurrentProjectId.startsWith("jxj0")
                || mCurrentProjectId.startsWith("jxj1")
                || mCurrentProjectId.startsWith("jxj2")
                || mCurrentProjectId.startsWith("jxj3")
                || mCurrentProjectId.startsWith("jxj6")
                || mCurrentProjectId.startsWith("jxj7")) {
            applyTable.setA_score(Float.valueOf(mContents[0]));
            applyTable.setA_job(mContents[1]);
            applyTable.setA_honor(mContents[2]);
            applyTable.setA_prize(mContents[3]);
            applyTable.setApply_reason(mContents[4]);
        } else if (mCurrentProjectId.startsWith("jxj4")) {
            applyTable.setA_score(Float.valueOf(mContents[0]));
            applyTable.setA_job(mContents[1]);
            applyTable.setApply_reason(mContents[2]);
        } else if (mCurrentProjectId.startsWith("jxj5")) {
            applyTable.setA_loan_sum(mContents[0]);
            applyTable.setApply_reason(mContents[1]);
        } else {
            Utils.showToast(ApplyTableActivity.this, R.string.toast_unknown_error);
            Utils.dismissLoadingDialog();
            return;
        }

        applyTable.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Utils.dismissLoadingDialog(true, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                    }
                });
                Utils.showToast(ApplyTableActivity.this, R.string.title_apply_table_succeed);
            }

            @Override
            public void onFailure(int i, String s) {
                Utils.showToast(ApplyTableActivity.this, R.string.login_failed_net_error);
                Utils.dismissLoadingDialog(false, null);
            }
        });
    }

    private void submitApplyTable() {

        if (checkSomeIsNull()){
            Utils.dismissLoadingDialog();
            return;
        }

        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeHms = format.format(date);

        FormBody.Builder builder = new FormBody.Builder();

        builder.add("type", "1")
                .add(KEY_S_ID, mCurrentUserId)
                .add(KEY_Z_ID, mCurrentProjectId)
                .add(KEY_A_STATUS, "0")
                .add(KEY_A_TIME, timeHms)
                .add(KEY_VERIFY_T_ID, "123456789")
                .add(KEY_VERIFY_RESULT, "0");

        if (mCurrentProjectId.startsWith("jxj0")
                || mCurrentProjectId.startsWith("jxj1")
                || mCurrentProjectId.startsWith("jxj2")
                || mCurrentProjectId.startsWith("jxj3")) {

            try {
                builder.add(KEY_A_SCORE, mContents[0])
                        .addEncoded(KEY_A_JOB, URLEncoder.encode(mContents[1], "UTF-8"))
                        .addEncoded(KEY_A_HONOR, URLEncoder.encode(mContents[2], "UTF-8"))
                        .addEncoded(KEY_A_PRIZE, URLEncoder.encode(mContents[3], "UTF-8"))
                        .addEncoded(KEY_A_REASON, URLEncoder.encode(mContents[4], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else if (mCurrentProjectId.startsWith("jxj4")) {
            try {
                builder.add(KEY_A_SCORE, URLEncoder.encode(mContents[0], "UTF-8"))
                        .addEncoded(KEY_A_JOB, URLEncoder.encode(mContents[1], "UTF-8"))
                        .addEncoded(KEY_A_REASON, URLEncoder.encode(mContents[2], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else if (mCurrentProjectId.startsWith("jxj5")) {

            try {
                builder.addEncoded(KEY_A_LOAN_SUM, URLEncoder.encode(mContents[0], "UTF-8"))
                        .addEncoded(KEY_A_REASON, URLEncoder.encode(mContents[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else if (mCurrentProjectId.startsWith("jxj6")) {
            try {
                builder.add(KEY_A_SCORE, URLEncoder.encode(mContents[0], "UTF-8"))
                        .addEncoded(KEY_A_JOB, URLEncoder.encode(mContents[1], "UTF-8"))
                        .addEncoded(KEY_A_HONOR, URLEncoder.encode(mContents[2], "UTF-8"))
                        .addEncoded(KEY_A_PRIZE, URLEncoder.encode(mContents[3], "UTF-8"))
                        .addEncoded(KEY_A_REASON, URLEncoder.encode(mContents[4], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (mCurrentProjectId.startsWith("jxj7")) {
            try {
                builder.add(KEY_A_SCORE, URLEncoder.encode(mContents[0], "UTF-8"))
                        .addEncoded(KEY_A_JOB, URLEncoder.encode(mContents[1], "UTF-8"))
                        .addEncoded(KEY_A_HONOR, URLEncoder.encode(mContents[2], "UTF-8"))
                        .addEncoded(KEY_A_PRIZE, URLEncoder.encode(mContents[3], "UTF-8"))
                        .addEncoded(KEY_A_REASON, URLEncoder.encode(mContents[4], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Utils.showToast(ApplyTableActivity.this, R.string.toast_unknown_error);
            Utils.dismissLoadingDialog();
            return;
        }

        HttpConnectUtils.postRequestByOKHttp(ConfigUtils.SERVER_URL, builder, new HttpCallbackListener() {
            @Override
            public void onLoadSuccess(String response) {
                if (response == null) {
                    Utils.showToast(ApplyTableActivity.this, R.string.login_failed_net_error);
                } else if (response.equals(ConfigUtils.ERROR)) {
                    Utils.showToast(ApplyTableActivity.this, R.string.login_failed_net_error);
                } else if (response.startsWith(ConfigUtils.SUCCESS)) {
                    Utils.showToast(ApplyTableActivity.this, R.string.title_apply_table_succeed);
                    finish();
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

    private boolean checkSomeIsNull() {
        for (int i=0;i<mContents.length;i++) {
            if (mContents[i] == null || mContents[i].trim().equals("")) {
                String warringText = getResources().getString(R.string.warring_can_not_null);
                Utils.showToast(this, mTitles[i] + warringText);
                return true;
            }
        }
        return false;
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
