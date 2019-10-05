package com.lwb.framelibrary.net.stringcalladapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * 创作时间： 2018/4/23 on 下午8:02
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class CustomCallAdapterFactory extends CallAdapter.Factory {
    public static final CustomCallAdapterFactory create(){
        return new CustomCallAdapterFactory();
    }
    @Override
    public CallAdapter<?,?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        // 获取原始类型
        Class<?> rawType = getRawType(returnType);
        // 返回值必须是CustomCall并且带有泛型
        if (rawType == CustomCall.class && returnType instanceof ParameterizedType) {
            Type callReturnType = getParameterUpperBound(0, (ParameterizedType) returnType);
            return new CustomCallAdapter(callReturnType);
        }
        return null;
    }
}

