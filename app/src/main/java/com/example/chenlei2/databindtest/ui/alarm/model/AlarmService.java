package com.example.chenlei2.databindtest.ui.alarm.model;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;


import com.example.basemoudle.util.LogUtil;

import org.json.JSONException;

import java.util.List;
import java.util.Locale;

/**
 * Created by chenlei2 on 2017/4/10 0010.
 */

public class AlarmService extends Service {


    private static final String TAG = AlarmService.class.getSimpleName();
    private static final String DB_NAME ="alarm.db";
    private static final int DB_VERSION = 1;
    private static final String BROADCAST_ALARM_COMING = "com.twsz.app.ivybox.clent.alarm.coming";
    private static final String PARAM_ALARM = "alarm";
    private final Binder localBinder =new LocalBinder();

    AlarmDbManager alarmDbManager;
    AlarmManager alarmManager;
    AlarmStateManager alarmStateManager;


    private final BroadcastReceiver alarmReciver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            final Alarm alarm = intent.getParcelableExtra(PARAM_ALARM);
            if(alarm != null){
                LogUtil.i(TAG,alarm.toString());
                alarmStateManager.toAlertingState(alarm);
            }
        }
    };


    public class LocalBinder extends Binder {
        public AlarmService getService(){
            return AlarmService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmDbManager = new AlarmDbManager(this,new String[]{Alarm.class.getName(),AlarmRule.class.getName()},DB_NAME,DB_VERSION);
        alarmManager = new AlarmManager(this);
        alarmStateManager = new AlarmStateManager(this,alarmDbManager);
        registerReceiver(alarmReciver,new IntentFilter(BROADCAST_ALARM_COMING));
        initAllUnRemindAlarmClocks();
    }

    private void initAllUnRemindAlarmClocks(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<Alarm> alarms = (List<Alarm>) alarmDbManager.queryAllByParams(Alarm.class,new String[]{Alarm.FILED_STATE},new String[]{String.valueOf(Alarm.STATE_UNREMIND)});
                if(alarms != null && !alarms.isEmpty()){
                    for(int i = 0 ; i<alarms.size();i++){
                        addClock(alarms.get(i));
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(alarmReciver);
    }

    public boolean addAlarmRule(AlarmRule alarmRule){
        if(addAlarm(alarmRule)){
            return  alarmDbManager.create(alarmRule,AlarmRule.class) > 0;
        }
        return  false;
    }

    public boolean addAlarm(AlarmRule alarmRule){
        try {
            Alarm nextAlarm = alarmRule.getNextAlarm(System.currentTimeMillis());
            if(nextAlarm != null){
                return addAlarm(nextAlarm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean updateAlarmRule(AlarmRule alarmRule){
        Alarm  stateUnRemindAlarm = (Alarm) alarmDbManager.queryFirstByParams(Alarm.class,new String[]{Alarm.FILED_STATE,Alarm.FILED_ALARMRULE_ID},new String[]{String.valueOf(Alarm.STATE_UNREMIND), String.valueOf(alarmRule.getId())});
        removeAlarm(stateUnRemindAlarm);
        if(addAlarm(alarmRule)){
           return alarmDbManager.update(alarmRule,AlarmRule.class) > 0;
        }
        return  false;
    }



    public boolean delAlarmRule(AlarmRule alarmRule){
//       / Alarm  stateUnRemindAlarm = (Alarm) alarmDbManager.queryFirstByParams(Alarm.class,new String[]{Alarm.FILED_STATE,Alarm.FILED_ALARMRULE_ID},new String[]{String.valueOf(Alarm.STATE_UNREMIND),String.valueOf(alarmRule.getId())});
        List<Alarm> unRemindAlarms = (List<Alarm>) alarmDbManager.queryAllByParams(Alarm.class,new String[]{Alarm.FILED_STATE,Alarm.FILED_ALARMRULE_ID},new String[]{String.valueOf(Alarm.STATE_UNREMIND), String.valueOf(alarmRule.getId())});
        if(unRemindAlarms != null && unRemindAlarms.size() != 0){
            for(Alarm alarm:unRemindAlarms){
                removeAlarm(alarm);
            }
        }
        alarmDbManager.deleteAlarmRule(alarmRule);
        return  true;
    }

    public boolean removeAlarm(Alarm alarm){
        if(alarmDbManager.delete(alarm,Alarm.class) >0){
            LogUtil.d(TAG, String.format(Locale.CHINA,"remove %s",alarm.toString()));
            Intent intent = new Intent(BROADCAST_ALARM_COMING);
            intent.putExtra(PARAM_ALARM, alarm);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.delClock(pendingIntent);
            return  true;
        }else{
            return  false;
        }
    }

    public void addClock(Alarm alarm){
        LogUtil.d(TAG, String.format(Locale.CHINA,"add %s",alarm.toString()));
        Intent intent = new Intent(BROADCAST_ALARM_COMING);
        intent.putExtra(PARAM_ALARM, alarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.addClock(alarm.getAlarmTime(), pendingIntent);
    }
    public boolean addAlarm(Alarm alarm){
        if(alarmDbManager.create(alarm,Alarm.class) > 0) {
            addClock(alarm);
            return true;
        }else{
            return false;
        }
    }
    public List<?> queryForAll(Class<?> clazz){
        return  alarmDbManager.queryForAll(clazz);
    }
}
