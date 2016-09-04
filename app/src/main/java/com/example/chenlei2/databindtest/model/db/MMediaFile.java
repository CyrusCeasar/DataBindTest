package com.example.chenlei2.databindtest.model.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
@DatabaseTable
public class MMediaFile extends MFile implements Serializable{

    @DatabaseField
    protected long timeLong;


    public long getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(long timeLong) {
        this.timeLong = timeLong;
    }
}
