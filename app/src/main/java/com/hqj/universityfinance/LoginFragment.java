package com.hqj.universityfinance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hqj.universityfinance.customview.PowerfulEditText;
import com.hqj.universityfinance.javabean.BmobBaseBean;
import com.hqj.universityfinance.javabean.StudentInfo;
import com.hqj.universityfinance.javabean.TeacherInfo;
import com.hqj.universityfinance.utils.ConfigUtils;
import com.hqj.universityfinance.utils.DatabaseUtils;
import com.hqj.universityfinance.utils.SaveDataService;
import com.hqj.universityfinance.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by wang on 17-12-18.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{

    public final static int MODE_STUDENT = 1;
    public final static int MODE_TEACHER = 2;
    private int mCurrentMode = -1;

    private Button mLoginBtn;
    private PowerfulEditText mAccountEt;
    private PowerfulEditText mPasswordEt;
    private TextView mForgetPasswordTv;

    private DatabaseUtils mdbHelper;
    private SQLiteDatabase mDB;

    private Context mContext;

    public static LoginFragment newInstance(int mode) {

        Bundle args = new Bundle();
        args.putInt("mode", mode);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCurrentMode = getArguments().getInt("mode");
        }
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        mdbHelper = new DatabaseUtils(getActivity(), ConfigUtils.DATABASE_NAME, ConfigUtils.DATABASE_VERSION);
        mDB = mdbHelper.getWritableDatabase();
    }

    private void initView(View parent) {
        mAccountEt = (PowerfulEditText) parent.findViewById(R.id.login_account);
        if (mCurrentMode == MODE_TEACHER) {
            mAccountEt.setHint(R.string.hint_account_teacher);
        }
        mPasswordEt = (PowerfulEditText) parent.findViewById(R.id.login_password);
        mLoginBtn = (Button) parent.findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);
        mForgetPasswordTv = (TextView) parent.findViewById(R.id.forget_password);
        mForgetPasswordTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                Utils.showLoadingDialog(mContext, 0, R.string.loading_text_login);
                if (mCurrentMode == MODE_STUDENT) {
                    confirmAccountForStudent();
                } else if (mCurrentMode == MODE_TEACHER) {
                    //confirmAccountForTeacher();
                    Utils.dismissLoadingDialog();
                }

                break;

            case R.id.forget_password:
                break;
        }
    }


    private void confirmAccountForStudent() {
        int account;
        String password;
        try {
            account = Integer.parseInt(mAccountEt.getText().toString());
            password = mPasswordEt.getText().toString();
        } catch (Exception e) {
            Utils.showToast(mContext, R.string.toast_login_account_error);
            Utils.dismissLoadingDialog();
            return;
        }

        BmobQuery<StudentInfo> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("id", account);

        BmobQuery<StudentInfo> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("password", password);

        List<BmobQuery<StudentInfo>> andQuery = new ArrayList<>();
        andQuery.add(query1);
        andQuery.add(query2);

        BmobQuery<StudentInfo> query = new BmobQuery<>();
        query.and(andQuery);

        query.findObjects(mContext, new FindListener<StudentInfo>() {
            @Override
            public void onSuccess(List<StudentInfo> list) {
                Utils.showToast(mContext, "login succeed");
                StudentInfo info = list.get(0);
                if (info != null) {
                    Utils.writeToSharedPreferences(mContext, "account", mAccountEt.getText().toString());
                    Utils.writeToSharedPreferences(mContext, "password", mPasswordEt.getText().toString());
                    ConfigUtils.setCurrentUserId(mAccountEt.getText().toString());
                    saveDataToDatabase(info);
                }
                Utils.dismissLoadingDialog(true, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        succeedToLogin();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Utils.showToast(mContext, R.string.toast_login_account_error);
                Utils.dismissLoadingDialog(false, null);
            }
        });
    }

    private void confirmAccountForTeacher() {
        int account;
        String password;
        try {
            account = Integer.parseInt(mAccountEt.getText().toString());
            password = mPasswordEt.getText().toString();
        } catch (Exception e) {
            Utils.showToast(mContext, R.string.toast_login_account_error);
            Utils.dismissLoadingDialog();
            return;
        }

        BmobQuery<TeacherInfo> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("id", account);

        BmobQuery<TeacherInfo> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("password", password);

        List<BmobQuery<TeacherInfo>> andQuery = new ArrayList<>();
        andQuery.add(query1);
        andQuery.add(query2);

        BmobQuery<TeacherInfo> query = new BmobQuery<>();
        query.and(andQuery);

        query.findObjects(mContext, new FindListener<TeacherInfo>() {
            @Override
            public void onSuccess(List<TeacherInfo> list) {
                Utils.showToast(mContext, "login succeed");
                TeacherInfo info = list.get(0);
                if (info != null) {
                    Utils.writeToSharedPreferences(mContext, "account", mAccountEt.getText().toString());
                    Utils.writeToSharedPreferences(mContext, "password", mPasswordEt.getText().toString());
                    ConfigUtils.setCurrentUserId(mAccountEt.getText().toString());
                    saveDataToDatabase(info);

                    succeedToLogin();
                }
                Utils.dismissLoadingDialog();
            }

            @Override
            public void onError(int i, String s) {
                Utils.showToast(mContext, R.string.toast_login_account_error);
                Utils.dismissLoadingDialog();
            }
        });
    }

    private void saveDataToDatabase(BmobBaseBean info) {
        Intent intent = new Intent(mContext, SaveDataService.class);
        intent.putExtra("data", info);
        intent.putExtra("type", 1);
        mContext.startService(intent);
    }

    private void succeedToLogin() {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        getActivity().finish();
    }
}
