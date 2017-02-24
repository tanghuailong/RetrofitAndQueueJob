package com.vstar.sacredsun_android_pda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.service.HintService;
import com.vstar.sacredsun_android_pda.service.PDAApi;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.rest.HttpMethods;
import com.vstar.sacredsun_android_pda.util.rxjava.RxHelper;

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
        startHintService();
    }



    @OnClick(R.id.btn_login_out)
    public void loginOut() {

//        FunctionUtil.showDialog(MainActivity.this,"登出",R.drawable.allow,"确认要退出登陆么?",null);

        String workerSession = (String) SPHelper.get(MainActivity.this,getString(R.string.WORKER_SESSION),"");
        String driverSession = (String) SPHelper.get(MainActivity.this,getString(R.string.DRIVER_SESSION),"");

        if(TextUtils.isEmpty(workerSession) && TextUtils.isEmpty(driverSession)) {
            SPHelper.putAndApply(MainActivity.this,getString(R.string.IS_LOGIN),false);
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        //工人退出
        if(!TextUtils.isEmpty(workerSession)) {
            HttpMethods.getInstane().getService(PDAApi.class)
                    .userLoginOut(workerSession)
                    .compose(RxHelper.io_main())
                    .subscribe((r) -> {
                        SPHelper.remove(MainActivity.this,getString(R.string.WORKER_SESSION));

                        String session1 = (String) SPHelper.get(MainActivity.this,getString(R.string.WORKER_SESSION),"");
                        String session2 = (String) SPHelper.get(MainActivity.this,getString(R.string.DRIVER_SESSION),"");
                        if(TextUtils.isEmpty(session1) && TextUtils.isEmpty(session2)) {
                            SPHelper.putAndApply(MainActivity.this,getString(R.string.IS_LOGIN),false);
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },(e) -> {
                        Log.d(LOG_TAG,"some error happen",e);
                        Toast.makeText(MainActivity.this,"工人退出失败",Toast.LENGTH_SHORT).show();
                    });
        }
        //司机退出
        if(!TextUtils.isEmpty(driverSession)) {
            HttpMethods.getInstane().getService(PDAApi.class)
                    .userLoginOut(driverSession)
                    .compose(RxHelper.io_main())
                    .subscribe((r) -> {
                        SPHelper.remove(MainActivity.this,getString(R.string.DRIVER_SESSION));

                        String session1 = (String) SPHelper.get(MainActivity.this,getString(R.string.WORKER_SESSION),"");
                        String session2 = (String) SPHelper.get(MainActivity.this,getString(R.string.DRIVER_SESSION),"");
                        if(TextUtils.isEmpty(session1) && TextUtils.isEmpty(session2)) {
                            SPHelper.putAndApply(MainActivity.this,getString(R.string.IS_LOGIN),false);
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },(e) -> {
                        Log.d(LOG_TAG,"some error happen",e);
                        Toast.makeText(MainActivity.this,"司机退出失败",Toast.LENGTH_SHORT).show();
                    });
        }

    }

    /**
     * 订单绑定
     */
    @OnClick(R.id.order_bind)
    public void orderBind() {
        Intent intent = new Intent(MainActivity.this,OrderActivity.class);
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

    private void startHintService() {
        Intent intent = new Intent(MainActivity.this, HintService.class);
        startService(intent);
    }
}
