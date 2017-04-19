package com.example.chenlei2.databindtest.model.calcuteTime;


import com.example.basemoudle.util.LogUtil;

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
        LogUtil.i("beginTime："+beginTime+",endTime:"+endTime+"executeTime:"+executeTime);
    }
}
