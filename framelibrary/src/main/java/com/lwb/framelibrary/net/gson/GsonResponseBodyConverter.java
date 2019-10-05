package com.lwb.framelibrary.net.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.lwb.framelibrary.net.execption.ApiException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * 创建时间：2018/6/25
 * 作者：李伟斌
 * 功能描述:
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody,T> {
    private final Gson gson;
    private final Type type;
    private TypeAdapter<T> adapter;
    public GsonResponseBodyConverter(Gson gson,TypeAdapter<T> adapter,Type type){
        this.gson = gson;
        this.type = type;
        this.adapter = adapter;
    }
    @Override
    public T convert(ResponseBody value) throws IOException {
//        try {
//            return gson.fromJson(value.toString(),type);
//        }catch (Exception e){
//            throw new ApiException(1000, value.toString());//解析异常
//        }
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        }catch (Exception e){

            throw new ApiException(1000, value.toString());//解析异常
        }finally {
            value.close();
        }
    }
}

