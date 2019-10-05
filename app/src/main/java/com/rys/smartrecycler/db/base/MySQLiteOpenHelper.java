package com.rys.smartrecycler.db.base;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 创作时间： 2019/5/6 on 下午9:51
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static MySQLiteOpenHelper instance;

    private MySQLiteOpenHelper(Context context) {
        super(new DataBaseContext(context), "diyicourier.db", null, DATABASE_VERSION);
    }

    public static synchronized MySQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySQLiteOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(DBCommonValue.SOCKET_TEST_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 1) {

        }


    }

    /**
     * 特殊版本下，清空所有表数据
     *
     * @param db
     */
    public void dropAllTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS " + "user";
        db.execSQL(sql);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
            }
            instance = null;
        }
    }
}
