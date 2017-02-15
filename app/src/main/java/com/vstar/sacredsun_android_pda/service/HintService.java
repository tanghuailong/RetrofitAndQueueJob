package com.vstar.sacredsun_android_pda.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.vstar.sacredsun_android_pda.R;
import com.vstar.sacredsun_android_pda.util.other.SPHelper;

/**
 * Created by tanghuailong on 2017/2/14.
 */

public class HintService extends Service {

    Handler handler = null;
    PendingIntent pendingIntent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        pendingIntent = PendingIntent.getActivity(this,requestID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int number = (int)SPHelper.get(HintService.this,"not_handle",0);
                showNotification(number);
                handler.postDelayed(this,10000);
            }
        };
        handler.postDelayed(runnable,10000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(int count){

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("任务未处理")
                .setContentText(count+"任务未处理")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setOngoing(true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }
}
