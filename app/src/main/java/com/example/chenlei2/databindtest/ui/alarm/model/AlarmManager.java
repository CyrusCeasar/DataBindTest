package com.example.chenlei2.databindtest.ui.alarm.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class AlarmManager
{
    
    @SuppressWarnings("unused")
    private static final String TAG = AlarmManager.class.getSimpleName();
    
    private Context mContext;
    
    public AlarmManager(Context context)
    {
        this.mContext = context;
    }
    
    /** 非重复*/
    public void addClock(long startTime, PendingIntent action)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, startTime, action);
    }
    
    /** 重复闹钟*/
    public void addRepeatClock(long startTime, long interval,
            PendingIntent action)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP,
                startTime,
                interval,
                action);

    }
    

    
    public PendingIntent getPendingIntent(Alarm alarm, Context context)
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClass(context, AcMedicineAlarm.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable(Alarm.NAME, alarm);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                (int) alarm.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public void delClock(PendingIntent intent)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
    }


}