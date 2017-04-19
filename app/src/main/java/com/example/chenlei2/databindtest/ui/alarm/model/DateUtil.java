package com.example.chenlei2.databindtest.ui.alarm.model;

/**
 * Created by chenlei2 on 2017/4/12 0012.
 */

public class DateUtil {

    public static final long MILLSECONDS_ONE_DAY = 24 * 60 * 60 * 1000;
    public static boolean isLeapYear(int year)
    {

        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ;
    }
}
