package com.lwb.framelibrary.utils;

import java.text.DecimalFormat;
/**
 * @author by mayn on 2018/8/20.
 */

public class NumberUtil {

    /**
     * @param str
     */
    public static boolean isNull(String str) {
        return null == str || "".equals(str.trim());
    }
    /**
     * 格式化钱，默认保留,三位小数，如果第三位为0，则保留两位小数
     *
     * @param a
     * @return
     */
    public static String decimalMoney(String a) {
        try {
            if (isNull(a)) {
                return "";
            }
            return decimalMoney(Float.parseFloat(a));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化钱，默认保留,三位小数，如果第三位为0，则保留两位小数
     *
     * @param a
     * @return
     */
    public static String decimalMoney(float a) {
        DecimalFormat dFormat = new DecimalFormat("#.000");
        String temp = dFormat.format(a);
        if (temp.substring(0, 1).equals(".")) {
            temp = "0" + temp;
        }
        if (temp.substring(0, 1).equals("-") && (temp.substring(1, 2).equals("."))) {
            temp = temp.substring(0, 1) + "0" + temp.substring(1, temp.length());
        }
        if (temp.substring(temp.length() - 1, temp.length()).equals("0"))
            return temp.substring(0, temp.length() - 1);
        else
            return temp;
    }

    /**
     * 格式化钱，默认保留,三位小数，如果第三位为0，则保留两位小数
     *
     * @param a
     * @return
     */
    public static String decimalMoney(double a) {
        DecimalFormat dFormat = new DecimalFormat("#.000");
        String temp = dFormat.format(a);
        if (temp.substring(0, 1).equals(".")) {
            temp = "0" + temp;
        }
        if (temp.substring(0, 1).equals("-") && (temp.substring(1, 2).equals("."))) {
            temp = temp.substring(0, 1) + "0" + temp.substring(1, temp.length());
        }
        if (temp.substring(temp.length() - 1, temp.length()).equals("0"))
            return temp.substring(0, temp.length() - 1);
        else
            return temp;
    }

    /**
     * 格式化钱，默认保留,三位小数，如果第三位为0，则保留两位小数
     *
     * @param a
     * @return
     */
    public static double decimalMoneyToFloat(double a) {
        if (a == 0) {
            return 0.00;
        }
        DecimalFormat dFormat = new DecimalFormat("#.000");
        String temp = dFormat.format(a);
        if (temp.substring(0, 1).equals(".")) {
            temp = "0" + temp;
        }
        if (temp.substring(0, 1).equals("-") && (temp.substring(1, 2).equals("."))) {
            temp = temp.substring(0, 1) + "0" + temp.substring(1, temp.length());
        }
        if (temp.substring(temp.length() - 1, temp.length()).equals("0"))
            return Double.parseDouble(temp.substring(0, temp.length() - 1));
        else
            return Double.parseDouble(temp);
    }
}
