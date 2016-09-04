package com.example.chenlei2.databindtest;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;

import com.example.chenlei2.databindtest.model.FileManager;
import com.example.chenlei2.databindtest.model.calcuteTime.Calcuter;
import com.example.chenlei2.databindtest.model.calcuteTime.Method;
import com.example.chenlei2.databindtest.model.db.Alarm;
import com.example.chenlei2.databindtest.model.db.Directory;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MMediaFile;
import com.example.chenlei2.databindtest.model.util.DbManager;
import com.example.chenlei2.databindtest.model.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei2 on 2016/9/2 0002.
 */
public class CyrucApplication extends Application{

    public static final String DB_NAME = "cyrus.db";

    @Override
    public void onCreate() {
        super.onCreate();


        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                new Calcuter(new Method() {
                    @Override
                    public void execute() {
                        List<String>  tables = new ArrayList<String>();
                        tables.add( Alarm.class.getName());
                        tables.add( Directory.class.getName());
                        tables.add( MFile.class.getName());
                        tables.add( MMediaFile.class.getName());
                        DbManager.getInstance().addOrmHelper(getApplicationContext(),tables,DB_NAME,1);
                    }
                }).execute();

//                FileManager.getInstance().searchFilePath(Environment.getExternalStorageDirectory().getAbsolutePath());
            }
        });
        startService(new Intent(this,ServMusicPlayer.class));
    }
}
