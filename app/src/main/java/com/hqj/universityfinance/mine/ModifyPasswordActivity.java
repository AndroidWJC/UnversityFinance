package com.hqj.universityfinance.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.LoginActivity;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.customview.ClearEditText;
import com.hqj.universityfinance.javabean.StudentInfo;
import com.hqj.universityfinance.utils.ActivityCollector;
import com.hqj.universityfinance.utils.Utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by wang on 17-11-25.
 */

public class ModifyPasswordActivity extends BaseActivity {

    ClearEditText mOriginalPasswordTv;
    ClearEditText mModifyPasswordTv;
    ClearEditText mModifyPasswordAgainTv;
    Button mConfirmBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        initView();
    }

    private void initView() {
        mActionBarTitle.setText(R.string.title_modify_password);

        mOriginalPasswordTv = (ClearEditText) findViewById(R.id.original_password_tv);
        mModifyPasswordTv = (ClearEditText) findViewById(R.id.modify_password_tv);
        mModifyPasswordAgainTv = (ClearEditText) findViewById(R.id.modify_password_again_tv);
        mConfirmBtn = (Button) findViewById(R.id.confirm_btn);
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showLoadingDialog(ModifyPasswordActivity.this);
                if (!isNull() && isSame()) {
                    if (confirmAccount()) {
                        modifyPassword();
                    } else {
                        Utils.showToast(ModifyPasswordActivity.this, R.string.toast_login_account_error);
                        Utils.dismissLoadingDialog();
                    }
                } else {
                    Utils.showToast(ModifyPasswordActivity.this, R.string.toast_not_same);
                    Utils.dismissLoadingDialog();
                }
            }
        });
    }

    private boolean isNull() {
        return mModifyPasswordTv.getText().toString().equals("")
                || mModifyPasswordAgainTv.getText().toString().equals("");
    }

    private boolean isSame() {
        return mModifyPasswordTv.getText().toString()
                .equals(mModifyPasswordAgainTv.getText().toString());
    }

    private boolean confirmAccount() {
        String password = mOriginalPasswordTv.getText().toString();
        return password.equals(Utils.getStringFromSharedPreferences(this, "password"));
    }

    private void modifyPassword() {
        String account = Utils.getStringFromSharedPreferences(this, "account");
        BmobQuery<StudentInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("id", Integer.valueOf(account));
        query.findObjects(this, new FindListener<StudentInfo>() {
            @Override
            public void onSuccess(List<StudentInfo> list) {
                if (list.size() > 0) {
                    modifyPasswordFinal(list.get(0).getObjectId());
                } else {
                    Utils.dismissLoadingDialog();
                }
            }

            @Override
            public void onError(int i, String s) {
                Utils.showToast(ModifyPasswordActivity.this, R.string.toast_unknown_error);
                Utils.dismissLoadingDialog();
            }
        });
    }

    private void modifyPasswordFinal(String objectId) {
        StudentInfo info = new StudentInfo();
        info.setPassword(mModifyPasswordTv.getText().toString());
        info.update(this, objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Utils.dismissLoadingDialog();
                goToLoginActivity();
            }

            @Override
            public void onFailure(int i, String s) {
                Utils.showToast(ModifyPasswordActivity.this, R.string.login_failed_net_error);
                Utils.dismissLoadingDialog();
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        ActivityCollector.finishAllActivities();
    }
}
