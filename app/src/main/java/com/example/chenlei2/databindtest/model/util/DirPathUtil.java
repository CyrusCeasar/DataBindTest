package com.example.chenlei2.databindtest.model.util;

import android.content.Context;
import android.os.Environment;

import com.example.basemoudle.util.LogUtil;
import com.example.chenlei2.databindtest.CyrusApplication;

/**
 * Created by chenlei2 on 2016/9/8 0008.
 */
public class DirPathUtil {


    public static void logAllPath(Context Context){
        LogUtil.i(Environment.getDataDirectory().getPath());                  //        获得根目录/data 内部存储路径

        LogUtil.i(Environment.getDownloadCacheDirectory().getPath());    //  获得缓存目录/cache

        LogUtil.i(Environment.getExternalStorageDirectory().getPath());     //    获得SD卡目录/mnt/sdcard（获取的是手机外置sd卡的路径）

        LogUtil.i(Environment.getRootDirectory().getPath());                   //       获得系统目录/system



        LogUtil.i(Context.getDatabasePath(CyrusApplication.DB_NAME).toString());                                         //        返回通过Context.openOrCreateDatabase 创建的数据库文件
        LogUtil.i(Context.getCacheDir().getPath());                                 //         用于获取APP的cache目录 /data/data/<application package>/cache目录

//        LogUtil.i(Context.getExternalCacheDir().getPath());                     //       用于获取APP的在SD卡中的cache目录/mnt/sdcard/Android/data/<application package>/cache

        LogUtil.i(Context.getFilesDir().getPath());                                   //        用于获取APP的files目录 /data/data/<application package>/files

        LogUtil.i(Context.getObbDir().getPath());                                     //        用于获取APPSDK中的obb目录 /mnt/sdcard/Android/obb/<application package>

        LogUtil.i(Context.getPackageName());                                         //       用于获取APP的所在包目录

        LogUtil.i(Context.getPackageCodePath());                                     //    来获得当前应用程序对应的 apk 文件的路径

        LogUtil.i(Context.getPackageResourcePath());                               //     获取该程序的安装包路径
    }
}
