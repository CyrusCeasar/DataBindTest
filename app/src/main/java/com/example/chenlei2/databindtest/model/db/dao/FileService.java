package com.example.chenlei2.databindtest.model.db.dao;

import com.example.chenlei2.databindtest.CyrucApplication;
import com.example.chenlei2.databindtest.model.MediaFile;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MFile.TYPE;
import com.example.chenlei2.databindtest.model.db.MMediaFile;
import com.example.chenlei2.databindtest.model.util.DbManager;
import com.example.chenlei2.databindtest.model.util.DbOrmHelper;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by chenlei2 on 2016/9/2 0002.
 */
public class FileService {


  /*  public List<MFile> getFiles(TYPE fileType){
        List<MFile> mFiles;
        DbOrmHelper dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrucApplication.DB_NAME);

        switch (fileType){
            case audio:
                try {
                    mFiles = (List<MFile>)dbOrmHelper.getDaoEx(MMediaFile.class).queryBuilder().where().eq("type",fileType).query();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case video:
                break;
            case img:
                break;
            default:
                break;
        }
    }*/
}
