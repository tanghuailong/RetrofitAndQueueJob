package com.vstar.sacredsun_android_pda.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    /**
     * 点击登陆
     */
    @OnClick(R.id.btn_login)
    public void userLogin() {

        String username = FunctionUtil.getEditText(editText_user);
        String password = FunctionUtil.getEditText(editText_pwd);

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            //工人登陆
//        HttpMethods.getInstane().getService(PDAApi.class).userLogin()
            //司机登陆 跳转到操作页面
        }else {
            editText_user.setError("不能为空");
            editText_pwd.setError("不能为空");
        }

    }
}
