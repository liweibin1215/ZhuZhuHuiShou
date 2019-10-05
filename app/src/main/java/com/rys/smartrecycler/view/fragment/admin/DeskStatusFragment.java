package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lwb.framelibrary.net.subscriber.BaseHttpSubscriber;
import com.lwb.framelibrary.utils.ToastUtil;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.adapter.DeskStatusAdapter;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.DeviceInfoController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.db.retbean.DeviceInfo;
import com.rys.smartrecycler.net.CommonHttpManager;
import com.rys.smartrecycler.net.bean.DeskInfoBean;
import com.rys.smartrecycler.net.bean.DeviceInfoBean;
import com.rys.smartrecycler.tool.AppTool;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.rys.smartrecycler.view.fragment.AdminHomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 创作时间： 2019/5/23 on 下午3:20
 * <p>
 * 描述：柜机状态
 * <p>
 * 作者：lwb
 */
public class DeskStatusFragment extends BaseTitleFragment implements View.OnClickListener {
    public static final String TAG = "DeskStatusFragment";
    private DeskStatusAdapter deskStatusAdapter;
    private Button btnGetDesk;
    private RecyclerView rv;
    private List<DeskConfigBean> vos;
    private boolean isGeting = false;
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
        mAcFragmentCall.excuteFragmentAction(1000,"柜机状态");
        rv = conventView.findViewById(R.id.rv_content);
        btnGetDesk = conventView.findViewById(R.id.btn_get);
        btnGetDesk.setOnClickListener(this);
        vos = DeskConfigController.getAllDesks();
        deskStatusAdapter = new DeskStatusAdapter(mContext,vos);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setAdapter(deskStatusAdapter);
        deskStatusAdapter.notifyDataSetChanged();
        return conventView;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_desk_status;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.btn_get){
            getDeskConfig();
        }
    }
    public void getDeskConfig(){
        if(isGeting){
            return;
        }
        isGeting = true;
        if("".equals(BaseApplication.getInstance().getTerminalId())){
            isGeting = false;
            ToastUtil.showCenterToast(mContext,"未配置设备编号");
            return;
        }
        if("".equals(BaseApplication.getInstance().getToken())){
            isGeting = false;
            ToastUtil.showCenterToast(mContext,"设备未初始化");
            return;
        }
        CommonHttpManager.commonObserve(CommonHttpManager.getInstance().getmApiService().deviceInfo()).subscribe(new BaseHttpSubscriber<DeviceInfoBean>() {

            @Override
            public void onSuccess(DeviceInfoBean deviceInfoBean) {
                if(deviceInfoBean != null){
                    DeviceInfo deviceInfo = DeviceInfoController.getDeviceInfo();
                    deviceInfo.setDeviceName(deviceInfoBean.getName());
                    deviceInfo.setAdminPwd(deviceInfoBean.getAdmin_pw());
                    deviceInfo.setProvince(deviceInfoBean.getSheng());
                    deviceInfo.setCity(deviceInfoBean.getShi());
                    deviceInfo.setArea(deviceInfoBean.getQu());
                    deviceInfo.setAdress(deviceInfoBean.getAddress());
                    deviceInfo.setKeFuPhone(deviceInfoBean.getKefu());
                    deviceInfo.setCreate_at(deviceInfoBean.getCreate_at());
                    DeviceInfoController.updateDeviceInfo(deviceInfo);
                    if(deviceInfoBean.getAttach() != null){
                        List<DeskConfigBean> vos = new ArrayList<>();
                        List<DeskInfoBean> list = deviceInfoBean.getAttach();
                        for (DeskInfoBean vo:list) {
                            try {
                                vos.add(new DeskConfigBean(AppTool.getTypeValue(vo.getType()),vo.getType(),vo.getId(),AppTool.getFullMin(deviceInfoBean.getStatus(),vo.getType()),AppTool.getPrice(vo.getType(),deviceInfoBean.getPrice()),"0","0","0",vo.getPercent(),1,vo.getIs_full(),0,"00000000",vo.getUpdate_at()));
                            }catch (NumberFormatException e){
                            }
                        }
                        DeskConfigController.insertDesk(vos);
                    }
                    if(vos == null){
                        vos = new ArrayList<>();
                    }
                    vos.clear();
                    vos.addAll(DeskConfigController.getAllDesks());
                    deskStatusAdapter.notifyDataSetChanged();
                    ToastUtil.showCenterToast(mContext,"副柜信息获取成功");
                }else{
                    ToastUtil.showCenterToast(mContext,"设备信息获取失败");
                }
                isGeting = false;
            }
            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.showCenterToast(mContext,errorMsg);
                isGeting = false;
            }
        });
    }
}
