package com.rys.smartrecycler.adapter;

import android.content.Context;

import com.lwb.framelibrary.adapter.BaseRecycleAdapter;
import com.lwb.framelibrary.adapter.BaseViewHolder;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.retbean.AdminOrder;
import com.rys.smartrecycler.tool.AppTool;

import java.util.List;

/**
 * 创建时间：2019/6/3
 * 作者：李伟斌
 * 功能描述:
 */
public class RecycleOrderAdapter extends BaseRecycleAdapter<AdminOrder> {
    public RecycleOrderAdapter(Context mContext, List<AdminOrder> mDatas) {
        super(mContext, mDatas, R.layout.adapter_recycle_order_item);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, AdminOrder adminOrder, int position) {
        holder.setText(R.id.tv_deliver_type, AppTool.getTypeCnName(String.valueOf(adminOrder.getDeskType())));
        holder.setText(R.id.tv_delivery_desk_no,adminOrder.getDeskNo()+"");
        holder.setText(R.id.tv_delivery_sum,adminOrder.getRecycleSum());
        holder.setText(R.id.tv_delivery_user,adminOrder.getUserPhone());
        holder.setText(R.id.tv_delivery_time,String.valueOf(adminOrder.getCreateTime()));
    }
}
