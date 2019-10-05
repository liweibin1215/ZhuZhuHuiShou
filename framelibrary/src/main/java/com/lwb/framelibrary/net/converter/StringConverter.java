package com.lwb.framelibrary.net.converter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 创作时间： 2018/4/23 on 下午7:32
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class StringConverter implements Converter<ResponseBody,String>{

    public static final StringConverter create(){
        return new StringConverter();
    }

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
