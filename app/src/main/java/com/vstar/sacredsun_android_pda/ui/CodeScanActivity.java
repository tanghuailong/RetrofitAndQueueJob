package com.vstar.sacredsun_android_pda.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.CodeType;
import com.vstar.sacredsun_android_pda.util.other.ScanHelper;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scan);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_commit)
    public void commitMessage() {
        CodeType result = ScanHelper.judgeCodeNumber(ScanHelper.getScanText(scanCode));
        if(result.equals())
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    }

    /**
     * 切换为手动模式
     */
    @OnClick(R.id.txt_manual)
    public void switchManualMode() {
        ScanHelper.changeToManualInput(CodeScanActivity.this,scanCode);
    }


}
