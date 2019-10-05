package com.rys.smartrecycler.adapter;

import android.content.Context;

import com.lwb.framelibrary.adapter.BaseRecycleAdapter;
import com.lwb.framelibrary.adapter.BaseViewHolder;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.tool.AppTool;

import java.util.List;

/**
 * 创建时间：2019/6/3
 * 作者：李伟斌
 * 功能描述:
 */
public class DeskConfigAdapter extends BaseRecycleAdapter<DeskConfigBean> {

    public DeskConfigAdapter(Context mContext, List<DeskConfigBean> mDatas) {
        super(mContext, mDatas, R.layout.adapter_deskconfig_item);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, DeskConfigBean deskConfigBean, int position) {
        holder.setText(R.id.tv_desk_no,String.valueOf(deskConfigBean.getDeskNo()));
        holder.setText(R.id.tv_desk_type, AppTool.getTypeCnName(String.valueOf(deskConfigBean.getDeskType())));
        holder.setText(R.id.tv_unit_price,deskConfigBean.getUnitPrice());
        holder.setText(R.id.tv_update_time,deskConfigBean.getUpdateTime());
    }
}
