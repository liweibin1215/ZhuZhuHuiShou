package com.lwb.framelibrary.net.params;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 创建时间：2018/3/29
 * 作者：李伟斌
 * 功能描述: DES加密ECB模式
 */

public class DesECB {
    public static final String ALGORITHM_DES = "DES/ECB/PKCS5Padding";

    /**
     * 加密
     * @param key 秘钥（长度必须大于等于八）
     * @param data
     * @return
     * @throws Exception
     */
    public static String encode(String key, String data){
        try {
            return encode(key, data.getBytes());
        } catch (Exception e) {
            return "";
        }
    }
    public static String encode(String key, byte[] data) throws Exception {

        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(data);
            return Base64.encodeToString(bytes, Base64.DEFAULT).replace("\n","").trim();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 解密
     * @param key 秘钥 (长度必须大于等于八)
     * @param data
     * @return
     */
    public static String decode(String key, String data) {
        byte[] datas;
        String value = null;
        try {
            if (System.getProperty("os.name") != null
                    && (System.getProperty("os.name").equalsIgnoreCase("sunos") || System
                    .getProperty("os.name").equalsIgnoreCase("linux"))) {
                datas = decode(key, Base64.decode(data, Base64.DEFAULT));
            } else {
                datas = decode(key, Base64.decode(data, Base64.DEFAULT));
            }
            value = new String(datas);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }
    public static byte[] decode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
