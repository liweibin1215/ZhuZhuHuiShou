package com.lwb.framelibrary.adapter.SectionedAdapter;

import android.support.v7.widget.GridLayoutManager;

/**
 * 创建时间：2017/11/01
 * 作者：李伟斌
 * 功能描述:
 */
public class SectionedSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

    protected SectionedRecyclerViewAdapter<?, ?, ?, ?, ?> adapter = null;
    protected GridLayoutManager layoutManager = null;

    public SectionedSpanSizeLookup(SectionedRecyclerViewAdapter<?, ?, ?, ?, ?> adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    //目前使用的地方 问答和险种分类选择
    @Override
    public int getSpanSize(int position) {
        if (adapter.hasHeader()) {//列表顶部有header
            if (position == 0) {
                return layoutManager.getSpanCount();
            } else if (position + 1 < adapter.getItemCount()) {
                if (adapter.isSectionHeaderPosition(position -1) || adapter.isSectionFooterPosition(position -1)) {
                    return layoutManager.getSpanCount();
                } else {
                    return 1;
                }
            } else {
                return layoutManager.getSpanCount();
            }
        } else {//列表顶部没有header
            if (position + 1 < adapter.getItemCount()) {
                if (adapter.isSectionHeaderPosition(position) || adapter.isSectionFooterPosition(position)) {
                    return layoutManager.getSpanCount();
                } else {
                    return 1;
                }
            } else {
                return layoutManager.getSpanCount();
            }
        }
    }
}
