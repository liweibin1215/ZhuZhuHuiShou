package com.lwb.framelibrary.adapter.loadmore;

/**
 * 创建时间：2017/10/22
 * 作者：李伟斌
 * 功能描述:
 */
public interface FooterNoDataClickListener {
    /**
     * when foot Click to callback
     */
    void onFooterClick();

    /**
     * 尾部布局显示的文字
     * @return
     */
    String getFooterContent();

    /**
     * 文字颜色设置
     * @return
     */
    int getFooterTextColor();

}
