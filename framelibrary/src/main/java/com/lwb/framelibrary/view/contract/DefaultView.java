package com.lwb.framelibrary.view.contract;


import com.lwb.framelibrary.view.base.BaseModel;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;

/**
 * 创建时间：2017/11/13
 * 作者：李伟斌
 * 功能描述:
 */

public class DefaultView {
    public interface View extends BaseView {

    }
    public interface Presenter<V extends BaseView> extends BasePresenter<V> {

    }
    public interface Model extends BaseModel {
    }
}
