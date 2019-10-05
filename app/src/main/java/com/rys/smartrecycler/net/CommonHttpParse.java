package com.rys.smartrecycler.net;

import com.lwb.framelibrary.net.response.base.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创作时间： 2019/5/27 on 下午5:49
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class CommonHttpParse {
    public static HttpResponse<String> parseCommonHttpResult(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            int code = jsonObject.optInt("code");
            boolean isSuccess = jsonObject.optBoolean("isSuccess");
            String  msg = jsonObject.optString("msg");
            String data = result;
            HttpResponse<String> baseBean = new HttpResponse<>(isSuccess,code,msg,"",data);
            return  baseBean;
        } catch (JSONException e) {
            return null;
        }
    }
}
