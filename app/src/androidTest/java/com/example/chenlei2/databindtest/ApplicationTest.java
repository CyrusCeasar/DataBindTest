package com.example.chenlei2.databindtest;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.basemoudle.util.LogUtil;
import com.example.chenlei2.databindtest.model.CronExpression;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest  {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        try {
            CronExpression cronExpression = new CronExpression("0 10 12 ? * MON/5 2017");
            LogUtil.d(new SimpleDateFormat("yyyy/MM/dd hh:mm").format(cronExpression.getNextValidTimeAfter(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}