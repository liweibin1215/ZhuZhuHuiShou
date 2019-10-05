package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lwb.framelibrary.utils.DataTyeConvertUtil;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.device.balance.BalanceManager;
import com.rys.smartrecycler.view.base.BaseTitleFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceDebugFragment extends BaseTitleFragment implements View.OnClickListener{
    public static final String TAG = "BalanceDebugFragment";
    private Spinner spDesk;
    private EditText etSetRange;
    private EditText etSetDevision;
    private EditText etSetLwt;
    private Button btnLock;
    private int deskNo = 1;
    private String[] deskNums;
    private TextView tvShowLock;
    private List<DeskConfigBean> vos;
    public BalanceDebugFragment() {
    }

    @Override
    protected int doThingsBeforeExit() {
        return 0;
    }

    @Override
    protected void onClickBack() {
        startFragmentAndFinishSelf(this,new DeviceDebugFragment(),DeviceDebugFragment.TAG,R.id.fy_admin_id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1000, "电子秤调试");
        initView();
        initListener();
        initDeskNumSpinner();
        return conventView;
    }

    public void initView(){
        etSetRange = conventView.findViewById(R.id.et_set_range);
        etSetDevision = conventView.findViewById(R.id.et_set_devision);
        spDesk = conventView.findViewById(R.id.et_desk);
        tvShowLock = conventView.findViewById(R.id.tv_lock_status);
        etSetLwt = conventView.findViewById(R.id.et_set_lwt);
        btnLock = conventView.findViewById(R.id.btn_lock);
    }
    public void initDeskNumSpinner(){
        vos = DeskConfigController.getAllDesks();
        deskNums = new String[]{"1号副柜","2号副柜","3号副柜","4号副柜","5号副柜","6号副柜"};
        ArrayAdapter<String> comAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, deskNums);
        comAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spDesk.setAdapter(comAdapter);
        spDesk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deskNo = position;
                if(vos != null && vos.size() > position){
                    if(vos.get(position).getLockStatus() == 1){
                        tvShowLock.setTextColor(mContext.getResources().getColor(R.color.red_error));
                        btnLock.setVisibility(View.VISIBLE);
                        btnLock.setText("解锁");
                        tvShowLock.setText("已锁定");
                    }else{
                        tvShowLock.setTextColor(mContext.getResources().getColor(R.color.green_formal));
                        btnLock.setVisibility(View.VISIBLE);
                        btnLock.setText("锁定");
                        tvShowLock.setText("未锁定");
                    }
                }else{
                    tvShowLock.setTextColor(mContext.getResources().getColor(R.color.red_error));
                    tvShowLock.setText("未添加");
                    btnLock.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_balance_debug;
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
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                openDevice();
                break;
            case R.id.btn_close:
                closeDevice();
                break;
            case R.id.btn_get_weight:
                getWeight();
                break;
            case R.id.btn_set_zero:
                setZero();
                break;
            case R.id.btn_get_range:
                getRange();
                break;
            case R.id.btn_set_range:
                setRange();
                break;
            case R.id.btn_set_devision:
                setDivision();
                break;
            case R.id.btn_set_ldw:
                setEmptyLdw();
                break;
            case R.id.btn_set_lwt:
                setValueLwt();
                break;
        }
    }

    public void initListener(){
        conventView.findViewById(R.id.btn_open).setOnClickListener(this);
        conventView.findViewById(R.id.btn_close).setOnClickListener(this);
        conventView.findViewById(R.id.btn_get_weight).setOnClickListener(this);
        conventView.findViewById(R.id.btn_set_zero).setOnClickListener(this);
        conventView.findViewById(R.id.btn_get_range).setOnClickListener(this);
        conventView.findViewById(R.id.btn_set_range).setOnClickListener(this);
        conventView.findViewById(R.id.btn_set_devision).setOnClickListener(this);
        conventView.findViewById(R.id.btn_set_ldw).setOnClickListener(this);
        conventView.findViewById(R.id.btn_set_lwt).setOnClickListener(this);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vos != null && vos.size() > deskNo){
                    if(vos.get(deskNo).getLockStatus() == 0){
                        tvShowLock.setTextColor(mContext.getResources().getColor(R.color.red_error));
                        btnLock.setVisibility(View.VISIBLE);
                        btnLock.setText("解锁");
                        tvShowLock.setText("已锁定");
                        vos.get(deskNo).setLockStatus(1);
                        DeskConfigController.updateDesk(vos.get(deskNo));
                    }else{
                        tvShowLock.setTextColor(mContext.getResources().getColor(R.color.green_formal));
                        btnLock.setVisibility(View.VISIBLE);
                        btnLock.setText("锁定");
                        tvShowLock.setText("未锁定");
                        vos.get(deskNo).setLockStatus(0);
                        vos.get(deskNo).setErrorStatus("00000000");
                        DeskConfigController.updateDesk(vos.get(deskNo));
                    }
                }
            }
        });
    }

    public void openDevice(){
        if(!BalanceManager.getInstance().isDeviceOpened()){
            if(BalanceManager.getInstance().openMainBoardDevice()){
                showToast(1,"设备打开成功");
            }else{
                showToast(1,"设备打开失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    public void closeDevice(){
        if(BalanceManager.getInstance().isDeviceOpened()){
            BalanceManager.getInstance().closeDevice();
        }
        showToast(1,"设备已经关闭");
    }

    public void getWeight(){
        if(BalanceManager.getInstance().isDeviceOpened()){
            String weight = BalanceManager.getInstance().getWeight(0, DataTyeConvertUtil.int2Hex(deskNo,2));
            showToast(2,"当前重量:"+weight);

        }else{
            showToast(1,"请先开启设备");
        }
    }

    public void setZero(){
        if(BalanceManager.getInstance().isDeviceOpened()){
            if(BalanceManager.getInstance().setZero(DataTyeConvertUtil.int2Hex(deskNo,2))){
                showToast(1,"清零成功");
            }else{
                showToast(1,"清零失败");

            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    public void getRange(){
        if(BalanceManager.getInstance().isDeviceOpened()){
            String range = BalanceManager.getInstance().getMaxRange(DataTyeConvertUtil.int2Hex(deskNo,2));
            showToast(2,"当前量程"+range);

        }else{
            showToast(1,"请先开启设备");
        }
    }

    public void setRange(){
        String maxValue = etSetRange.getText().toString().trim();
        if("".equals(maxValue)){
            showToast(1,"最大量程值不能为空");
            return;
        }
        int value = Integer.parseInt(maxValue);
        if(value < 10000){
            showToast(1,"最大量程值不能小于10000g");
            return;
        }
        if(BalanceManager.getInstance().isDeviceOpened()){
            if(BalanceManager.getInstance().setMaxRange(DataTyeConvertUtil.int2Hex(deskNo,2),value)){
                showToast(1,"量程设置成功");
            }else{
                showToast(1,"量程设置失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    public void setDivision(){
        String maxValue = etSetDevision.getText().toString().trim();
        if("".equals(maxValue)){
            showToast(1,"分度值不能为空");
            return;
        }
        int value = Integer.parseInt(maxValue);
        if(value <= 0){
            showToast(1,"分度值不能为0");
            return;
        }else if(value > 100){
            showToast(1,"分度值不能大于100g");
            return;
        }
        if(BalanceManager.getInstance().isDeviceOpened()){
            if(BalanceManager.getInstance().setMinDevision(DataTyeConvertUtil.int2Hex(deskNo,2),value)){
                showToast(1,"分度设置成功");
            }else{
                showToast(1,"分度设置失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void setEmptyLdw(){
        if(BalanceManager.getInstance().isDeviceOpened()){
            if(BalanceManager.getInstance().setLDW(DataTyeConvertUtil.int2Hex(deskNo,2))){
                showToast(1,"空载标定成功");
            }else{
                showToast(1,"空载标定失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void setValueLwt(){
        String maxValue = etSetLwt.getText().toString().trim();
        if("".equals(maxValue)){
            showToast(1,"标定值不能为空");
            return;
        }
        int value = Integer.parseInt(maxValue);
        if(value < 1000){
            showToast(1,"标定值不能小于1000g");
            return;
        }
        if(BalanceManager.getInstance().isDeviceOpened()){
            if(BalanceManager.getInstance().setLWT(DataTyeConvertUtil.int2Hex(deskNo,2),value)){
                showToast(1,"标定成功");
            }else{
                showToast(1,"标定失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
}
