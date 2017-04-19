package com.example.basemoudle.util;


import com.orhanobut.logger.Logger;

/**
 * Created by chenlei2 on 2017/4/18 0018.
 */

public class LogUtil {
    private static final String TAG = LogUtil.class.getSimpleName();
    public static void i(String content){
        Logger.t(TAG).i(content);
    }
    public static void i(String tag,String content){
        Logger.t(tag).i(content);
    }
    public static void d(String content){
        Logger.t(TAG).d(content);
    }
    public static void d(String tag,String content){
        Logger.t(TAG).d(content);
    }
    public static void e(String content){
        Logger.t(TAG).e(content);
    }
    public static void e(String tag,String content){
        Logger.t(TAG).e(content);
    }
    public static void w(String content){
        Logger.t(TAG).w(content);
    }
    public static void w(String tag,String content){
        Logger.t(TAG).w(content);
    }
}
