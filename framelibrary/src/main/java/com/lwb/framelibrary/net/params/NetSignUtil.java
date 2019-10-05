package com.lwb.framelibrary.net.params;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author lwb
 * @Time 2017年5月25日
 * @Description 参数签名
 */
public class NetSignUtil {
	/**
	 *  sha1加密
	 * @param info
	 * @return
	 */
	public static String encryptToSHA(String info) {
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
	/**
	 * 将参数以特定形式生成签名，并返回
	 */
	public static String getSign(Map<String, String> paraMap) {
		String params = format(paraMap);

		params = params + "&key=" + "sdhgdhdhdfghthfgh";

		String md5 = toMD5(params.getBytes());

		return md5.toUpperCase();
	}
	/**
	 * 将参数用于MD5加密
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
	 * 把参数拼接成一个字符串返回
	 *
	 * @param paraMap
	 * @return
	 */
	public static String format(Map<String, String> paraMap) {
		Set<String> keys = paraMap.keySet();
		List<String> sorted = asSortedList(keys);
		StringBuffer buffer = new StringBuffer();
		for (String key : sorted) {
			buffer.append(key + "=" + paraMap.get(key) + "&");
		}
		String result = "";
		if (buffer.length() > 0)// collAddr=南京西路99号外滩中心1号88层8801室上海有信信息技术服务有限公司技术部一分部&collArea=黄浦区&collCity=上海市&collPayment=0&collPerson=叶良辰&collPhone=13237760314&collProvince=上海&insuranceFee=0&orderNo=10111469617690441878&productName=包裹&sendAddr=上海市徐汇区田林路397号B座1层10002&sendArea=徐汇区&sendCity=上海市&sendPerson=陈默雨&sendPhone=13248159839&sendProvince=上海&toPayFee=0&trackNo=80215932544159&weight=1000
		{
			result = buffer.substring(0, buffer.length() - 1);
		}
		return result;
	}

	/**
	 * 将所有的key按字母排序
	 *
	 * @param c
	 * @return
	 */
	public static List<String> asSortedList(Collection<String> c) {
		List<String> list = new ArrayList<String>(c);
		mySortComparator mySort = new mySortComparator();
		java.util.Collections.sort(list, mySort);
		return list;
	}

	public static class mySortComparator implements Comparator<String> {
		@Override
		public int compare(String lhs, String rhs) {

			return lhs.compareTo(rhs);
		}

		public mySortComparator() {
		}
	}

	public static String formatString(String str) {
		if (str == null || "".equals(str))
			return "";
		StringBuilder builder = new StringBuilder("");
		int length = str.length();
		if (length <= 4)
			return str;
		for (int i = 1; i <= length; i++) {
			if (i % 3 == 0) {
				builder.append(str.subSequence(0, 3)).append(" ");
				str = str.substring(3);
				if (i + 3 > length) {
					builder.append(str);
				}
			}
		}

		return builder.toString();
	}
}
