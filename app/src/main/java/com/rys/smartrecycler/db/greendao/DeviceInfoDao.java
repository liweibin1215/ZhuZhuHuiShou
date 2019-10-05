package com.rys.smartrecycler.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.rys.smartrecycler.db.retbean.DeviceInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DEVICE_INFO".
*/
public class DeviceInfoDao extends AbstractDao<DeviceInfo, Long> {

    public static final String TABLENAME = "DEVICE_INFO";

    /**
     * Properties of entity DeviceInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DeviceSn = new Property(1, String.class, "deviceSn", false, "DEVICE_SN");
        public final static Property DevicePwd = new Property(2, String.class, "devicePwd", false, "DEVICE_PWD");
        public final static Property DeviceName = new Property(3, String.class, "deviceName", false, "DEVICE_NAME");
        public final static Property AdminPwd = new Property(4, String.class, "adminPwd", false, "ADMIN_PWD");
        public final static Property KeFuPhone = new Property(5, String.class, "keFuPhone", false, "KE_FU_PHONE");
        public final static Property Province = new Property(6, String.class, "province", false, "PROVINCE");
        public final static Property City = new Property(7, String.class, "city", false, "CITY");
        public final static Property Area = new Property(8, String.class, "area", false, "AREA");
        public final static Property Adress = new Property(9, String.class, "adress", false, "ADRESS");
        public final static Property Create_at = new Property(10, String.class, "create_at", false, "CREATE_AT");
        public final static Property ProductKey = new Property(11, String.class, "ProductKey", false, "PRODUCT_KEY");
        public final static Property DeviceSecret = new Property(12, String.class, "DeviceSecret", false, "DEVICE_SECRET");
    }


    public DeviceInfoDao(DaoConfig config) {
        super(config);
    }
    
    public DeviceInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DEVICE_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DEVICE_SN\" TEXT," + // 1: deviceSn
                "\"DEVICE_PWD\" TEXT," + // 2: devicePwd
                "\"DEVICE_NAME\" TEXT," + // 3: deviceName
                "\"ADMIN_PWD\" TEXT," + // 4: adminPwd
                "\"KE_FU_PHONE\" TEXT," + // 5: keFuPhone
                "\"PROVINCE\" TEXT," + // 6: province
                "\"CITY\" TEXT," + // 7: city
                "\"AREA\" TEXT," + // 8: area
                "\"ADRESS\" TEXT," + // 9: adress
                "\"CREATE_AT\" TEXT," + // 10: create_at
                "\"PRODUCT_KEY\" TEXT," + // 11: ProductKey
                "\"DEVICE_SECRET\" TEXT);"); // 12: DeviceSecret
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DEVICE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DeviceInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceSn = entity.getDeviceSn();
        if (deviceSn != null) {
            stmt.bindString(2, deviceSn);
        }
 
        String devicePwd = entity.getDevicePwd();
        if (devicePwd != null) {
            stmt.bindString(3, devicePwd);
        }
 
        String deviceName = entity.getDeviceName();
        if (deviceName != null) {
            stmt.bindString(4, deviceName);
        }
 
        String adminPwd = entity.getAdminPwd();
        if (adminPwd != null) {
            stmt.bindString(5, adminPwd);
        }
 
        String keFuPhone = entity.getKeFuPhone();
        if (keFuPhone != null) {
            stmt.bindString(6, keFuPhone);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(7, province);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(8, city);
        }
 
        String area = entity.getArea();
        if (area != null) {
            stmt.bindString(9, area);
        }
 
        String adress = entity.getAdress();
        if (adress != null) {
            stmt.bindString(10, adress);
        }
 
        String create_at = entity.getCreate_at();
        if (create_at != null) {
            stmt.bindString(11, create_at);
        }
 
        String ProductKey = entity.getProductKey();
        if (ProductKey != null) {
            stmt.bindString(12, ProductKey);
        }
 
        String DeviceSecret = entity.getDeviceSecret();
        if (DeviceSecret != null) {
            stmt.bindString(13, DeviceSecret);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DeviceInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceSn = entity.getDeviceSn();
        if (deviceSn != null) {
            stmt.bindString(2, deviceSn);
        }
 
        String devicePwd = entity.getDevicePwd();
        if (devicePwd != null) {
            stmt.bindString(3, devicePwd);
        }
 
        String deviceName = entity.getDeviceName();
        if (deviceName != null) {
            stmt.bindString(4, deviceName);
        }
 
        String adminPwd = entity.getAdminPwd();
        if (adminPwd != null) {
            stmt.bindString(5, adminPwd);
        }
 
        String keFuPhone = entity.getKeFuPhone();
        if (keFuPhone != null) {
            stmt.bindString(6, keFuPhone);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(7, province);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(8, city);
        }
 
        String area = entity.getArea();
        if (area != null) {
            stmt.bindString(9, area);
        }
 
        String adress = entity.getAdress();
        if (adress != null) {
            stmt.bindString(10, adress);
        }
 
        String create_at = entity.getCreate_at();
        if (create_at != null) {
            stmt.bindString(11, create_at);
        }
 
        String ProductKey = entity.getProductKey();
        if (ProductKey != null) {
            stmt.bindString(12, ProductKey);
        }
 
        String DeviceSecret = entity.getDeviceSecret();
        if (DeviceSecret != null) {
            stmt.bindString(13, DeviceSecret);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DeviceInfo readEntity(Cursor cursor, int offset) {
        DeviceInfo entity = new DeviceInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // deviceSn
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // devicePwd
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // deviceName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // adminPwd
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // keFuPhone
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // province
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // city
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // area
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // adress
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // create_at
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // ProductKey
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // DeviceSecret
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DeviceInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeviceSn(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDevicePwd(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDeviceName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAdminPwd(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setKeFuPhone(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setProvince(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCity(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setArea(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setAdress(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCreate_at(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setProductKey(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setDeviceSecret(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DeviceInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DeviceInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DeviceInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
