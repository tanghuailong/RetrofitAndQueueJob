package com.vstar.sacredsun_android_pda.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanghuailong on 2017/2/9.
 */

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.work_center_code)
    EditText workCenterCode;
    @BindView(R.id.work_center_code_layout)
    TextInputLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        workCenterCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s.toString());
            }
        });
    }

    /**
     * 确认输入的工位编号
     */
    @OnClick(R.id.btn_commit)
    public void inputConfirm() {

        workCenterCode.clearFocus();
        String result = FunctionUtil.getEditText(workCenterCode);

        if(!TextUtils.isEmpty(result)) {
            if(result.matches("^[a-zA-Z0-9]+$")) {
                DialogInterface.OnClickListener workCenterListener = (dialog, which) -> {
                    SPHelper.putAndApply(SettingActivity.this, getString(R.string.FIRST_USE), true);
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                };
                FunctionUtil.showDialog(SettingActivity.this, "工位编号", R.drawable.submit, "输入结果:" + result + " 确认提交?", workCenterListener);
            }
        }else {
            layout.setErrorEnabled(true);
            layout.setError(getString(R.string.validate_empty));
        }

    }


    private void validateEditText(String s) {
        if (TextUtils.isEmpty(s)) {
            layout.setErrorEnabled(true);
            layout.setError(getString(R.string.validate_empty));
        }
        else{
            if(!s.matches("^[a-zA-Z0-9]+$")){
                layout.setErrorEnabled(true);
                layout.setError(getString(R.string.validate_only_numchar));
            }else {
                layout.setErrorEnabled(false);
                layout.setError(null);
            }
        }
    }


}
