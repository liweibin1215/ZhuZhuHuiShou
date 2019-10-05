package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lwb.framelibrary.net.subscriber.BaseHttpSubscriber;
import com.lwb.framelibrary.utils.ToastUtil;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.controller.DeviceInfoController;
import com.rys.smartrecycler.db.retbean.DeviceInfo;
import com.rys.smartrecycler.net.CommonHttpManager;
import com.rys.smartrecycler.net.bean.DeviceInitBean;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.rys.smartrecycler.view.fragment.AdminHomeFragment;

/**
 * 创作时间： 2019/5/23 on 下午3:20
 * <p>
 * 描述：系统设置
 * <p>
 * 作者：lwb
 */
public class SystemSetFragment extends BaseTitleFragment implements View.OnClickListener , TextWatcher {
    private EditText etDeviceSn,etDevicePwd;
    private Button btnSave;
    public static final String TAG = "SystemSetFragment";
    private String currentDevice,currentPwd;
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
        mAcFragmentCall.excuteFragmentAction(1000,"系统设置");
        etDeviceSn = conventView.findViewById(R.id.et_device);
        etDevicePwd = conventView.findViewById(R.id.et_device_pwd);
        btnSave = conventView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        etDeviceSn.addTextChangedListener(this);
        etDevicePwd.addTextChangedListener(this);
        currentDevice = BaseApplication.getInstance().getTerminalId();
        currentPwd = BaseApplication.getInstance().getTerminalIdPwd();
        etDeviceSn.setText("190419114048");
        etDevicePwd.setText("123456");
        return conventView;
    }

    public void initView(){
    }
    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_system_set;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_save:
                saveDeviceSn();
                break;
        }
    }

    public void saveDeviceSn(){
        final String deviceSn = etDeviceSn.getText().toString().trim();
        final String devicePwd = etDevicePwd.getText().toString().trim();
        if("".equals(deviceSn) || deviceSn.length() < 8){
            Toast.makeText(getActivity(),"设备编号必须大于八位",Toast.LENGTH_LONG).show();
            return;
        }
        if("".equals(devicePwd) || deviceSn.length() < 6){
            Toast.makeText(getActivity(),"密码必须大于等于六位",Toast.LENGTH_LONG).show();
            return;
        }
        CommonHttpManager.commonObserve(CommonHttpManager.getInstance().getmApiService().deviceInit(deviceSn,devicePwd)).subscribe(new BaseHttpSubscriber<DeviceInitBean>() {

            @Override
            public void onSuccess(DeviceInitBean deviceInitBean) {
                if(deviceInitBean != null){
                    if(deviceInitBean.getMqtt() != null){
                        DeviceInfoController.insertDesk(new DeviceInfo(deviceSn,devicePwd,deviceInitBean.getMqtt().getProductKey(),deviceInitBean.getMqtt().getDeviceSecret()));
                    }else{
                        DeviceInfoController.insertDesk(new DeviceInfo(deviceSn,devicePwd,"",""));
                    }
                    btnSave.setVisibility(View.GONE);
                    etDeviceSn.setText(deviceSn);
                    etDevicePwd.setText(devicePwd);
                    currentDevice = etDeviceSn.getText().toString().trim();
                    currentPwd = etDevicePwd.getText().toString().trim();
                    BaseApplication.getInstance().setTerminalId(currentDevice);
                    BaseApplication.getInstance().setTerminalIdPwd(currentPwd);
                    BaseApplication.getInstance().setToken(deviceInitBean.getToken());
                    ToastUtil.showCenterToast(mContext,"设备初始化成功");
                }else{
                    ToastUtil.showCenterToast(mContext,"设备初始化失败");
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.showCenterToast(mContext,errorMsg);
            }
        });
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!currentDevice.equals(etDeviceSn.getText().toString()) || !currentPwd.equals(etDevicePwd.getText().toString())){
            if(etDeviceSn.getText().toString().length() > 8){
                btnSave.setVisibility(View.VISIBLE);
            }
        }
    }
}
