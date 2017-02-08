package com.vstar.sacredsun_android_pda.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.CodeType;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.other.ScanHelper;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanghuailong on 2017/1/20.
 */

public class CodeScanActivity extends AppCompatActivity {

    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.txt_manual)
    TextView txtManual;
    @BindView(R.id.scan_code)
    EditText scanCode;
    @BindView(R.id.order_sign_img)
    ImageView orderSignImg;
    @BindView(R.id.order_sign_txt)
    TextView orderSignTxt;
    @BindView(R.id.device_sign_img)
    ImageView deviceSignImg;
    @BindView(R.id.device_sign_txt)
    TextView deviceSignTxt;
    @BindView(R.id.btn_scan_again)
    Button btnScanAgain;

    //进行过的操作
    private Stack<Pair<String,String>> operation = new Stack<Pair<String,String>>();

    private static final String LOG_TAG = "CodeScanActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scan);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_commit)
    public void commitMessage() {
        String scanResult = ScanHelper.getScanText(scanCode);
        CodeType result = ScanHelper.judgeCodeNumber(scanResult);
        switch (result) {
            case ORDER:
                DialogInterface.OnClickListener orderListener= (dialog,which) -> {
                    operation.push(Pair.create("order","订单"));
                    orderSignImg.setImageResource(R.drawable.complete);
                    orderSignTxt.setText(R.string.order_scan_complete);
                    SPHelper.putAndApply(CodeScanActivity.this,"order",scanResult);
                    if(operation.size() == 1) {
                        btnCommit.setText("绑定");
                    }
                    scanCode.getText().clear();
                };
                ScanHelper.showDialog(CodeScanActivity.this,"订单",R.drawable.submit,"扫描结果:"+scanResult+" 确认提交?",orderListener);
                break;
            case DEVICE:
                DialogInterface.OnClickListener deviceListener= (dialog,which) -> {
                    operation.push(Pair.create("device","设备"));
                    deviceSignImg.setImageResource(R.drawable.complete);
                    deviceSignTxt.setText(R.string.order_scan_complete);
                    SPHelper.putAndApply(CodeScanActivity.this,"device",scanResult);
                    if(operation.size() == 1) {
                        btnCommit.setText("绑定");
                    }
                    scanCode.getText().clear();
                };
                ScanHelper.showDialog(CodeScanActivity.this,"设备",R.drawable.submit,"扫描结果:"+scanResult+" 确认提交?",deviceListener);
                break;
            default:
                ScanHelper.showDialog(CodeScanActivity.this,"未知",R.drawable.error,"无效的扫描结果:"+scanResult,null);
                scanCode.getText().clear();
                break;
        }
    }

    /**
     * 重新扫描
     */
    @OnClick(R.id.btn_scan_again)
    public void scanAgain() {
        DialogInterface.OnClickListener againListener= (dialog,which) -> {
            if(!operation.isEmpty()) {
                Pair<String,String> pair = operation.pop();
                SPHelper.remove(CodeScanActivity.this,pair.first);
            }
        };
        if(!operation.isEmpty()) {
            ScanHelper.showDialog(CodeScanActivity.this, "消除", R.drawable.warning, "是否要清除" + operation.peek().second + "的扫描结果?", againListener);
        }else{
            ScanHelper.showDialog(CodeScanActivity.this, "消除", R.drawable.warning, "无最近操作记录", null);
        }
    }
    /**
     * 切换为手动模式
     */
    @OnClick(R.id.txt_manual)
    public void switchManualMode() {
        ScanHelper.changeToManualInput(CodeScanActivity.this,scanCode);
    }
}
