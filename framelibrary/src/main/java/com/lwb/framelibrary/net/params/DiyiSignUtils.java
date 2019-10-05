package com.lwb.framelibrary.net.params;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 创建时间：2018/3/22
 * 作者：李伟斌
 * 功能描述: 新版本递管家签名生成工具
 */

public class DiyiSignUtils {
    /**
     * 给参数添加MD5签名
     *
     * @param paraMap
     * @param token
     * @return
     */
    public static Map<String, String> addSignMD5(Map<String, String> paraMap, String token) {
        String sign = createRequestSignToMD5(paraMap, token);
        paraMap.put("Signature", sign);
        return paraMap;
    }

    /**
     * 给参数添加SHA-1签名
     *
     * @param paraMap
     * @param token
     * @return
     */
    public static Map<String, String> addSignSHA1(Map<String, String> paraMap, String token) {
        String sign = createRequestSignToSHA1(paraMap, token);
        paraMap.put("key", sign);
        return paraMap;
    }

    /**
     * 生成MD5签名
     *
     * @param paraMap
     * @param token
     * @return
     */
    public static String createRequestSignToMD5(Map<String, String> paraMap, String token) {
        String result = paramSort(paraMap, token);
        return "".equals(result) ? "" : toMD5(result.getBytes()).toUpperCase();
    }

    /**
     * 生成SHA-1签名
     *
     * @param paraMap
     * @param token
     * @return
     */
    public static String createRequestSignToSHA1(Map<String, String> paraMap, String token) {
        String result = paramSort(paraMap, token);
        return "".equals(result) ? "" : toSHA1(result).toUpperCase();
    }

    /***************************************************************************************************************/

    public static String paramSort(Map<String, String> paraMap, String keySeret) {
        Set<String> keys = paraMap.keySet();
        List<String> sorted = asSortedList(keys);
        StringBuffer buffer = new StringBuffer();
        for (String key : sorted) {
            String result = paraMap.get(key);
            if (result == null || "".equals(result))
                continue;//参数为空不参与签名
            buffer.append(key + "=" + result + "&");
        }
        if (buffer.length() <= 0) {
            return "";
        }
        String result = "";
        if (keySeret != null && !"".equals(keySeret)) {
            buffer.append("key=" + keySeret);
            result = buffer.toString();
        } else {
            if (buffer.length() > 0) {
                result = buffer.substring(0, buffer.length() - 1);
            }
        }
        return result;
    }

    /**
     * MD5加密
     *
     * @param source
     * @return
     */
    public static String toMD5(byte[] source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest())
                buf.append(String.format("%02x", b & 0xff));
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sha1加密
     *
     * @param info
     * @return
     */
    public static String toSHA1(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String rs = byte2hex(digesta);
        return rs;
    }

    /**
     * 给密码DES加密
     *
     * @param phone 手机号
     * @param data
     * @return
     */
    public static String encodePassword(String phone, String data) {
        if (phone == null || "".equals(phone) || phone.length() < 8) {
            return "";
        }
        return DesECB.encode(phone.substring(0, 8), data);
    }

    /**
     * 生成的随机数
     *
     * @param length
     * @return
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成数字随机数
     * @param length
     * @return
     */
    public static String getRandomNumStringByLength(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    public static List<String> asSortedList(Collection<String> c) {
        List<String> list = new ArrayList<String>(c);
        SortComparator mySort = new SortComparator();
        java.util.Collections.sort(list, mySort);
        return list;
    }

    public static class SortComparator implements Comparator<String> {
        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }
}
