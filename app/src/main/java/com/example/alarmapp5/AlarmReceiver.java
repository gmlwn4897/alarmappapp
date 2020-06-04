package com.example.alarmapp5;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    static String TAG="AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar cal = Calendar.getInstance();


        //notification id와 메시지 가져오기
        int notificationId = intent.getIntExtra("notificationId",0);
        intent.putExtra("notificationId", notificationId+1);
        String message = intent.getStringExtra("drug");

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,notificationId,mainIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "drugId");
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.P) {

            builder.setSmallIcon(R.drawable.ic_drug_icon);

            String chaanelName = "약쏙";
            String description = "매일 정해진 시간에 알림합니다. ";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("drugId", chaanelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        else
            builder.setSmallIcon(R.mipmap.ic_launcher);

            builder.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("약쏙")
                    .setContentText(message+"을(를) 복용할시간에요:)")
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(contentIntent)
                    .setContentInfo("INFO")
                    .setDefaults(Notification.DEFAULT_VIBRATE);



        if(notificationManager !=null){


            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "My:Tag"
            );
            wakeLock.acquire(5000);
            notificationManager.notify(notificationId, builder.build());


            //Calendar nextNotifyTime = Calendar.getInstance();

            //nextNotifyTime.add(Calendar.DATE,1);

            //SharedPreferences.Editor editor = context.getSharedPreferences("drug alarm",Context.MODE_PRIVATE).edit();
            //editor.putLong("nextNotifyTime",nextNotifyTime.getTimeInMillis());
            //editor.apply();

            //다음 이시간에 알림도 자동으로 설정
            //Date currentDateTime = nextNotifyTime.getTime();
            //String date_next = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 hh시 mm분", Locale.getDefault()).format(currentDateTime);
            //Toast.makeText(context.getApplicationContext(),"다음 알림은"+date_next+"으로 설정되었습니다.", Toast.LENGTH_SHORT).show();

        }
    }
}
