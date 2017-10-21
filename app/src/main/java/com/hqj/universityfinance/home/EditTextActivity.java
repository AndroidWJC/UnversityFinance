package com.hqj.universityfinance.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hqj.universityfinance.BaseActivity;
import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-10-17.
 */

public class EditTextActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittxt);

        initView();
    }

    private void initView() {
        mEditText = (EditText) findViewById(R.id.edit_content);
        String content = getIntent().getStringExtra("content");
        if (content != null) {
            mEditText.setText(content);
            mEditText.setSelection(content.length());
        }

        mActionBarTitle.setText(getIntent().getStringExtra("title"));
        mActionBarRightBtn.setVisibility(View.VISIBLE);
        mActionBarRightBtn.setText(R.string.btn_save);
        mActionBarRightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_btn:
                Intent intent = new Intent();
                intent.putExtra("content", mEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
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
