package com.vstar.sacredsun_android_pda.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.path.android.jobqueue.JobManager;
import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.job.UnBindJob;
import com.vstar.sacredsun_android_pda.util.other.CodeType;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanghuailong on 2017/1/20.
 */

/**
 * 用来解绑 订单的Activity
 */
public class DeviceScanActivity extends AppCompatActivity {

    @BindView(R.id.scan_code)
    EditText scanCode;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_scan_again)
    Button btnScanAgain;
    @BindView(R.id.txt_manual)
    TextView txtManual;

    private JobManager jobManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        ButterKnife.bind(this);
        jobManager = App.getInstance().getJobManager();
    }

    @OnClick(R.id.btn_commit)
    public void orderUnbind() {
        String scanResult = FunctionUtil.getEditText(scanCode);
        CodeType result = FunctionUtil.judgeCodeNumber(scanResult);

        DialogInterface.OnClickListener deviceListener = (dialog, which) -> {
            //订单解绑
            jobManager.addJobInBackground(UnBindJob.create().setAssetsCode(scanResult));
        };

        DialogInterface.OnClickListener invalidListener = (dialog, which) -> {
            scanCode.getText().clear();
        };

        if(result == CodeType.DEVICE) {
            FunctionUtil.showDialog(DeviceScanActivity.this, "设备", R.drawable.warning, "扫描结果:" + scanResult + " 确认解绑?", deviceListener);
        }else {
            FunctionUtil.showDialog(DeviceScanActivity.this, "未知", R.drawable.warning, "无效的扫描结果", invalidListener);
        }
    }

    @OnClick(R.id.btn_scan_again)
    public void deviceCodeScanAgain() {
        scanCode.getText().clear();
    }

    /**
     * 切换为手动模式
     */
    @OnClick(R.id.txt_manual)
    public void switchManualMode() {
        FunctionUtil.changeToManualInput(DeviceScanActivity.this,scanCode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPHelper.remove(DeviceScanActivity.this,getString(R.string.ORDER_UNBIND));
    }

}
