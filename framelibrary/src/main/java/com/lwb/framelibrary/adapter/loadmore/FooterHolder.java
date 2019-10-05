package com.lwb.framelibrary.adapter.loadmore;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lwb.framelibrary.R;


/**
 * 创建时间：2017/10/22
 * 作者：李伟斌
 * 功能描述:
 */

public class FooterHolder extends RecyclerView.ViewHolder {
    public TextView tvFooter;
    public ProgressBar progressBar;

    public FooterHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }

    /**
     * 设置默认的文字颜色 #AAAAAA
     */
    public void setDefaultTextColor(){
        tvFooter.setTextColor(Color.parseColor("#AAAAAA"));
    }
}
