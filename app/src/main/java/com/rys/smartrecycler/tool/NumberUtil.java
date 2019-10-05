package com.rys.smartrecycler.tool;

/**
 * 创建时间：2019/5/30
 * 作者：李伟斌
 * 功能描述:
 */
public class NumberUtil {
    /**
     * 保留两位小数
     * @param num
     * @return
     */
    public static double getDoubleFromat(double num){
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.00");
        String str = myformat.format(num);
        return Double.parseDouble(str);
    }

}
