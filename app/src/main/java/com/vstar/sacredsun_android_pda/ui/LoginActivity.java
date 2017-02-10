package com.vstar.sacredsun_android_pda.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.service.PDAApi;
import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.rest.HttpMethods;
import com.vstar.sacredsun_android_pda.util.rxjava.RxHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanghuailong on 2017/1/20.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LoginActivity";

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.username)
    EditText editText_user;
    @BindView(R.id.password)
    EditText editText_pwd;
    @BindView(R.id.username_layout)
    TextInputLayout usernameLayout;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.login_container)
    CoordinatorLayout loginContainer;

    private static String workCenterCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        workCenterCode =(String) SPHelper.get(this,getString(R.string.WORKCENTERCODE_SESSION),"01");

        editText_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s.toString(),usernameLayout);
            }
        });
        editText_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s.toString(),passwordLayout);
            }
        });
    }

    /**
     * 点击登陆
     */
    @OnClick(R.id.btn_login)
    public void userLogin() {

        String username = FunctionUtil.getEditText(editText_user);
        String password = FunctionUtil.getEditText(editText_pwd);
        boolean isEmptyName = TextUtils.isEmpty(username);
        boolean isEmptyPwd = TextUtils.isEmpty(password);

        if(!isEmptyName && !isEmptyPwd) {
            //执行登陆操作
            HttpMethods.getInstane()
                    .getService(PDAApi.class)
                    .userLogin(username,password,workCenterCode)
                    .compose(RxHelper.io_main())
                    .subscribe((next) -> {

                    },(e) -> {
                        FunctionUtil.showSnackBar(LoginActivity.this,loginContainer,getString(R.string.login_fail),1);
                        e.printStackTrace();
                    },() -> {

                    });
        }else {
            if(isEmptyName) {
                usernameLayout.setErrorEnabled(true);
                usernameLayout.setError(getString(R.string.validate_empty));
            }
            if(isEmptyPwd) {
                passwordLayout.setErrorEnabled(true);
                passwordLayout.setError(getString(R.string.validate_empty));
            }
        }
    }


    private void validateEditText(String s,TextInputLayout layout) {
        if (TextUtils.isEmpty(s)) {
            layout.setErrorEnabled(true);
            layout.setError(getString(R.string.validate_empty));
        }else {
            layout.setErrorEnabled(false);
            layout.setError(null);
        }
    }


}
