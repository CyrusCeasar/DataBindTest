package com.example.basemoudle.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apkfuns.logutils.LogUtils;

import java.util.List;
import java.util.TreeMap;

public class DbManager {


    private static DbManager instance = new DbManager();


    public static DbManager getInstance() {
        return instance;
    }

    public DbOrmHelper getOrmHelper(String dbName) {
        return ormHelpers.get(dbName);
    }

    public void addOrmHelper(Context context, List<String> tables, String dbName, int dbVersion) {
        if (!ormHelpers.containsKey(dbName)) {
            ormHelpers.put(dbName, new DbOrmHelper(context, tables, dbName, dbVersion));
        }
    }


    private final TreeMap<String, DbOrmHelper> ormHelpers = new TreeMap<>();


    public void printAllData(String dbName) {

        DbOrmHelper dbOrmHelper = ormHelpers.get(dbName);
        Cursor dbCursor = dbOrmHelper.getReadableDatabase().rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(dbCursor.moveToNext()){
            //遍历出表名
            String name = dbCursor.getString(0);
            Cursor tableCursor = dbOrmHelper.getReadableDatabase().rawQuery("select * from "+name,null);
            StringBuilder sb = new StringBuilder();
            String[] columnNames = tableCursor.getColumnNames();
            for(String string :columnNames){
                sb.append(string);
                sb.append(",");
            }
            LogUtils.i(sb.toString());
            while(tableCursor.moveToNext()){
                sb = new StringBuilder();
                for(int i = 0 ; i<tableCursor.getColumnCount();i++){
                    sb.append(tableCursor.getString(i));
                    sb.append(",");
                }
                LogUtils.i(sb.toString());
            }
            tableCursor.close();
        }
        dbCursor.close();

    }
}
