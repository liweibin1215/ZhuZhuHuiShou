package com.rys.smartrecycler.adapter;

import android.content.Context;

import com.lwb.framelibrary.adapter.BaseRecycleAdapter;
import com.lwb.framelibrary.adapter.BaseViewHolder;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.retbean.UserOrder;
import com.rys.smartrecycler.tool.AppTool;

import java.util.List;

/**
 * 创建时间：2019/6/3
 * 作者：李伟斌
 * 功能描述:
 */
public class DeliveryOrderAdapter extends BaseRecycleAdapter<UserOrder> {

    public DeliveryOrderAdapter(Context mContext, List<UserOrder> mDatas) {
        super(mContext, mDatas, R.layout.adapter_delivery_order_item);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, UserOrder userOrder, int position) {
        holder.setText(R.id.tv_deliver_type, AppTool.getTypeCnName(String.valueOf(userOrder.getDeskType())));
        holder.setText(R.id.tv_delivery_desk_no,userOrder.getDeskNo()+"");
        holder.setText(R.id.tv_delivery_sum,userOrder.getRecycleSum());
        holder.setText(R.id.tv_delivery_user,userOrder.getUserPhone());
        holder.setText(R.id.tv_delivery_time,String.valueOf(userOrder.getCreateTime()));
    }
}
