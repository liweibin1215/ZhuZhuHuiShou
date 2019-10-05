package com.lwb.framelibrary.net.stringcalladapter;


import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * 创作时间： 2018/4/23 on 下午7:53
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class CustomCallAdapter implements CallAdapter<String,CustomCall<String>> {

    private final Type responseType;

    // 下面的 responseType 方法需要数据的类型
    CustomCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public CustomCall<String> adapt(Call<String> call) {
        // 由 CustomCall 决定如何使用
        return new CustomCall<>(call);
    }
}

