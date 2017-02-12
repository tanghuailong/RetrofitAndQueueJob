package com.vstar.sacredsun_android_pda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanghuailong on 2017/2/12.
 */

public class OrderActivity extends AppCompatActivity{

    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.order_count)
    EditText orderCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_commit)
    public void commitInput() {
        String result = FunctionUtil.getEditText(orderCount);
        if(!TextUtils.isEmpty(result)) {
            //保存order数量
            SPHelper.putAndApply(OrderActivity.this,getString(R.string.ORDER_NUMBER),result);
            Intent intent = new Intent(OrderActivity.this,CodeScanActivity.class);
            startActivity(intent);
        }
    }
}
