package com.example.chenlei2.databindtest.model.calcuteTime;

import com.apkfuns.logutils.LogUtils;

import java.text.SimpleDateFormat;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
public class Calcuter implements  Method{

    private Method method;

    public Calcuter(Method method){
        this.method = method;
    }

    @Override
    public void execute() {
        long beginTime,endTime;
        beginTime = System.currentTimeMillis();
        method.execute();
        endTime = System.currentTimeMillis();

        long executeTime = endTime - beginTime;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        LogUtils.i("beginTime："+beginTime+",endTime:"+endTime+"executeTime:"+executeTime);

    }
}
