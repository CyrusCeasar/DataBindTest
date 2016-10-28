package com.example.basemoudle.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by chenlei2 on 2016/9/29 0029.
 */

public class PkgUtil {
    public int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();//context为当前Activity上下文
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

    }

    public String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();//context为当前Activity上下文
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
