package com.vstar.sacredsun_android_pda.util.other;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.vstar.sacredsun_android_pda.App;
import com.vstar.sacredsun_android_pda.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by tanghuailong on 2017/2/20.
 */

public class NotificationHelper {

    private static  int ID = 2;

    public static void showNotification(String msg,int nofityType){

        String title = "";
        if(nofityType == 0) {
            title = "绑定";
        }else if(nofityType == 1) {
            title ="解绑";
        }

        Notification notification = new NotificationCompat.Builder(App.getInstance())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title+"订单")
                .setContentText(msg+" "+title+"成功")
                .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND)
                .build();
        NotificationManager manager = (NotificationManager) App.getInstance().getSystemService(NOTIFICATION_SERVICE);
        if(ID == Integer.MAX_VALUE) {
            ID = 0;
        }
        manager.notify(++ID,notification);

    }
}
