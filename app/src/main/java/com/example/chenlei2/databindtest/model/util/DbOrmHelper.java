package com.example.chenlei2.databindtest.model.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.apkfuns.logutils.LogUtils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.db.SqliteAndroidDatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlei2 on 2016/9/2 0002.
 */
public class DbOrmHelper extends OrmLiteSqliteOpenHelper {
    private static DbManager databaseHelper;
    private final Class<?>[] classes;
    private final Map<Class<?>, Dao<?, Long>> daos = new HashMap<Class<?>, Dao<?, Long>>();
    private final Map<Class<?>, DatabaseTableConfig<?>> tableConfigs = new HashMap<Class<?>, DatabaseTableConfig<?>>();



     DbOrmHelper(Context context, List<String> tables, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        Class<?>[] classes = null;
        if (tables != null && !tables.isEmpty()) {
            classes = new Class[tables.size()];
            for (int i = 0; i < tables.size(); i++) {
                try {
                    classes[i] = Class.forName(tables.get(i));
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        this.classes = classes;
    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        LogUtils.i("Creating database");
        createTables(db, cs);

        // 数据库创建时自动执行,以后不会再自动执行
        // try {
        // TableUtils.createTable(connectionSource, Device.class); // 创建Device表
        // TableUtils.createTable(connectionSource, Setting.class); //
        // 创建Setting表
        //
        // initData();
        // LogUtils.i(DbManager.class.getName(), "创建数据库成功！");
        // } catch (Exception e) {
        // LogUtils.i(DbManager.class.getName(), "创建数据库失败！", e);
        // e.printStackTrace();
        // }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs,
                          int oldVersion, int newVersion) {
        LogUtils.i("Upgrading database from version " + oldVersion + " to "
                + newVersion);
        int upgradeVersion = oldVersion;

        //假如表结构发生很大变化，要删除别的表
        try {
            db.beginTransaction();

            if (upgradeVersion == 1) {
//            renameTable();     1、重命名老表
//            createNewTable();  2、创建新表
//            copyData();        3、复制数据
//            deleteOldTables(); 4、删除老表
//                LogUtils.i("upgrade","upgrade:start");
//                String renameSql =  "ALTER TABLE account RENAME TO account_temp";
//
//                LogUtils.i("upgrade","upgrade:" + renameSql);
//                db.execSQL(renameSql);
//                LogUtils.i("upgrade","upgrade:db.execSQL(renameSql);");
//
//                try {
//                    TableUtils.createTableIfNotExists(cs, Account.class);
//                    LogUtils.i("upgrade","upgrade createTableIfNotExists(cs, Account.class);");
//                } catch (java.sql.SQLException e) {
//                    e.printStackTrace();
//                    LogUtils.i("upgrade","upgrade java.sql.SQLException e");
//                }
//
//
//                String copyDataSql = "insert into account(accountId,nickName,iconUrl) Select accountId,nickName,iconUrl from account_temp";
//
//                LogUtils.i("upgrade","upgrade: " + copyDataSql);
//                db.execSQL(copyDataSql);
//                LogUtils.i("upgrade","upgrade: db.execSQL(copyDataSql)");
//                String delTableSql = "DROP TABLE account_temp";
//                LogUtils.i("upgrade","upgrade: " + delTableSql);
//                db.execSQL(delTableSql);
//                LogUtils.i("upgrade","upgrade:  db.execSQL(delTableSql);");

                createTables(db, cs);

                db.setTransactionSuccessful();
                upgradeVersion = 2;
            }
            //假如表结构知识增加或者删除一列
            if (upgradeVersion == 2) {

            }
        } catch (Exception e) {
            LogUtils.i("upgrade", "upgrade:  catch(Exception e)");
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    @Override
    public void close() {
        super.close();
        daos.clear();
        tableConfigs.clear();
    }


    public void addTable(Class<?> clazz) {
        createTable(clazz, getConnectionSource());
    }

    private void createTables(SQLiteDatabase db, ConnectionSource cs) {
        for (Class<?> clazz : classes) {
            createTable(clazz, cs);
        }
    }

    private void createTable(Class<?> clazz, ConnectionSource cs) {
        try {

            LogUtils.i(

                    "create table " + clazz.getSimpleName() + "---result:"
                            + TableUtils.createTableIfNotExists(cs, clazz));
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    public <T> Dao<T, Long> getDaoEx(Class<T> clazz) {
        Dao<T, Long> result = null;
        if (daos.containsKey(clazz)) {
            result = (Dao<T, Long>) daos.get(clazz);
        } else {
            try {
                result = getDao(clazz);
            } catch (java.sql.SQLException e) {
                throw new SQLException(e.getMessage());
            }
            daos.put(clazz, result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> DatabaseTableConfig<T> getTableConfig(Class<T> clazz) {
        DatabaseTableConfig<T> result = null;
        if (tableConfigs.containsKey(clazz)) {
            result = (DatabaseTableConfig<T>) tableConfigs.get(clazz);
        } else {
            try {
                result = DatabaseTableConfig.fromClass(getConnectionSource(),
                        clazz);
            } catch (java.sql.SQLException e) {
                throw new SQLException(e.getMessage());
            }
            tableConfigs.put(clazz, result);
        }
        return result;
    }

    public <T> String getTableName(Class<T> clazz) {
        DatabaseTableConfig<?> cfg = getTableConfig(clazz);
        return cfg.getTableName();
    }

    public <T> String[] getColumnNames(Class<T> clazz, boolean foreignOnly) {
        List<String> columnNames = new ArrayList<String>();
        try {
            DatabaseTableConfig<?> cfg = getTableConfig(clazz);
            SqliteAndroidDatabaseType dbType = new SqliteAndroidDatabaseType();
            for (FieldType fieldType : cfg.getFieldTypes(dbType)) {
                if (!foreignOnly || fieldType.isForeign()) {
                    columnNames.add(fieldType.getColumnName());
                }
            }
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return columnNames.toArray(new String[]{});
    }

    public <T> T queryById(long id, Class<T> clazz) {
        T entity = null;
        try {
            entity = getDaoEx(clazz).queryForId(id);
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return entity;
    }

    public <T> void deleteById(long id, Class<T> clazz) {
        try {
            int count = getDaoEx(clazz).deleteById(id);
            assert (count == 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }


    private void deleteTables(SQLiteDatabase db, ConnectionSource cs) {
        for (Class<?> clazz : classes) {
            dropTable(clazz, cs);
        }
    }


    private void dropTable(Class<?> clazz, ConnectionSource cs) {
        try {
            LogUtils.i(

                    "drop table " + clazz.getSimpleName() + "---result:"
                            + TableUtils.dropTable(cs, clazz, false));
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    private List<?> queryByParam(Class<?> class1, String columnName,
                                 String value) {
        try {
            return getDaoEx(class1).queryBuilder().where()
                    .eq(columnName, value).query();
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    public Object queryFirstByParam(Class<?> clazz, String columnName, String value) {
        try {
            return getDao(clazz).queryBuilder().where().eq(columnName, value).queryForFirst();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public List<?> queryForAll(Class<?> clazz) {
        try {
            return getDaoEx(clazz).queryForAll();
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<?> queryForRange(Class<?> clazz, long start, long rowCount) {
        try {
            return getDaoEx(clazz).queryBuilder().offset(start).limit(rowCount)
                    .query();
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<?> queryAllByColumnName(Class<?> clazz, String columnName,
                                        String value) {
        try {
            return getDaoEx(clazz).queryBuilder().where().eq(columnName, value)
                    .query();
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<?> queryForRangeByParam(Class<?> clazz, long start,
                                        long rowCount, String columnName, String value) {
        try {
            return getDaoEx(clazz).queryBuilder().offset(start).limit(rowCount)
                    .where().eq(columnName, value).query();
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public <T> void createOrUpdate(Object entity, Class<T> clazz) {
        try {
            getDaoEx(clazz).createOrUpdate(clazz.cast(entity));
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public <T> void updateColumn(Class<T> clazz, String where, Object whereValue, String eq,
                                 Object value) {
        try {
            getDaoEx(clazz).updateBuilder().updateColumnValue(eq, value).where()
                    .eq(where, whereValue);
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public <T> long create(Object entity, Class<T> clazz) {
        try {
            return getDaoEx(clazz).create(clazz.cast(entity));
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public <T> void update(Object entity, Class<T> clazz) {
        try {
            int count = getDaoEx(clazz).update(clazz.cast(entity));
            assert (count == 1);
        } catch (java.sql.SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public <T> int delete(Object entity, Class<T> clazz) {
        try {
            return getDaoEx(clazz).delete(clazz.cast(entity));
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据model删除表中数据
     *
     * @param clazz
     * @param <T>
     */
    public <T> void deleteTableData(Class<T> clazz) {
        LogUtils.i("DbManager", "clazz simplename:" + clazz.getSimpleName());
        String sqlCmd = "DELETE FROM " + clazz.getSimpleName();
        if (databaseHelper != null) {
            try {
                getDaoEx(clazz).executeRawNoArgs(sqlCmd);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        } else {
            LogUtils.e("DBHelper", "DBHelper: myDataBase is null");
        }

    }


}
