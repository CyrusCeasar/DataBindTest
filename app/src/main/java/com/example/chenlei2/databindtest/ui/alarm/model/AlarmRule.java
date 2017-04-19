package com.example.chenlei2.databindtest.ui.alarm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.basemoudle.util.LogUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chenlei2 on 2017/4/10 0010.
 */
@DatabaseTable
public class AlarmRule implements Parcelable {
    public static final String TAG = AlarmRule.class.getSimpleName();


    public static final String PARAM_BOOL_REPEAT = "is_repeat";
    public static final String PARAM_LONG_OUT_OF_TIME = "out_of_time";
    public static final String PARAM_INT_MAX_COUNT = "max_count";
    public static final String PARAM_INT_REPEAT_TYPE = "repeat_type";
    public static final String PARAM_LONG_REPEAT_DAYS = "repeat_days";
    public static final String PARAM_INT_REPEAT_INTERVAL = "repeat_interval";
    public static final String PARAM_INT_REPEATED_COUNT = "repeat_count";
    public static final String PARAM_INT_END_TYPE = "end_type";

    public static final int REPEAT_TYPE_DAY = 0;
    public static final int REPEAT_TYPE_WEEK = 1;
    public static final int REPEAT_TYPE_MONTH = 2;
    public static final int REPEAT_TYPE_YEAR = 3;

    public static final byte STATE_NORMAL = 0;
    public static final byte STATE_OUT_OF_TIME = -1;

    public static final int TYPE_END_NONE = 0;
    public static final int TYPE_END_TIME = 1;
    public static final int TYPE_END_COUNT = 2;
    public static final Parcelable.Creator<AlarmRule> CREATOR = new Parcelable.Creator<AlarmRule>() {
        @Override
        public AlarmRule createFromParcel(Parcel source) {
            return new AlarmRule(source);
        }

        @Override
        public AlarmRule[] newArray(int size) {
            return new AlarmRule[size];
        }
    };
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = false)
    private String ruleContent;
    @DatabaseField(canBeNull = false, defaultValue = "0")
    private byte state;
    @DatabaseField(canBeNull = false)
    private long beginTime;
    @DatabaseField
    private String label;
    @DatabaseField(canBeNull = false)
    private String ringRule;

    public AlarmRule() {

    }

    protected AlarmRule(Parcel in) {
        this.id = in.readLong();
        this.ruleContent = in.readString();
        this.state = in.readByte();
        this.beginTime = in.readLong();
        this.label = in.readString();
        this.ringRule = in.readString();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRingRule() {
        return ringRule;
    }

    public void setRingRule(String ringRule) {
        this.ringRule = ringRule;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd dd:mm", Locale.CHINA);
        return String.format(Locale.CHINA, "id:%d   label:%s  \nbeginTime:%s   \nrule:%s \nringRule:%s", id, label, sdf.format(beginTime), ruleContent, ringRule);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.ruleContent);
        dest.writeByte(this.state);
        dest.writeLong(this.beginTime);
        dest.writeString(this.label);
        dest.writeString(this.ringRule);
    }

    public long calcNextAlarmTime(long beginTime) throws JSONException {
        JSONObject ruleJson = new JSONObject(ruleContent);
        int repeatType;
        long repeatDays;
        int repeatInterval;
        Calendar calendar = Calendar.getInstance();
        long nextAlarmTime = calendar.getTimeInMillis();

        repeatType = ruleJson.getInt(AlarmRule.PARAM_INT_REPEAT_TYPE);
        repeatInterval = ruleJson.getInt(AlarmRule.PARAM_INT_REPEAT_INTERVAL);
        switch (repeatType) {
            case AlarmRule.REPEAT_TYPE_DAY:
                if(nextAlarmTime >= beginTime) {//当前时间大于开始时间，下个闹钟时间需要计算下个循环
                    nextAlarmTime = beginTime + repeatInterval * DateUtil.MILLSECONDS_ONE_DAY;
                }else{//当前时间小于开始时间，下个闹钟时间就是开始时间
                    nextAlarmTime = beginTime;
                }
                break;
            case AlarmRule.REPEAT_TYPE_WEEK:
                int dayInterval = 1;
                boolean needUseRepeatInterval = false;
                int dayOfWeek = 1 << ((calendar.get(Calendar.DAY_OF_WEEK) +5)%7);//取得是周几
                repeatDays = ruleJson.getLong(AlarmRule.PARAM_LONG_REPEAT_DAYS);
                if((dayOfWeek&repeatDays) == dayOfWeek && nextAlarmTime<beginTime){//周重复中存在今天这一天，所以计算当前时间是否小于开始时间
                    nextAlarmTime = beginTime;
                }else {
                    int weekDay;
                    weekDay = dayOfWeek << dayInterval;
                    while ((weekDay & repeatDays) != weekDay) {//找到下次需要响铃的周天
                        weekDay = dayOfWeek <<++dayInterval;
                        if (weekDay == (1 << 7)) {//如果加上周间隔等于周8，计算下个7天循环
                            repeatDays = repeatDays << 7;
                        }
                    }
                    nextAlarmTime = beginTime + dayInterval * DateUtil.MILLSECONDS_ONE_DAY;
                    if (needUseRepeatInterval) {//跨周日的情况，例如今天周4，下次响铃是周1，中间需要计算周间隔
                        nextAlarmTime += (repeatInterval * 7  * DateUtil.MILLSECONDS_ONE_DAY);
                    }
                }
                break;
            case AlarmRule.REPEAT_TYPE_MONTH:
                if(nextAlarmTime >= beginTime) {
                    calendar.setTimeInMillis(beginTime);
                    calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+repeatInterval);
                    nextAlarmTime = calendar.getTimeInMillis();
                //    nextAlarmTime += (calcDaysByMonth(calendar, repeatInterval) * DateUtil.MILLSECONDS_ONE_DAY);
                }else{
                    nextAlarmTime = beginTime;
                }
                break;
            case AlarmRule.REPEAT_TYPE_YEAR:
                if(nextAlarmTime >= beginTime) {
                    calendar.setTimeInMillis(beginTime);
                    calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)+repeatInterval);
                    nextAlarmTime = calendar.getTimeInMillis();
                }else{
                    nextAlarmTime = beginTime;
                }
                break;
        }

        return nextAlarmTime;
    }

    public Alarm getNextAlarm(long currentTime) throws JSONException {
        JSONObject ruleJson = new JSONObject(ruleContent);
        Alarm nextAlarm = null;
        boolean repeated = ruleJson.getBoolean(AlarmRule.PARAM_BOOL_REPEAT);
        if (repeated) {
            int end_type = ruleJson.getInt(AlarmRule.PARAM_INT_END_TYPE);
            long nextAlarmTime = calcNextAlarmTime(beginTime);
            LogUtil.d(TAG, String.format(Locale.CHINA, "下个时闹钟时间:%s", new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA).format(new Date(nextAlarmTime))));
            switch (end_type) {
                case AlarmRule.TYPE_END_NONE://一直提醒
                    nextAlarm = new Alarm(this);
                    nextAlarm.setAlarmTime(nextAlarmTime);
                    break;
                case AlarmRule.TYPE_END_TIME://按过期时间结束闹钟
                    long outOfTime = ruleJson.getLong(AlarmRule.PARAM_LONG_OUT_OF_TIME);
                    if (nextAlarmTime < outOfTime) {
                        nextAlarm = new Alarm(this);
                        nextAlarm.setAlarmTime(nextAlarmTime);
                    }
                    break;
                case AlarmRule.TYPE_END_COUNT://按次数结束闹钟
                    int maxCount = ruleJson.getInt(AlarmRule.PARAM_INT_MAX_COUNT);
                    int repeatedCount = ruleJson.getInt(AlarmRule.PARAM_INT_REPEATED_COUNT);
                    if (++repeatedCount < maxCount) {//下个闹钟的重复次数大于最大次数，将闹钟规则设为过期状态
                        nextAlarm = new Alarm(this);
                        nextAlarm.setAlarmTime(nextAlarmTime);
                    }
                    break;
            }
        } else {
            if (currentTime < beginTime) {
                nextAlarm = new Alarm(this);
                nextAlarm.setAlarmTime(beginTime);
            }
        }
        return nextAlarm;
    }


   /* *//**
     * 利用月数间隔计算下个闹钟的时间
     *
     * @param calendar   日历对象
     * @param monthCount 两个闹钟之间间隔的月数
     * @return
     *//*
    private int calcDaysByMonth(Calendar calendar, int monthCount) {
        int allDayInterval = 0;

        int currentMonth = calendar.get(Calendar.MONTH);
        int nextMonth;
        int year = calendar.get(Calendar.YEAR);
        for (int i = 1; i <= monthCount; i++) {
            nextMonth = currentMonth + monthCount;
            if (nextMonth > 12) {
                nextMonth -= 12;
                year++;
            }
            if (nextMonth == 4 || nextMonth == 6 || nextMonth == 9 || nextMonth == 11) {
                allDayInterval += 30;
            } else if (nextMonth == 2) {
                if (DateUtil.isLeapYear(year)) {
                    allDayInterval += 29;
                } else {
                    allDayInterval += 28;
                }
            } else {
                allDayInterval += 31;
            }
        }
        return allDayInterval;
    }*/

}
