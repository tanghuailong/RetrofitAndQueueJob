package com.vstar.sacredsun_android_pda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.testCookie)
    Button button;

    private static final String LOG_TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDpi();
    }


    @OnClick(R.id.testCookie)
    public void clickForTest(View view) {
        Log.d(LOG_TAG,"clickTestCookie");
        AppJobManager.getJobManager().addJobInBackground(new OrderJob());
    }

    public void getDpi() {
        Log.d(LOG_TAG,"getDpi");
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.d(LOG_TAG,metrics.toString());
    }
}
