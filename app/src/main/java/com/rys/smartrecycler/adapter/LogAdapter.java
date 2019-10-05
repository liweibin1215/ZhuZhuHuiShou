package com.rys.smartrecycler.adapter;

import android.content.Context;

import com.lwb.framelibrary.adapter.BaseRecycleAdapter;
import com.lwb.framelibrary.adapter.BaseViewHolder;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.retbean.LogBean;

import java.util.List;

/**
 * 创建时间：2019/6/3
 * 作者：李伟斌
 * 功能描述:
 */
public class LogAdapter extends BaseRecycleAdapter<LogBean> {

    public LogAdapter(Context mContext, List<LogBean> mDatas) {
        super(mContext, mDatas, R.layout.adapter_log_item);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, LogBean finishLogBean, int position) {
        holder.setText(R.id.tv_log_type,getLogTypeName(finishLogBean.getLogType()));
        holder.setText(R.id.tv_log_time,finishLogBean.getCreateTime());
        holder.setText(R.id.tv_log_moudle,finishLogBean.getLogName());
        holder.setText(R.id.tv_log_desc,finishLogBean.getLogDesc());
    }
    public String getLogTypeName(int type){
        if(type == 0){
            return "操作日志";
        }else if(type == 1){
            return "告警日志";
        }else {
            return "崩溃日志";
        }
    }

}