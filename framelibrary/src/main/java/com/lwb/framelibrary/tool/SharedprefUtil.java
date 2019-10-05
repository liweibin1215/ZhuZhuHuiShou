package com.lwb.framelibrary.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 手机应用的共享数
 * 
 * @author lwb
 */
public class SharedprefUtil {
    public static String CONFIG_CACHE ="defaultCache";

	/**
	 * 根据key移除共享
	 * 
	 * @param context
	 *            Context
	 * @param key
	 *            key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_CACHE, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 共享数据中存储数
	 * 
	 * @param context
	 *            Context
	 * @param key
	 *            key
	 * @param value
	 *            value
	 */
	public static void put(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_CACHE, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 共享数据中获取数
	 * 
	 * @param context
	 *            Context
	 * @param key
	 *            key
	 */
	public static String get(Context context, String key, String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_CACHE, Context.MODE_PRIVATE);
		return settings.getString(key, defaultValue);
	}

	/**
	 * 清楚共享数据
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_CACHE, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}

    /**
     *
     * @param context
     * @param key 存储键值
     * @param object 存储对象
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_CACHE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     *
     * @param context
     * @param key 存储键值
     * @param defaultObject 默认类型
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_CACHE, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return defaultObject;
    }
}
