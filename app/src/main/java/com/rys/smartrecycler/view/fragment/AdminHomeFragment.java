package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwb.framelibrary.tool.AppManagerUtils;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.rys.smartrecycler.view.fragment.admin.DeliveryOrderFragment;
import com.rys.smartrecycler.view.fragment.admin.DeskStatusFragment;
import com.rys.smartrecycler.view.fragment.admin.DeviceDebugFragment;
import com.rys.smartrecycler.view.fragment.admin.OperatOderFragment;
import com.rys.smartrecycler.view.fragment.admin.RecycleOrderFragment;
import com.rys.smartrecycler.view.fragment.admin.SoftVersionFragment;
import com.rys.smartrecycler.view.fragment.admin.SystemSetFragment;

/**
 * 创作时间： 2019/5/23 on 下午3:20
 * <p>
 * 描述：首页/投递选择/登录
 * <p>
 * 作者：lwb
 */
public class AdminHomeFragment extends BaseTitleFragment implements View.OnClickListener {
    public static final String TAG = "AdminHomeFragment";
    @Override
    protected int doThingsBeforeExit() {
        return 1;
    }

    @Override
    protected void onClickBack() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1001,"");
        initView();

        return conventView;
    }

    public void initView() {
        conventView.findViewById(R.id.ll_delivery_order).setOnClickListener(this);
        conventView.findViewById(R.id.ll_recycle_order).setOnClickListener(this);
        conventView.findViewById(R.id.ll_opertor_order).setOnClickListener(this);
        conventView.findViewById(R.id.ll_system_config).setOnClickListener(this);
        conventView.findViewById(R.id.ll_device_debug).setOnClickListener(this);
        conventView.findViewById(R.id.ll_desk_status).setOnClickListener(this);
        conventView.findViewById(R.id.ll_soft_version).setOnClickListener(this);
        conventView.findViewById(R.id.ll_go_front).setOnClickListener(this);
        conventView.findViewById(R.id.ll_go_exit).setOnClickListener(this);
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_admin_home;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delivery_order:
                startFragmentAndFinishSelf(this,new DeliveryOrderFragment(),DeliveryOrderFragment.TAG,R.id.fy_admin_id);
                break;
            case R.id.ll_recycle_order:
                startFragmentAndFinishSelf(this,new RecycleOrderFragment(),RecycleOrderFragment.TAG,R.id.fy_admin_id);
                break;
            case R.id.ll_opertor_order:
                startFragmentAndFinishSelf(this,new OperatOderFragment(),OperatOderFragment.TAG,R.id.fy_admin_id);
                break;
            case R.id.ll_system_config:
                startFragmentAndFinishSelf(this,new SystemSetFragment(),SystemSetFragment.TAG,R.id.fy_admin_id);
                break;
            case R.id.ll_device_debug:
                startFragmentAndFinishSelf(this,new DeviceDebugFragment(),DeviceDebugFragment.TAG,R.id.fy_admin_id);
                break;
            case R.id.ll_desk_status:
                startFragmentAndFinishSelf(this,new DeskStatusFragment(),DeskStatusFragment.TAG,R.id.fy_admin_id);
                break;
            case R.id.ll_soft_version:
                startFragmentAndFinishSelf(this,new SoftVersionFragment(),SoftVersionFragment.TAG,R.id.fy_admin_id);
                break;
            case R.id.ll_go_front:
                getActivity().finish();
                break;
            case R.id.ll_go_exit:
                AppManagerUtils.getInstance().AppExit(mContext);
                break;
        }
    }

}
