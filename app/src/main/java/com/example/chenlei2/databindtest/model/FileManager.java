package com.example.chenlei2.databindtest.model;



import com.example.chenlei2.databindtest.CyrucApplication;
import com.example.chenlei2.databindtest.model.db.Directory;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MMediaFile;
import com.example.chenlei2.databindtest.model.util.DbManager;
import com.example.chenlei2.databindtest.model.util.DbOrmHelper;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
public class FileManager {
    private static FileManager ourInstance = new FileManager();


    private final DbOrmHelper dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrucApplication.DB_NAME);

    public static FileManager getInstance() {
        return ourInstance;
    }

    private FileManager() {
    }

 /*   final LinkedList<String> audioFiles =new LinkedList<>();
    final LinkedList<String> videoFiles = new LinkedList<>();
    final LinkedList<String> imgFiles = new LinkedList<>();*/

    public void searchFilePath(String filePath){
        File file = new File(filePath);
        if(file.isDirectory()){
            Directory directory = new Directory();
            directory.setName(file.getName());
            directory.setPath(file.getPath());
            directory.setSize(file.length());
            directory.setType(MFile.TYPE.dir);
            dbOrmHelper.createOrUpdate(directory,Directory.class);
            File[] fileList = file.listFiles();
            for(File mfile :fileList){
//                LogUtils.i("查询目录"+mfile.getAbsolutePath());
                searchFilePath(mfile.getAbsolutePath());
            }
        }else {

            MFile mfile = new MMediaFile();
            mfile.setName(file.getName());
            mfile.setPath(filePath);
            mfile.setSize(file.length());
            if(MediaFile.isAudioFileType(filePath)){
//                LogUtils.i("添加文件"+filePath);
                mfile.setType(MFile.TYPE.audio);
//                audioFiles.add(filePath);
                dbOrmHelper.createOrUpdate(mfile, MMediaFile.class);
            }else if (MediaFile.isVideoFileType(filePath)){
//                LogUtils.i("添加文件"+filePath);
//                videoFiles.add(filePath);
                mfile.setType(MFile.TYPE.video);
                dbOrmHelper.createOrUpdate(mfile, MMediaFile.class);
            }else if(MediaFile.isImgFileType(filePath)){
//                imgFiles.add(filePath);
                mfile.setType(MFile.TYPE.img);
                dbOrmHelper.createOrUpdate(mfile, MMediaFile.class);
//                LogUtils.i("添加文件"+filePath);
            }else {
                mfile = new MFile();
                mfile.setName(file.getName());
                mfile.setPath(filePath);
                mfile.setSize(file.length());
                mfile.setType(MFile.TYPE.others);
                dbOrmHelper.createOrUpdate(mfile, MFile.class);
            }


        }
    }

  /*  public LinkedList<String> getAudioFiles() {
        return audioFiles;
    }

    public LinkedList<String> getVideoFiles() {
        return videoFiles;
    }

    public LinkedList<String> getImgFiles() {
        return imgFiles;
    }*/
}
