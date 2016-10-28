package com.example.chenlei2.databindtest.model.db.dao;


/**
 * Created by chenlei2 on 2016/9/2 0002.
 */
public class FileService {


  /*  public List<MFile> getFiles(TYPE fileType){
        List<MFile> mFiles;
        DbOrmHelper dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrusApplication.DB_NAME);

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
