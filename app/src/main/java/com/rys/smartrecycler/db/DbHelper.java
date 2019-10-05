package com.rys.smartrecycler.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.base.DataBaseContext;
import com.rys.smartrecycler.db.base.MyDevOpenHelper;
import com.rys.smartrecycler.db.greendao.DaoMaster;
import com.rys.smartrecycler.db.greendao.DaoSession;

/**
 * 创作时间： 2019/5/7 on 下午8:15
 * <p>
 * 描述：数据库操作通用类
 * <p>
 * 作者：lwb
 */

public class DbHelper {
    private static DbHelper instance;
    private static String   dbName = "smartcyc.db";
    private MyDevOpenHelper myDevOpenHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    public DbHelper (Context mContext){
        myDevOpenHelper = new MyDevOpenHelper(new DataBaseContext(mContext),dbName,null);
        daoMaster = new DaoMaster(myDevOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }
    public static DbHelper getInstance(){
        if(null == instance){
            synchronized(DbHelper.class){
                if(null == instance){
                    instance = new DbHelper(BaseApplication.getInstance().getApplicationContext());
                }
            }
        }
        return instance;
    }
    public static DbHelper getInstance(Context mContext){
        if(null == instance){
            synchronized(DbHelper.class){
                if(null == instance){
                    instance = new DbHelper(mContext);
                }
            }
        }
        return instance;
    }
    /**
     *
     * @return
     */
    public DaoSession getDaoSession(){
        return daoSession;
    }

    /**
     *
     * @return
     */
    public DaoMaster getDaoMaster(){
        return daoMaster;
    }

    /**
     *
     * @return
     */
    public SQLiteDatabase getReadableDatabase(){
        return myDevOpenHelper.getReadableDatabase();
    }

    /**
     *
     * @return
     */
    public SQLiteDatabase getWriteableDatabase(){
        return myDevOpenHelper.getWritableDatabase();
    }

}
