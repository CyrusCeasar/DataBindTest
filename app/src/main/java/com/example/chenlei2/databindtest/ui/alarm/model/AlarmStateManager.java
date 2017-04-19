package com.example.chenlei2.databindtest.ui.alarm.model;


import com.example.basemoudle.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenlei2 on 2017/4/11 0011.
 */

public class AlarmStateManager {
    private static final String TAG = AlarmStateManager.class.getSimpleName();
    private static final long ALERT_OUT_OF_TIME = 2 * 60 * 1000;//two minutes

    private AlertManager mAlertManager;
    private AlarmDbManager mAlarmDbManager;
    private AlarmService mAlarmService;

    private Alarm alertIngAlarm;
    private Timer mTimer;

    public AlarmStateManager(AlarmService alarmService, AlarmDbManager alarmDbManager) {
        mAlarmService = alarmService;
        this.mAlertManager = new AlertManager(mAlarmService);
        this.mAlarmDbManager = alarmDbManager;
    }

    public synchronized void toAlertingState(final Alarm alarm) {
        scheduleToNextAlarm(alarm);
        if (alertIngAlarm != null) {
            mTimer.cancel();
            mTimer = new Timer();
            toUnhandleState(alertIngAlarm);
        }
        alertIngAlarm = alarm;
        LogUtil.d(TAG, String.format("alarm:%s    to  alerting  state", alertIngAlarm.toString()));
        mAlertManager.startAlert("ring.mp3");
        alarm.setState(Alarm.STATE_ALERTING);
        mAlarmDbManager.update(alertIngAlarm, Alarm.class);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                toUnhandleState(alarm);
            }
        }, ALERT_OUT_OF_TIME);
    }

    public synchronized void toUnhandleState(Alarm alarm) {
        LogUtil.d(TAG, String.format("alarm:%s    to   unhandle state", alarm.toString()));
        alarm.setState(Alarm.STATE_UNHANDLED);
        mAlarmDbManager.update(alarm, Alarm.class);
        mAlertManager.pauseAlert();
    }


    /**
     * 计划下个闹钟的添加
     *
     * @param alarm 响铃的这个闹钟实例
     */
    public void scheduleToNextAlarm(Alarm alarm) {
        AlarmRule alarmRule = alarm.getAlarmRule();
        String ruleContent = alarmRule.getRuleContent();
        try {
            JSONObject ruleJson = new JSONObject(ruleContent);
            boolean repeated = ruleJson.getBoolean(AlarmRule.PARAM_BOOL_REPEAT);
            if (repeated) {// 重复计算出下个闹钟
                int end_type = ruleJson.getInt(AlarmRule.PARAM_INT_END_TYPE);
                long nextAlarmTime = alarmRule.calcNextAlarmTime(alarm.getAlarmTime());
                Alarm nextAlarm = new Alarm(alarmRule);
                nextAlarm.setAlarmTime(nextAlarmTime);
                switch (end_type) {
                    case AlarmRule.TYPE_END_NONE://一直提醒
                        mAlarmService.addAlarm(nextAlarm);
                        break;
                    case AlarmRule.TYPE_END_TIME://按过期时间结束闹钟
                        long outOfTime = ruleJson.getLong(AlarmRule.PARAM_LONG_OUT_OF_TIME);
                        if (nextAlarmTime > outOfTime) {//下个闹钟时间过期，将闹钟规则为过期状态
                            alarmRule.setState(AlarmRule.STATE_OUT_OF_TIME);
                            mAlarmDbManager.update(alarmRule, AlarmRule.class);
                        } else {//添加下个闹钟
                            mAlarmService.addAlarm(nextAlarm);
                        }
                        break;
                    case AlarmRule.TYPE_END_COUNT://按次数结束闹钟
                        int maxCount = ruleJson.getInt(AlarmRule.PARAM_INT_MAX_COUNT);
                        int repeatedCount = ruleJson.getInt(AlarmRule.PARAM_INT_REPEATED_COUNT);
                        if (++repeatedCount >= maxCount) {//下个闹钟的重复次数大于最大次数，将闹钟规则设为过期状态
                            alarmRule.setState(AlarmRule.STATE_OUT_OF_TIME);
                            mAlarmDbManager.update(alarmRule, AlarmRule.class);
                        } else {
                            mAlarmService.addAlarm(nextAlarm);
                            //闹钟规则重复数加1
                            ruleJson.put(AlarmRule.PARAM_INT_REPEATED_COUNT, repeatedCount);
                            alarmRule.setRuleContent(ruleJson.toString());
                            mAlarmDbManager.update(alarmRule, AlarmRule.class);
                        }
                        break;
                }
            } else {//不重复直接删除闹钟规则
                mAlarmDbManager.deleteAlarmRule(alarmRule);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.d(TAG, String.format("alarm ruleContent invalid format,delete alarmRule---%s and corresponding alarms", alarmRule.toString()));
            mAlarmDbManager.deleteAlarmRule(alarmRule);
        }
    }
}
