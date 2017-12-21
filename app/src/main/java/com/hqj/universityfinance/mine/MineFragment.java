package com.hqj.universityfinance.mine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.Utils;

/**
 * Created by wang on 17-9-12.
 */

public class MineFragment extends Fragment implements View.OnClickListener{

    private TextView mMyApplyTV;
    private TextView mMessageTV;
    private TextView mHelpTV;
    private TextView mSettingTV;
    private TextView mUserNameTV;
    private TextView mUserIdTV;
    private ImageView mUserPhotoIv;

    private LinearLayout mStudentInfo;

    private Context mContext;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initMemberVariable();
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        initView(view);

        return view;
    }

    private void initMemberVariable() {
        mContext = getActivity();
        mdbHelper = new DatabaseUtils(mContext, ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getReadableDatabase();
    }

    private void initView(View parent) {
        mMyApplyTV = (TextView) parent.findViewById(R.id.my_apply_tv);
        mMyApplyTV.setOnClickListener(this);
        mMessageTV = (TextView) parent.findViewById(R.id.message_tv);
        mMessageTV.setOnClickListener(this);
        mHelpTV = (TextView) parent.findViewById(R.id.help_tv);
        mHelpTV.setOnClickListener(this);
        mSettingTV = (TextView) parent.findViewById(R.id.setting_tv);
        mSettingTV.setOnClickListener(this);
        mStudentInfo = (LinearLayout) parent.findViewById(R.id.student_info);
        mStudentInfo.setOnClickListener(this);
        mUserNameTV = (TextView) parent.findViewById(R.id.user_name);
        mUserIdTV = (TextView) parent.findViewById(R.id.user_id);
        mUserPhotoIv = (ImageView) parent.findViewById(R.id.user_photo);

        String userId = Utils.getStringFromSharedPreferences(mContext, "account");
        Cursor cursor = mDB.rawQuery("select s_name,s_photo,s_photo_bytes from student_info where s_id=?",
                new String[]{userId});
        if (cursor.moveToFirst()) {
            mUserIdTV.setText(userId);
            mUserNameTV.setText(cursor.getString(cursor.getColumnIndex("s_name")));
            byte[] bytes;
            if ((bytes = cursor.getBlob(cursor.getColumnIndex("s_photo_bytes"))) != null) {
                Log.d("wangjuncheng", "initView: set photo by DB");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mUserPhotoIv.setImageBitmap(bitmap);
            } else {
                Glide.with(mContext).load(cursor.getString(cursor.getColumnIndex("s_photo"))).into(mUserPhotoIv);
            }
        }

        cursor.close();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.student_info:
                intent = new Intent(mContext, MyInfoActivity.class);
                mContext.startActivity(intent);
                break;

            case R.id.my_apply_tv:
                intent = new Intent(mContext, MyApplyActivity.class);
                mContext.startActivity(intent);
                break;

            case R.id.message_tv:
                intent = new Intent(mContext, MyMessageActivity.class);
                startActivity(intent);
                break;

            case R.id.help_tv:
                break;

            case R.id.setting_tv:
                intent = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intent);
                break;
        }

    }
}
