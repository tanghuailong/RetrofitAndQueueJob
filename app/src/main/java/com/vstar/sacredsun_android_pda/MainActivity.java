package com.vstar.sacredsun_android_pda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vstar.sacredsun_android_pda.rest.GithubApi;
import com.vstar.sacredsun_android_pda.rest.HttpMethods;
import com.vstar.sacredsun_android_pda.rest.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.testCookie)
    Button button;

    private static final String LOG_TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.testCookie)
    public void clickForTest(View view) {
        Log.d(LOG_TAG,"clickTestCookie");
        HttpMethods.setContext(this);
        GithubApi api =  HttpMethods.getInstane().getService(GithubApi.class);
        retrofit2.Call<User> call = api.getUser("tanghuailong");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(LOG_TAG,response.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }
}
