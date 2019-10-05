package com.lwb.framelibrary.adapter.loadmore;

/**
 * 创建时间：2017/10/22
 * 作者：李伟斌
 * 功能描述:
 */

public interface OnLoadMoreListener {
    /**
     * when recyclerview scroll to bottom callback
     */
    void onLoadMore();
    /**
     * when load fail click to callback
     */
    void onClickLoadMore();
}
