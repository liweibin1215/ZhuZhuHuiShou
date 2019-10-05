package com.lwb.framelibrary.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startInvoke(savedInstanceState);
    }

    protected abstract void startInvoke(Bundle savedInstanceState);
}
