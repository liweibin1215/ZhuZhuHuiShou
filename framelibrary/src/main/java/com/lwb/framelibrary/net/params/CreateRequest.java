package com.lwb.framelibrary.net.params;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


/**
 * 构造请求request类
 * @author lwb
 * @Time 2017年6月14日
 * @Description
 */
public class CreateRequest {
	/**
	 * 方法一
	 * 参数以map形式传入
	 * @param params
	 * @return request实体
	 */
	public static String createRequest(Map<String, String> params) {
		JSONObject request = new JSONObject();
		try {
			if (params != null && !params.isEmpty()) {
				for (String key : params.keySet()) {
                    request.put(key, params.get(key));
				}
			}
		} catch (JSONException e) {
		}
		return request.toString();
	}

    /**
     * 方法三
     * @param params 参数
     * @param listKey 待转array的key
     * @return
     */
    public static String createRequest(Map<String, String> params,String listKey) {
        JSONObject request = new JSONObject();
        try {
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    if(key.equals(listKey)){
                        request.put(key, new JSONArray(params.get(key)));
                    }else{
                        request.put(key, params.get(key));
                    }
                }
            }
        } catch (JSONException e) {
        }
        return request.toString();
    }
	/**
	 * 方法二
	 * 参数以字符串数组形式（建议参数较少时使用）
	 * @param params
	 * @return request实体
	 */
	public static String createRequest(String... params) {
		JSONObject request = new JSONObject();
		int len = params.length;
		if(len %2  !=  0)
			return null;
		try {
			for (int i = 0; i < len/2; i++) {
                request.put(params[2*i], params[2*i+1]);
			}
		} catch (JSONException e) {
			return null;
		}
		return request.toString();
	}

    /**
     * 创建map集合
     * @param params
     * @return
     */
    public static Map<String, String>  createMapParams (String... params){
        Map<String,String> map = new HashMap<>();
        int len = params.length;
        if(len %2  !=  0)
            return null;
        for (int i = 0; i < len/2; i++) {
            map.put(params[2*i], params[2*i+1]);
        }
        return map;
    }
    public static Map<String, String> createBaseParams(String DeviceSn){
        Map<String,String> map = new HashMap<>();
        map.put("ReqTime", String.valueOf(System.currentTimeMillis()));
        map.put("AppID", "0");
        map.put("TerminalType", "Android");
        map.put("TerminalVersion", "");
        map.put("Nonce", DiyiSignUtils.getRandomStringByLength(20));
        map.put("SmartBoxSn",DeviceSn);
        return map;
    }

    /**
     * 合成请求body
     * @param jsonStr json参数
     * @return
     */
    public static RequestBody createRequestBody(String jsonStr){
        if(jsonStr == null || "".equals(jsonStr)){
            return null;
        }
		return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonStr);
    }

    public static String createGetPayCodeRequest(Map<String, String> params,String speckialKey) {
        JSONObject request = new JSONObject();
        try {
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    if(speckialKey.equals(key)){
                        int num = Integer.parseInt(params.get(key));
                        request.put(key, num);
                    }else{
                        request.put(key, params.get(key));
                    }

                }
            }
        } catch (JSONException e) {
        }
        return request.toString();
    }
}
