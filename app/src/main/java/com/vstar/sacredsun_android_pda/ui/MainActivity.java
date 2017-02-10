package com.vstar.sacredsun_android_pda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @BindView(R.id.btn_login_out)
    TextView btnLoginOut;
    @BindView(R.id.order_bind)
    CardView orderBind;
    @BindView(R.id.order_unbind)
    CardView orderUnBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }




    @OnClick(R.id.btn_login_out)
    public void loginOut() {

        FunctionUtil.showDialog(MainActivity.this,"登出",R.drawable.allow,"确认要退出登陆么?",null);

        //删除司机和工人的session
        SPHelper.remove(MainActivity.this,getString(R.string.DRIVER_SESSION));
        SPHelper.remove(MainActivity.this,getString(R.string.WORKER_SESSION));
        //跳转到登陆页面 保证之前的Activity不能被返回
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 订单绑定
     */
    @OnClick(R.id.order_bind)
    public void orderBind() {
        Intent intent = new Intent(MainActivity.this,CodeScanActivity.class);
        startActivity(intent);
    }

    /**
     * 订单解绑
     */
    @OnClick(R.id.order_unbind)
    public void orderUnbind() {
        Intent intent = new Intent(MainActivity.this,DeviceScanActivity.class);
        startActivity(intent);
    }
}
