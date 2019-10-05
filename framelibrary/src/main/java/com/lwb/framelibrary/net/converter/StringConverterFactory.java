package com.lwb.framelibrary.net.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 创作时间： 2018/4/23 on 下午7:43
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class StringConverterFactory extends Converter.Factory{
    public static final StringConverterFactory INSTANCE = new StringConverterFactory();

    public static StringConverterFactory create() {
        return INSTANCE;
    }

    // 我们只关实现从ResponseBody 到 String 的转换，所以其它方法可不覆盖
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return StringConverter.create();
        }
        //其它类型我们不处理，返回null就行
        return null;
    }
}
