
package com.example.chenlei2.databindtest.model.util;

import android.content.Context;

import java.util.List;
import java.util.TreeMap;

public class DbManager {


    private static DbManager instance = new DbManager();


    public static DbManager getInstance(){
        return  instance;
    }

    public DbOrmHelper getOrmHelper(String dbName){
        return  ormHelpers.get(dbName);
    }

    public void addOrmHelper(Context context, List<String> tables, String dbName, int dbVersion){
        if(!ormHelpers.containsKey(dbName)) {
            ormHelpers.put(dbName, new DbOrmHelper(context, tables, dbName, dbVersion));
        }
    }


    private final TreeMap<String,DbOrmHelper> ormHelpers = new TreeMap<>();






}
