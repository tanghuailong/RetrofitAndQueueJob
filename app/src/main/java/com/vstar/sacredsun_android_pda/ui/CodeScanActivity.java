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
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;
import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.entity.RunResult;
import com.vstar.sacredsun_android_pda.job.BindJob;
import com.vstar.sacredsun_android_pda.util.other.CodeType;
import com.vstar.sacredsun_android_pda.util.other.FunctionUtil;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;
import com.vstar.sacredsun_android_pda.util.other.StatusCompoment;
import com.vstar.sacredsun_android_pda.util.rxjava.RxBus;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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

    private JobManager jobManager;
    private Subscription subscription;

    //进行过的操作
    private Stack<Pair<String,String>> operation = new Stack<Pair<String,String>>();

    private static final String LOG_TAG = "CodeScanActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scan);
        ButterKnife.bind(this);
        jobManager = App.getInstance().getJobManager();
    }


    @OnClick(R.id.btn_commit)
    public void commitMessage() {
        String scanResult = FunctionUtil.getEditText(scanCode);
        CodeType result = FunctionUtil.judgeCodeNumber(scanResult);

        switch (result) {
            case ORDER:
                DialogInterface.OnClickListener orderListener= (dialog,which) -> {
                    operation.push(Pair.create(getString(R.string.ORDER),getString(R.string.ORDER_DESC)));
                    FunctionUtil.changeStateCompoment(CodeScanActivity.this,orderSignImg,orderSignTxt, StatusCompoment.ORDER_COMPLEMENT);
                    SPHelper.putAndApply(CodeScanActivity.this,getString(R.string.ORDER),scanResult);
                    if(operation.size() == 1) {
                        btnCommit.setText("绑定");
                    }
                    scanCode.getText().clear();
                    if(operation.size() == 2) {
                        //当两次操作都处理完成之后,提交到队列中去
                        jobManager.addJobInBackground(createBindJob(scanResult));
                    }
                };
                FunctionUtil.showDialog(CodeScanActivity.this,"订单",R.drawable.submit,"扫描结果:"+scanResult+" 确认提交?",orderListener);
                break;
            case DEVICE:
                DialogInterface.OnClickListener deviceListener= (dialog,which)  -> {
                    operation.push(Pair.create(getString(R.string.DEVICE),getString(R.string.DEVICE_DESC)));
                    FunctionUtil.changeStateCompoment(CodeScanActivity.this,deviceSignImg,deviceSignTxt, StatusCompoment.DEVICE_COMPLEMENT);
                    SPHelper.putAndApply(CodeScanActivity.this,getString(R.string.DEVICE),scanResult);
                    if(operation.size() == 1) {
                        btnCommit.setText("绑定");
                    }
                    scanCode.getText().clear();
                    if(operation.size() == 2) {
                        jobManager.addJobInBackground(createBindJob(scanResult));
                    }
                };
                FunctionUtil.showDialog(CodeScanActivity.this,"设备",R.drawable.submit,"扫描结果:"+scanResult+" 确认提交?",deviceListener);
                break;
            default:
                FunctionUtil.showDialog(CodeScanActivity.this,"未知",R.drawable.error,"无效的扫描结果:"+scanResult,null);
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
                btnCommit.setText("确定");
                Pair<String,String> pair = operation.pop();
                SPHelper.remove(CodeScanActivity.this,pair.first);
                if(pair.first.equals(getString(R.string.ORDER))) {
                    FunctionUtil.changeStateCompoment(CodeScanActivity.this,orderSignImg,orderSignTxt, StatusCompoment.ORDER_INCOMPLEMENT);
                }else if(pair.first.equals(getString(R.string.DEVICE))) {
                    FunctionUtil.changeStateCompoment(CodeScanActivity.this,deviceSignImg,deviceSignTxt, StatusCompoment.DEVICE_INCOMPLEMENT);
                }
            }
        };
        if(!operation.isEmpty()) {
            FunctionUtil.showDialog(CodeScanActivity.this, "消除", R.drawable.warning, "是否要清除" + operation.peek().second + "的扫描结果?", againListener);
        }else{
            FunctionUtil.showDialog(CodeScanActivity.this, "消除", R.drawable.warning, "无最近操作记录", null);
        }
    }
    /**
     * 切换为手动模式
     */
    @OnClick(R.id.txt_manual)
    public void switchManualMode() {
        FunctionUtil.changeToManualInput(CodeScanActivity.this,scanCode);
    }



    /**
     * 为了和Operation队列保持一致，退出时要清除数据保存的数据
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(subscription == null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        //TODO 清空所有操作
        while (!operation.empty()) {
            operation.pop();
        }
        SPHelper.remove(CodeScanActivity.this,getString(R.string.ORDER));
        SPHelper.remove(CodeScanActivity.this,getString(R.string.DEVICE));
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscription = RxBus.getDefault().tObservable(RunResult.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((r) -> {
                    Toast.makeText(CodeScanActivity.this,r.getMessage(),Toast.LENGTH_LONG).show();
                });
    }

    private BindJob createBindJob(String orderNumber) {

        BindJob bindJob = BindJob.create()
                .setAssetsCode((String)SPHelper.get(CodeScanActivity.this,getString(R.string.DEVICE),""))
                .setWorkerSession((String)SPHelper.get(CodeScanActivity.this,getString(R.string.WORKER_SESSION),""))
                .setDriverSession((String)SPHelper.get(CodeScanActivity.this,getString(R.string.DRIVER_SESSION),""))
                .setOrderCount((String)SPHelper.get(CodeScanActivity.this,getString(R.string.ORDER_NUMBER),""))
                .setOrderCode(FunctionUtil.getOrder(orderNumber))
                .setMaterialCode(FunctionUtil.getOrder(orderNumber))
                .setNumber(FunctionUtil.getSerialNumber(orderNumber))
                .setRelCreateTime(FunctionUtil.getCurrentTimeStr());

        return bindJob;

    }


}
