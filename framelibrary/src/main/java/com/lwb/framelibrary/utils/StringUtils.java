package com.lwb.framelibrary.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {

    /**
     * @param str
     */
    public static boolean isNull(String str) {
        return null == str || "".equals(str.trim());
    }

    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    public static int stringNumbers(String str, String mat) {
        if (isNull(str) || isNull(mat)) {
            return 0;
        }
        if (str.indexOf(mat) == -1) {
            return 0;
        }
        int count = 0;
        int i = str.indexOf(mat);
        int length = mat.length();
        while (i != -1) {
            count++;
            str = str.substring(i + length);
            i = str.indexOf(mat);
        }

        return count;
    }

    public static String deleteChineseInNumber(String s) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(s);
        String result = m.replaceAll("");
        return result;
    }


    public static String myBase64encode(String content, String password) {
        String newcontent = password + content;
        String baseString = Base64.encodeToString(newcontent.getBytes(), Base64.DEFAULT);
        return baseString;
    }

    public static String myBase64decode(String content, String password) {
        byte[] mybyte = Base64.decode(content.getBytes(), Base64.DEFAULT);
        String baseString = new String(mybyte);
        if (baseString.length() > 6) {
            if (password.equals(baseString.subSequence(0, 6))) {
                baseString = baseString.substring(6);
            } else {
                baseString = "解密码不正确";
            }
        }
        return baseString;
    }


    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChinese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character
                .UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock
                .CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock
                .GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>,.~-———_&^%$#{}=+();，。、？”“；：！@#￥%……&*（）{}《》|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    // ----判断手机号是否正确-------
    public static int numbererror(String s) {
        int n;
        if (s.equals("")) {
            return 0;
        }
        int l;
        l = s.length();
        // --
        if (l != 11) {
            return 0;
        }
        boolean result = s.matches("[0-9]+");
        if (result != true) {
            return 0;
        }

        if (l > 3) l = 3;
        String newstr = s.substring(0, l);
        if (newstr.equals("")) {
            n = 0;
        } else {
            n = Integer.parseInt(newstr);
        }
        // n= Integer.parseInt(newstr);
        if ((n > 129) && (n < 200)) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * 匹配手机号后n位，并返回结果
     *
     * @param s 要匹配的数字  5-10
     * @return 匹配结果, 匹配不到返回“”
     */
    public static String matchLastNNumber(String s) {
//      List<PhoneNumber1> list = phoneNumber1Dao.queryBuilder().where(PhoneNumber1Dao.Properties
//                .Number.like("%" + s)).list();
//        if (list.size() == 1) {
//            result = list.get(0).getNumber();
//        } else {
//            result = "";
//        }
        return "";
    }

    /**
     * 是否是默认货号
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isCommonHuoHao(String str) throws PatternSyntaxException {
        String regExp = "^[0-3][0-9][a-zA-Z][0-9]{3}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 大陆手机号码11位数，匹配格式：前2位固定格式+后9位任意数
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^[1][0-9]{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 身份证号
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */


    public static boolean isIDCardLegal(String str) throws PatternSyntaxException {
        String regExp = "/^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}" +
                "(\\d|[xX])$/\n";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 身份证号
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */

    public static boolean iSIDCardLegal(String str) throws PatternSyntaxException {
        String regExp = "^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String generateNextHuoHao(String s) {
        String result = "";
        s = s.toUpperCase();
        if (TextUtils.isEmpty(s.trim())) {
            int i = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (i < 10) {
                return "0" + i + "A001";
            }
            return i + "A001";
        }
        if (StringUtils.isCommonHuoHao(s)) {
            String head = s.substring(0, 3);
            String foot = s.substring(3, 6);
            int j = Integer.parseInt(foot);
            //后三位增加
            if (j < 999) {
                j++;
                String format = String.format("%03d", j);
                result = head + format;
            } else {
                char c = s.charAt(2);
                String replace = s.replace(c, ++c);
                result = replace.replace(foot, "001");
            }
        } else {
            String str3;
            char[] p1 = new char[12];
            char[] p2 = new char[12];
            char[] p = s.toCharArray();
            int len = p.length;
            int i;
            for (i = 0; i < len; i++) {
                if (p[len - i - 1] < '0' || p[len - i - 1] > '9') break;
            }
            int m = len - i;
            int numlength = i + 1; // 为最后字符补零准备
            for (i = 0; i < m; i++) {
                p1[i] = p[i];
            }
            for (i = 0; i < len - m; i++) {
                p2[i] = p[m + i];
            }
            long h, l;
            if (String.valueOf(p2).trim().length() == 0) // java需要
            {
                p2[0] = '0';
            }

            h = Long.parseLong((String.valueOf(p2)).trim()) + 1;

            str3 = String.valueOf(h);
            int n1 = numlength - str3.length() - 1; // 得到补零的个数
            String s0 = "";
            for (i = 0; i < n1; i++) {
                s0 = s0 + "0";
            }
            result = String.valueOf(p1).trim() + s0 + str3;

        }
        return result;
    }

    public static String generateLastHuoHao(String s) {
        String result = "";
        s = s.toUpperCase();
        if (TextUtils.isEmpty(s.trim())) {
            int i = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (i < 10) {
                return "0" + i + "A001";
            }
            return i + "A001";
        }
        if (StringUtils.isCommonHuoHao(s)) {
            String head = s.substring(0, 3);
            String foot = s.substring(3, 6);
            int j = Integer.parseInt(foot);
            //后三位增加
            if (j < 999) {
                if(j>0){
                    j--;
                }
                String format = String.format("%03d", j);
                result = head + format;
            } else {
                char c = s.charAt(2);
                String replace = s.replace(c, --c);
                result = replace.replace(foot, "001");
            }
        } else {
            String str3;
            char[] p1 = new char[12];
            char[] p2 = new char[12];
            char[] p = s.toCharArray();
            int len = p.length;
            int i;
            for (i = 0; i < len; i++) {
                if (p[len - i - 1] < '0' || p[len - i - 1] > '9') break;
            }
            int m = len - i;
            int numlength = i + 1; // 为最后字符补零准备
            for (i = 0; i < m; i++) {
                p1[i] = p[i];
            }
            for (i = 0; i < len - m; i++) {
                p2[i] = p[m + i];
            }
            long h = 0, l;
            if (String.valueOf(p2).trim().length() == 0) // java需要
            {
                p2[0] = '0';
            }
            if(Long.parseLong((String.valueOf(p2)).trim())>0){
                h = Long.parseLong((String.valueOf(p2)).trim()) - 1;
            }
            str3 = String.valueOf(h);
            int n1 = numlength - str3.length() - 1; // 得到补零的个数
            String s0 = "";
            for (i = 0; i < n1; i++) {
                s0 = s0 + "0";
            }
            result = String.valueOf(p1).trim() + s0 + str3;

        }
        return result;
    }


    public static String generateRandomExpressNo() {

        String l = System.currentTimeMillis() + "";
        int a = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
        return l + a;
    }



    /**
     * 判断是否是标签
     *
     * @param content
     * @return
     */
    public static Boolean judgeIsTag(String content) {
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(content);
        while (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * @param text
     * @return
     */
    public static ArrayList<String> getTagsFromRawText(String text) {
        ArrayList<String> datas = new ArrayList<>();
        String[] split = text.split("\\[", -1);
        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split("\\]", -1);
            if (split1.length % 2 == 1) {
                datas.add(split1[0]);
            } else {
                for (int j = 0; j <= 1; j++) {
                    if (j == 0) {
                        datas.add("[" + split1[j] + "]");
                    } else {
                        datas.add(split1[j]);
                    }
                }
            }

        }
        return datas;
    }




    //防止 value  of 报空
    public static String validateStringValue(String text) {
        if (text == null) {
            return "";
        } else if ("".equals(text)) {
            return "";
        } else if (TextUtils.isEmpty(text)) {
            return "";
        } else if (text.equals("null")) {
            return "";
        }else{
            return text;
        }
    }

    public static int getWordCountRegex(String s) {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

    public static String getWeight(int weight) {
        double wei = weight / 1000 + weight % 1000;
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(wei);
    }

    public static String getPrice(int price) {
        if (price == 0)
            return "0.00";
        double pri = price / 100 + price % 100;
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(pri);
    }

    public static boolean isNumber(String s) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 判断字符串是否为URL
     * @param urls 用户头像key
     * @return true:是URL、false:不是URL
     */
    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        //比对
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        //判断是否匹配
        isurl = mat.matches();
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }


    public static boolean equals(String a, String b) {
        if (a == null || b == null)
            return false;
        return a.equals(b);
    }
}
