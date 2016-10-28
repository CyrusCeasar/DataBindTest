package com.example.basemoudle.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by chenlei2 on 2016/10/27 0027.
 */

public class IntentUtil {

    public static void startBrowser(Context context,String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        context.startActivity(intent);
    }

    public static void call(Context context,String number){
        Uri uri = Uri.parse("tel:"+number);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        context.startActivity(intent);
    }
    public static void playVideo(Context context,String path){
        Intent it = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(path);
        it.setDataAndType(uri, "audio/mp3");
        context.startActivity(it);
    }

    public static void installApk(Context context,String fileUri){
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(Uri.parse(fileUri),
                "application/vnd.android.package-archive");
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(installIntent);
    }

}
