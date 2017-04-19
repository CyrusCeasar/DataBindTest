package com.example.chenlei2.databindtest.model;



import com.example.basemoudle.util.DbManager;
import com.example.basemoudle.util.DbOrmHelper;
import com.example.chenlei2.databindtest.CyrusApplication;
import com.example.chenlei2.databindtest.model.db.Directory;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MMediaFile;

import java.io.File;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
public class FileManager {
    private static FileManager ourInstance = new FileManager();
    private final DbOrmHelper dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrusApplication.DB_NAME);
    public static FileManager getInstance() {
        return ourInstance;
    }
    private FileManager() {
    }

    public void searchFilePath(String filePath){
        File file = new File(filePath);
        if(file.isDirectory()){
            Directory directory = new Directory();
            directory.setName(file.getName());
            directory.setPath(file.getPath());
            directory.setSize(file.length());
            directory.setType(MFile.TYPE.dir);
            dbOrmHelper.createOrUpdate(directory,Directory.class);
            if(file.exists()) {
                if(file.canRead()) {
                    File[] fileList = file.listFiles();
                    if (fileList != null && fileList.length != 0) {
                        for (File mfile : fileList) {
                            searchFilePath(mfile.getAbsolutePath());
                        }
                    }
                }
            }
        }else {
            MFile mfile = new MMediaFile();
            mfile.setName(file.getName());
            mfile.setPath(filePath);
            mfile.setSize(file.length());
            if(MediaFile.isAudioFileType(filePath)){
//                LogUtil.i("添加文件"+filePath);
                mfile.setType(MFile.TYPE.audio);
//                audioFiles.add(filePath);
                dbOrmHelper.createOrUpdate(mfile, MMediaFile.class);
            }else if (MediaFile.isVideoFileType(filePath)){
//                LogUtil.i("添加文件"+filePath);
//                videoFiles.add(filePath);
                mfile.setType(MFile.TYPE.video);
                dbOrmHelper.createOrUpdate(mfile, MMediaFile.class);
            }else if(MediaFile.isImgFileType(filePath)){
//                imgFiles.add(filePath);
                mfile.setType(MFile.TYPE.img);
                dbOrmHelper.createOrUpdate(mfile, MMediaFile.class);
//                LogUtil.i("添加文件"+filePath);
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
