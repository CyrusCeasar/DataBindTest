package com.example.chenlei2.databindtest;

import android.app.Application;

import com.example.basemoudle.util.DbManager;
import com.example.basemoudle.util.ThreadUtil;
import com.example.chenlei2.databindtest.model.FileManager;
import com.example.chenlei2.databindtest.model.calcuteTime.Calcuter;
import com.example.chenlei2.databindtest.model.db.Alarm;
import com.example.chenlei2.databindtest.model.db.Directory;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MMediaFile;
import com.example.chenlei2.databindtest.model.util.SdCardUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei2 on 2016/9/2 0002.
 */
public class CyrusApplication extends Application{

    public static final String DB_NAME = "cyrus.db";

    private static CyrusApplication instatnce;



    public static CyrusApplication getInstance(){
        return  instatnce;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instatnce = this;
        ThreadUtil.getInstance().execute(()->{
                new Calcuter(() ->{
                        List<String>  tables = new ArrayList<>();
                        tables.add( Alarm.class.getName());
                        tables.add( Directory.class.getName());
                        tables.add( MFile.class.getName());
                        tables.add( MMediaFile.class.getName());
                        DbManager.getInstance().addOrmHelper(getApplicationContext(),tables,DB_NAME,1);
                }).execute();
                List<MFile> files = (List<MFile>) DbManager.getInstance().getOrmHelper(DB_NAME).queryForAll(MFile.class);
                if(files != null && !files.isEmpty()) {
                    FileManager.getInstance().searchFilePath(SdCardUtil.getAllStorageLocations().get(SdCardUtil.SD_CARD).getAbsolutePath());
                }
                DbManager.getInstance().printAllData(CyrusApplication.DB_NAME);
        });


    }
}
