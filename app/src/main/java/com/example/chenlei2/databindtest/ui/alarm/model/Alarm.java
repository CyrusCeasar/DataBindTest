package com.example.chenlei2.databindtest.ui.alarm.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.chenlei2.databindtest.ui.alarm.model.Alarm.TABLE_NAME;


/**
 * Created by chenlei2 on 2017/4/10 0010.
 */
@DatabaseTable(tableName = TABLE_NAME)
public class Alarm implements Parcelable {

    public static final byte STATE_UNREMIND = 0;
    public static final byte STATE_ALERTING = 1;
    public static final byte STATE_UNHANDLED = 2;
    public static final byte STATE_HANDLED = 3;

    public static final String TABLE_NAME = "tb_alarm";
    public static final String FILED_ALARMRULE_ID="alarmRule_id";
    public static final String FILED_STATE ="state";



    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(foreign = true ,foreignAutoRefresh = true,columnName = FILED_ALARMRULE_ID)
    private AlarmRule alarmRule;
    @DatabaseField(canBeNull = false,defaultValue = "0",columnName = FILED_STATE)
    private byte state;
    @DatabaseField(canBeNull = false)
    private long alarmTime;

    public Alarm(){

    }


    public Alarm(AlarmRule alarmRule){
        this.alarmRule = alarmRule;
        this.alarmTime = alarmRule.getBeginTime();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AlarmRule getAlarmRule() {
        return alarmRule;
    }

    public void setAlarmRule(AlarmRule alarmRule) {
        this.alarmRule = alarmRule;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }


    public static boolean addAlarmClock(Context context, Alarm alarm){

        return false;
    }

    public static boolean removeClock(Context context, Alarm alarm){
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeParcelable(this.alarmRule, flags);
        dest.writeByte(this.state);
        dest.writeLong(this.alarmTime);
    }

    protected Alarm(Parcel in) {
        this.id = in.readLong();
        this.alarmRule = in.readParcelable(AlarmRule.class.getClassLoader());
        this.state = in.readByte();
        this.alarmTime = in.readLong();
    }

    public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel source) {
            return new Alarm(source);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public String toString() {
        return String.format(Locale.CHINA,"alarm:%d  %s",id,new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA).format(new Date(alarmTime)));
    }


}
