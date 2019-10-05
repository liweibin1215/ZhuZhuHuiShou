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
public class DeskStatusAdapter extends BaseRecycleAdapter<DeskConfigBean> {

    public DeskStatusAdapter(Context mContext, List<DeskConfigBean> mDatas) {
        super(mContext, mDatas, R.layout.adapter_desk_status_item);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, DeskConfigBean deskConfigBean, int position) {
        holder.setText(R.id.tv_desk_no,String.valueOf(deskConfigBean.getDeskNo()));
        holder.setText(R.id.tv_desk_type, AppTool.getTypeCnName(String.valueOf(deskConfigBean.getDeskType())));
        holder.setText(R.id.tv_unit_price,deskConfigBean.getUnitPrice());
        holder.setText(R.id.tv_recycle_sum,deskConfigBean.getDeskType() == 1?deskConfigBean.getBottleNum():deskConfigBean.getTotalWeigth());
        holder.setText(R.id.tv_desk_percent,deskConfigBean.getPercent()+"%");
        holder.setText(R.id.tv_error_info,deskConfigBean.getErrorStatus());
    }
}
