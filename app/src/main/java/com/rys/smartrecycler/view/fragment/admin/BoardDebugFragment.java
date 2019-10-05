package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.device.board.MainBoardManager;
import com.rys.smartrecycler.view.base.BaseTitleFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardDebugFragment extends BaseTitleFragment implements View.OnClickListener {
    public static final String TAG = "BoardDebugFragment";
    private Spinner spDesk;
    private TextView tvShowLock;
    private Button   btnLock;
    private int deskNo = 1;
    private String[] deskNums;
    private List<DeskConfigBean> vos;
    public BoardDebugFragment() {
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
        mAcFragmentCall.excuteFragmentAction(1000, "主控板调试");
        initView();
        initListener();
        initDeskNumSpinner();
        return conventView;
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

    public void initView(){
        spDesk = conventView.findViewById(R.id.et_desk);
        tvShowLock = conventView.findViewById(R.id.tv_lock_status);
        btnLock = conventView.findViewById(R.id.btn_lock);
    }
    public void initListener(){
        conventView.findViewById(R.id.btn_open).setOnClickListener(this);
        conventView.findViewById(R.id.btn_close).setOnClickListener(this);
        conventView.findViewById(R.id.btn_open_door).setOnClickListener(this);
        conventView.findViewById(R.id.btn_close_door).setOnClickListener(this);
        conventView.findViewById(R.id.btn_check_door).setOnClickListener(this);
        conventView.findViewById(R.id.btn_check_full).setOnClickListener(this);
        conventView.findViewById(R.id.btn_check_percent).setOnClickListener(this);
        conventView.findViewById(R.id.btn_open_light).setOnClickListener(this);
        conventView.findViewById(R.id.btn_close_light).setOnClickListener(this);
        conventView.findViewById(R.id.btn_open_recycle_door).setOnClickListener(this);
        conventView.findViewById(R.id.btn_check_recycle_door).setOnClickListener(this);
        conventView.findViewById(R.id.btn_get_version).setOnClickListener(this);
        conventView.findViewById(R.id.btn_get_type).setOnClickListener(this);
        conventView.findViewById(R.id.btn_get_temp).setOnClickListener(this);
        conventView.findViewById(R.id.btn_light_status).setOnClickListener(this);
        btnLock.setOnClickListener(v -> {
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
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_board_debug;
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
        switch (view.getId()) {
            case R.id.btn_open:
                openDevice();
                break;
            case R.id.btn_close:
                closeDevice();
                break;
            case R.id.btn_open_door:
                openDoor();
                break;
            case R.id.btn_close_door:
                closeDoor();
                break;
            case R.id.btn_check_door:
                checkDoor();
                break;
            case R.id.btn_check_full:
                checkFull();
                break;
            case R.id.btn_check_percent:
                checkPercent();
                break;
            case R.id.btn_open_light:
                openLight();
                break;
            case R.id.btn_close_light:
                closeLight();
                break;
            case R.id.btn_open_recycle_door:
                openRecycleDoor();
                break;
            case R.id.btn_check_recycle_door:
                checkRecycleDoor();
                break;
            case R.id.btn_get_version:
                getDeviceVersion();
                break;
            case R.id.btn_get_type:
                getDeviceType();
                break;
            case R.id.btn_get_temp:
                getDeviceTemp();
                break;
            case R.id.btn_light_status:
                checkDeviceLightStatus();
                return;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void openDevice(){
        if(!MainBoardManager.getInstance().isDeviceOpened()){
            if(MainBoardManager.getInstance().openMainBoardDevice()){
                showToast(1,"设备打开成功");
            }else{
                showToast(1,"设备打开失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    public void closeDevice(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            MainBoardManager.getInstance().closeDevice();
        }
        showToast(1,"设备已经关闭");
    }
    public void openDoor(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            if(MainBoardManager.getInstance().openDeskDoor(deskNo) != -1){
                showToast(1,"投递门打开成功");
            }else{
                showToast(1,"投递门打开失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void closeDoor(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            if(MainBoardManager.getInstance().closeDeskDoor(deskNo) != -1){
                showToast(1,"投递门关闭成功");
            }else{
                showToast(1,"投递门关闭失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void checkDoor(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int checkStatus = MainBoardManager.getInstance().checkDeskDoorStatus(deskNo);
            if(checkStatus == 0){
                showToast(1,"投递门已关闭");
            }else if(checkStatus == 1){
                showToast(1,"投递门已打开");
            }else{
                showToast(1,"设备检测异常");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void checkFull(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().checkDeskIsFull(deskNo);
            if(status == -1){
                showToast(1,"设备检测异常");
            }else{
                showToast(1,"箱满状态："+(status==0?"已满":"未满"));
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void checkPercent(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().checkDeskUsePercent(0,deskNo);
            if(status == -1){
                showToast(1,"设备检测异常");
            }else{
                showToast(1,"占箱比例："+status+"%");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void openLight(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().openDeviceLight(deskNo,3,true);
            if(status == 0){
                showToast(1,"照明灯打开成功");
            }else{
                showToast(1,"照明灯打开失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void closeLight(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().openDeviceLight(deskNo,3,false);
            if(status == 0){
                showToast(1,"照明灯关闭成功");
            }else{
                showToast(1,"照明灯关闭失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void openRecycleDoor(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().openDeskRecycDoor(deskNo,3);
            if(status == 0){
                showToast(1,"回收门打开成功");
            }else{
                showToast(1,"回收门打开失败");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
    public void checkRecycleDoor(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().checkDeskRecycDoorIsClosed(deskNo);
            if(status == 0){
                showToast(1,"回收门关闭");
            }else if(status == 1){
                showToast(1,"回收门打开");
            }else{
                showToast(1,"设备检测异常");
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    /**
     * 获取版本信息
     */
    public void getDeviceVersion(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            String status = MainBoardManager.getInstance().getBoardVersion(deskNo);
            if("".equals(status)){
                showToast(1,"设备检测异常");
            }else{
                showToast(1,"软硬件版本："+status);
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    /**
     * 获取副柜类型
     */
    public void getDeviceType(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            String status = MainBoardManager.getInstance().getBoardType(deskNo);
            if("".equals(status)){
                showToast(1,"设备检测异常");
            }else{
                showToast(1,"柜体类型："+status);
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    /**
     * 获取副柜内部温度
     */
    public void getDeviceTemp(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().getDeviceTemperature(deskNo);
            if("".equals(status)){
                showToast(1,"设备检测异常");
            }else{
                showToast(1,"内部温度："+status);
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }

    /**
     * 获取照明灯开关状态
     */
    public void checkDeviceLightStatus(){
        if(MainBoardManager.getInstance().isDeviceOpened()){
            int status = MainBoardManager.getInstance().getDeviceLightStatus(deskNo);
            if("".equals(status)){
                showToast(1,"设备检测异常");
            }else{
                showToast(1,"照明灯："+(status==0?"灭":"亮"));
            }
        }else{
            showToast(1,"请先开启设备");
        }
    }
}
