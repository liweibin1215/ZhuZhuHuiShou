package com.lwb.framelibrary.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 */
public abstract class BaseFragment extends Fragment {


    public BaseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return startInvoke(inflater, container, savedInstanceState);
    }

    /**&
     *  无参跳转
     * @param cls
     * @param requestCode
     */
    protected void jumpActivityForResult(Class<?> cls,int requestCode){
        jumpActivityForResult(cls,requestCode,null);
    }

    /**
     *
     * @param cls
     * @param requestCode
     * @param bundle
     */
    protected void jumpActivityForResult(Class<?> cls,int requestCode,Bundle bundle){
        Intent intent = new Intent(getActivity(),cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }
    /**
     *
     * @param cls 无参跳转
     */
    protected void jumpActivity(Class<?> cls){
        jumpActivity(cls,null);
    }
    /**
     *
     * @param cls
     * @param bundle
     */
    protected  void jumpActivity(Class<?> cls,Bundle bundle){
        Intent intent = new Intent(getActivity(),cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    protected abstract View startInvoke(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
}
