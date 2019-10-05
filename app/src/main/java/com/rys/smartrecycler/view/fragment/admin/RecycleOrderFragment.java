package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.adapter.RecycleOrderAdapter;
import com.rys.smartrecycler.db.controller.AdminOrderController;
import com.rys.smartrecycler.db.retbean.AdminOrder;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.rys.smartrecycler.view.fragment.AdminHomeFragment;

import java.util.List;

/**
 * 创作时间： 2019/5/23 on 下午3:20
 * <p>
 * 描述：回收记录
 * <p>
 * 作者：lwb
 */
public class RecycleOrderFragment extends BaseTitleFragment {
    public static final String TAG = "RecycleOrderFragment";
    private RecycleOrderAdapter recycleOrderAdapter;
    private RecyclerView rv;
    private List<AdminOrder> vos;
    private int currentPage = 1;
    private int pageSize = 20;
    @Override
    protected int doThingsBeforeExit() {
        return 1;
    }

    @Override
    protected void onClickBack() {
        startFragmentAndFinishSelf(this,new AdminHomeFragment(),AdminHomeFragment.TAG,R.id.fy_admin_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1000,"回收记录");
        rv = conventView.findViewById(R.id.rv_content);
        vos = AdminOrderController.getAdminOrderByPage(currentPage,pageSize);
        recycleOrderAdapter = new RecycleOrderAdapter(mContext,vos);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setAdapter(recycleOrderAdapter);
        recycleOrderAdapter.notifyDataSetChanged();
        return conventView;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycle_order;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

}
