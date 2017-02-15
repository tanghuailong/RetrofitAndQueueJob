package com.vstar.sacredsun_android_pda.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
        if (FunctionUtil.isVaildInteger(result)) {
            if (!TextUtils.isEmpty(result)) {
                //保存order数量
                SPHelper.putAndApply(OrderActivity.this, getString(R.string.ORDER_NUMBER), result);
                Intent intent = new Intent(OrderActivity.this, CodeScanActivity.class);
                startActivity(intent);
            }
        }else {
            //隐藏软键盘
            hindSofeKeyBoard();
            FunctionUtil.showDialog(OrderActivity.this, "订单数量", R.drawable.error, "输入数量不符合规定",(d,w) -> {
                if (!TextUtils.isEmpty(result)) {
                    //保存order数量
                    SPHelper.putAndApply(OrderActivity.this, getString(R.string.ORDER_NUMBER), result);
                    Intent intent = new Intent(OrderActivity.this, CodeScanActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void hindSofeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
