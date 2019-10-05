package com.lwb.framelibrary.net.stringcalladapter;

import java.io.IOException;

import retrofit2.Call;

/**
 * 创作时间： 2018/4/23 on 下午7:51
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class CustomCall<R> {
    public final Call<R> call;

    public CustomCall(Call<R> call) {
        this.call = call;
    }

    public R get() throws IOException {
        return call.execute().body();
    }
}
