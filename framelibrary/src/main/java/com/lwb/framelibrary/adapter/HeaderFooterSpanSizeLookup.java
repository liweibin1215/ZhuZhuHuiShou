package com.lwb.framelibrary.adapter;

import android.support.v7.widget.GridLayoutManager;


/**
 * 创建时间：2017/11/01
 * 作者：李伟斌
 * 功能描述:添加头部和尾部的GridLayoutManager SpanSizeLookup
 */
public class HeaderFooterSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    protected BaseLoadMoreAdapter<?, ?, ?> adapter = null;
    protected GridLayoutManager layoutManager = null;

    public HeaderFooterSpanSizeLookup(BaseLoadMoreAdapter<?, ?, ?> adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    /**
     * @param position
     * @return 这里的返回值是指当前item宽度占多少份
     */
    @Override
    public int getSpanSize(int position) {
        if (adapter.hasHeader()) {//列表顶部有header
            if (position == 0) {
                return layoutManager.getSpanCount();
            } else if (position + 1 < adapter.getItemCount()) {
                return 1;
            } else {
                return layoutManager.getSpanCount();
            }
        } else {//列表顶部没有header
            if (position + 1 < adapter.getItemCount()) {
                return 1;
            } else {
                return layoutManager.getSpanCount();
            }
        }
    }
}
