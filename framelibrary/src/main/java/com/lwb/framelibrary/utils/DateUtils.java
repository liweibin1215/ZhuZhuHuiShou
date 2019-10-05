package com.lwb.framelibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 创作时间： 2019/6/3 on 下午10:30
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class DateUtils {
    /**
     *
     * @param time  1541569323155
     * @param pattern yyyy-MM-dd HH:mm:ss
     * @return 2018-11-07 13:42:03
     */
    public static String getDate2String(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    public static String getDateyyyyMMddHHmmss(long time){
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }
}
