package com.example.alarmapp5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    SQLiteDatabase sqliteDB;

    private TimePicker timePicker;
    private AlarmManager alarmManager;
    private int hour,minute;
    private EditText drugText;
    private int notificationId=1;

    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timePicker = findViewById(R.id.timepicker);
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

    }


    public void regist(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            Toast.makeText(this, "버전을 확인해 주세요;)", Toast.LENGTH_SHORT).show();
            return;
        }

        drugText = (EditText) findViewById(R.id.editText);


        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("drug", drugText.getText().toString());
        intent.putExtra("notificationId", notificationId);


        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();


        PendingIntent pIntent = PendingIntent.getBroadcast(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationId++;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = new Date();
        long intervalDay = 24 * 60 * 60 * 1000;

        long selectTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();

        if (currentTime > selectTime) {
            selectTime += intervalDay;
        }

        Log.e(TAG, "알람 설정시간: " + calendar.getTime());
        Log.d(TAG, "calendar.getTimeInMillis(): " + calendar.getTimeInMillis());
        Toast.makeText(this, "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show();

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, intervalDay, pIntent);


    }

    public void cancel(View view){
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this,notificationId,intent,0);
        alarmManager.cancel(pIntent);
        Toast.makeText(this,"알림이 취소되었습니다.",Toast.LENGTH_SHORT).show();

    }


}
