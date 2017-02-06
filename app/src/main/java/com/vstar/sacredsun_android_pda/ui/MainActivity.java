package com.vstar.sacredsun_android_pda.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;
import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.entity.OrderJob;
import com.vstar.sacredsun_android_pda.entity.UserEvent;
import com.vstar.sacredsun_android_pda.util.RxBus;
import com.vstar.sacredsun_android_pda.util.RxHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @BindView(R.id.testJob)
    Button testJob;

    private JobManager jobManager;
    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        subscription = RxBus.getDefault().tObservable(UserEvent.class)
                .compose(RxHelper.io_main())
                .subscribe(r -> {
            Toast.makeText(MainActivity.this,"发送成功 "+r.getMsg(),Toast.LENGTH_LONG).show();
        },e -> {
            e.printStackTrace();
        });
    }

    @OnClick(R.id.testJob)
    public void testJob(View view) {
        Log.d(LOG_TAG,"click button request github");
        jobManager = App.getInstance().getJobManager();
        jobManager.addJobInBackground(new OrderJob());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
