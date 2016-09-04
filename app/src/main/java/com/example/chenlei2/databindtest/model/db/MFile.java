package com.example.chenlei2.databindtest.model.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
@DatabaseTable
public class MFile implements Serializable{

    public enum TYPE{
        dir,audio,video,img,others
    }



    @DatabaseField(generatedId = true)
    protected int id;

    @DatabaseField(canBeNull = false)
    protected String name;

    @DatabaseField(canBeNull = false)
    protected TYPE type;


    @DatabaseField(canBeNull = false,unique = true)
    protected String path;

    @DatabaseField
    protected String md5;

    @DatabaseField
    protected long size;

    @DatabaseField( foreign = true)
    protected Directory parentDir;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public void setParentDir(Directory parentDir) {
        this.parentDir = parentDir;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
