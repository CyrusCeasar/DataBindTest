package com.example.chenlei2.databindtest.model.db;

import android.databinding.BindingConversion;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
@DatabaseTable
public class Alarm implements Serializable{


    @DatabaseField(generatedId = true)
    private int alarmId;

    @DatabaseField(canBeNull =   false)
    private long clockTime;

    @DatabaseField(canBeNull =   false)
    private long interval;


    public long getClockTime() {
        return clockTime;
    }

    public void setClockTime(long clockTime) {
        this.clockTime = clockTime;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getAlarmId() {
        return alarmId;
    }

    @BindingConversion
    public static String bindConverToDateString(long clockTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return  simpleDateFormat.format(new Date(clockTime));
    }
}
