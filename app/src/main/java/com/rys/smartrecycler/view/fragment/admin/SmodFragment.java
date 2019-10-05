package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.device.smog.SmogAlarmManager;
import com.rys.smartrecycler.view.base.BaseTitleFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmodFragment extends BaseTitleFragment implements View.OnClickListener{
    public static final String TAG = "SmodFragment";
    private TextView tvStatus;
    public SmodFragment() {
    }

    @Override
    protected int doThingsBeforeExit() {
        return 0;
    }

    @Override
    protected void onClickBack() {
        startFragmentAndFinishSelf(this,new DeviceDebugFragment(),DeviceDebugFragment.TAG,R.id.fy_admin_id);
    }

    public void initView(){
        conventView.findViewById(R.id.btn_open).setOnClickListener(this);
        conventView.findViewById(R.id.btn_close).setOnClickListener(this);
        conventView.findViewById(R.id.btn_get).setOnClickListener(this);
        tvStatus = conventView.findViewById(R.id.tv_status);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1000,"烟雾报警调试");
        initView();
        return conventView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_smod;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_open:
                openDevice();
                break;
            case R.id.btn_close:
                closeDevice();
                break;
            case R.id.btn_get:
                getCuurentSmodStatus();
                break;
        }
    }

    public void openDevice(){
        tvStatus.setText("当前状态：设备开启中...");
        if(!SmogAlarmManager.getInstance().isDeviceOpened()){
            if(SmogAlarmManager.getInstance().openMainBoardDevice()){
                tvStatus.setText("当前状态：设备已开");
                showToast(1,"报警器打开成功");
            }else{
                showToast(1,"报警器打开失败");
                tvStatus.setText("当前状态：设备未打开");
            }
        }else{
            tvStatus.setText("当前状态：设备已开");
            showToast(1,"报警器打开成功");
        }
    }

    public void closeDevice(){
        if(SmogAlarmManager.getInstance().isDeviceOpened()){
            SmogAlarmManager.getInstance().closeDevice();
        }
        showToast(1,"报警器已关闭");
    }

    public void getCuurentSmodStatus(){
        int status = SmogAlarmManager.getInstance().checkSmogAlarmStatus(1);
        if(status == 0){
            showToast(1,"报警器正常");
        }else if(status == 1){
            showToast(1,"报警器异常");
        }else{
            showToast(1,"报警器未开启");
        }
    }
}
