package com.example.chenlei2.databindtest.model.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

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

}
