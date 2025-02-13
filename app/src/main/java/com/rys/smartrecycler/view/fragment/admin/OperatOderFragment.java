package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.adapter.LogAdapter;
import com.rys.smartrecycler.db.controller.FinishLogController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.retbean.FinishLogBean;
import com.rys.smartrecycler.db.retbean.LogBean;
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
public class OperatOderFragment extends BaseTitleFragment {
    public static final String TAG = "OperatOderFragment";
    private LogAdapter logAdapter;
    private RecyclerView rv;
    private List<LogBean> vos;
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
        mAcFragmentCall.excuteFragmentAction(1000,"操作记录");
        rv = conventView.findViewById(R.id.rv_content);
        vos = LogController.getLogsByPage(currentPage,pageSize);
        logAdapter = new LogAdapter(mContext,vos);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setAdapter(logAdapter);
        logAdapter.notifyDataSetChanged();
        return conventView;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_operat_oder;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }
}
