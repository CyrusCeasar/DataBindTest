package com.example.chenlei2.databindtest.model;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;


@SuppressLint("SimpleDateFormat")
public class AlarmHelper
{
    
    @SuppressWarnings("unused")
    private static final String TAG = AlarmHelper.class.getSimpleName();
    
    private Context mContext;
    
    public AlarmHelper(Context context)
    {
        this.mContext = context;
    }
    
    /** 非重复*/
    public void addClock(long startTime, PendingIntent action)
    {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, action);
    }
    
    /** 重复闹钟*/
    public void addRepeatClock(long startTime, long interval,
            PendingIntent action)
    {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                startTime,
                interval,
                action);
    }


  /*  public void addClock(Alarm alarm, Context context, boolean isRepeatClock)
    {
        try
        {
            int putOffStart = 0;
            long clockTime = 0;
            long currentTime = DateUtil.getDayMillis_Optimize();
            Schedule schedule = alarm.getSchedule();
            long beginTime = schedule.getStartTime();
            long interval = schedule.getInterval() + 1;
            long alarmTime = alarm.getTime();

            if (!isRepeatClock)
            {
                addClock(Calendar.getInstance().getTimeInMillis()
                        + DateUtil.FIVE_MINUTE,
                        getPendingIntent(alarm, context));
            }
            else
            {
                if (beginTime > currentTime)
                {
                    clockTime = DateUtil.getDayMillis_Optimize() + (beginTime - currentTime) + alarmTime;
                    *//*putOffStart = (int) ((beginTime - currentTime) / DateUtil.TIME_IN_MILLS_DAY);
                   long clockTime2 = DateUtil.getDayMillis_Optimize() + putOffStart
                            * DateUtil.TIME_IN_MILLS_DAY + alarmTime;*//*
                }
                else if (beginTime == currentTime)
                {
                    clockTime = DateUtil.getDayMillis_Optimize() + alarmTime;
                    clockTime = clockTime > Calendar.getInstance()
                            .getTimeInMillis() ? clockTime : clockTime
                            + interval * DateUtil.TIME_IN_MILLS_DAY;
                }
                addRepeatClock(clockTime,
                        alarm.getIntervalTime(),
                        getPendingIntent(alarm, context));
            }
        }
        catch (Exception e)
        {
        }
    }

    public PendingIntent getPendingIntent(Alarm alarm, Context context)
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, AcMedicineAlarm.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Alarm.NAME, alarm);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                (int) alarm.getAlarmId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public void delClock(Alarm alarm, Context context)
    {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(alarm, context));
    }*/
}