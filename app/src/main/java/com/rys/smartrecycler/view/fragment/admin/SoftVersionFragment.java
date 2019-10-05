package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.BuildConfig;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.controller.DeviceInfoController;
import com.rys.smartrecycler.db.controller.SystemSetController;
import com.rys.smartrecycler.db.retbean.DeviceInfo;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.rys.smartrecycler.view.fragment.AdminHomeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoftVersionFragment extends BaseTitleFragment {
    public static final String TAG = "SoftVersionFragment";
    private TextView tvDeviceSn,tvDeskName,tvVersion;
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
        mAcFragmentCall.excuteFragmentAction(1000,"软件版本");
        tvDeviceSn = conventView.findViewById(R.id.tv_device_sn);
        tvDeskName = conventView.findViewById(R.id.tv_desk_name);
        tvVersion = conventView.findViewById(R.id.tv_version);
        DeviceInfo deviceInfo = DeviceInfoController.getDeviceInfo();
        if(deviceInfo != null){
            tvDeskName.setText(deviceInfo.getDeviceName());
            tvDeviceSn.setText(deviceInfo.getDeviceSn());
        }
        tvVersion.setText("v_"+BuildConfig.VERSION_NAME+"("+BuildConfig.serverType+")");
        return conventView;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_soft_version;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }
}
