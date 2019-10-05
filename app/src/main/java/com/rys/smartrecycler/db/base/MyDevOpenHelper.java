package com.rys.smartrecycler.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rys.smartrecycler.db.greendao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * 创作时间： 2019/5/6 on 下午9:51
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class MyDevOpenHelper extends DaoMaster.OpenHelper {
    public MyDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //操作数据库的更新 有几个表升级都可以传入到下面
//        MigrationHelper.migrate(db,UserOrderDao.class);


    }
}
