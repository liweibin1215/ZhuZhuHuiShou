/**
 *
 */
package com.rys.smartrecycler.db.base;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.io.File;
import java.io.IOException;

/**
 * 创建时间：2018/1/22
 * 作者：李伟斌
 * 功能描述:用于支持对存储在SD卡上的数据库的访问
 */

public class DataBaseContext extends ContextWrapper {

    /**
     * 构造函数
     * @param    base 上下文环境
     */
    public DataBaseContext(Context base){
        super(base);
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     * @param    name
     */
    @Override
    public File getDatabasePath(String name) {
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if(!sdExist){
            String dbDir=android.os.Environment.getExternalStorageDirectory().toString();
            dbDir += "/SmartRecycler/data/data/lib";//数据库所在目录
            String dbPath = dbDir+"/"+name;//数据库路径
            File dbFile = new File(dbPath);
            return dbFile;
        }else{
            String dbDir=android.os.Environment.getExternalStorageDirectory().toString();
            dbDir += "/SmartRecycler/data/data/lib";//数据库所在目录
            String dbPath = dbDir+"/"+name;//数据库路径
            File dirFile = new File(dbDir);
            if(!dirFile.exists())
                dirFile.mkdirs();
            boolean isFileCreateSuccess = false;
            File dbFile = new File(dbPath);
            if(!dbFile.exists()){
                try {
                    isFileCreateSuccess = dbFile.createNewFile();//创建文件
                } catch (IOException e) {
                }
            }
            else
                isFileCreateSuccess = true;
            if(isFileCreateSuccess)
                return dbFile;
            else
                return dbFile;
        }
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param    name
     * @param    mode
     * @param    factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               CursorFactory factory) {
        //super.openOrCreateDatabase(name, mode, factory);//此函数生成数据库到系统目录下
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @see ContextWrapper#openOrCreateDatabase(String, int,
     *              CursorFactory,
     *              DatabaseErrorHandler)
     * @param    name
     * @param    mode
     * @param    factory
     * @param     errorHandler
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {

        if(getDatabasePath(name) != null){
            SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
            return result;
        }else{
            return super.openOrCreateDatabase(name, mode, factory);
        }
    }
}
