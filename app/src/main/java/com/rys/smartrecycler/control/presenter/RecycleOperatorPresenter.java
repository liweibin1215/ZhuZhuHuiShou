package com.rys.smartrecycler.control.presenter;

import android.content.Context;

import com.lwb.framelibrary.net.subscriber.BaseSubscriber;
import com.lwb.framelibrary.utils.DataTyeConvertUtil;
import com.lwb.framelibrary.view.base.BasePresenterImpl;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.control.api.RecycleOperatorApi;
import com.rys.smartrecycler.control.model.RecycleOperatorModel;
import com.rys.smartrecycler.db.controller.AdminOrderController;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.DeviceInfoController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.retbean.AdminOrder;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.db.retbean.DeviceInfo;
import com.rys.smartrecycler.device.balance.BalanceManager;
import com.rys.smartrecycler.device.board.MainBoardManager;
import com.rys.smartrecycler.inter.RequestCallBackListener;
import com.rys.smartrecycler.tool.AppTool;
import com.rys.smartrecycler.tool.NumberUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建时间：2019/5/24
 * 作者：李伟斌
 * 功能描述:
 */
public class RecycleOperatorPresenter extends BasePresenterImpl<RecycleOperatorApi.View, RecycleOperatorApi.Model> implements RecycleOperatorApi.Presenter<RecycleOperatorApi.View> {
    private DeskConfigBean deskConfigBean;
    private DeviceInfo deviceInfo;
    private int deviceErrorType = 0;//标记故障类型
    private String deskAdress = "";

    public RecycleOperatorPresenter(Context context) {
        super(context);
    }

    @Override
    public RecycleOperatorApi.Model attachModel() {
        return new RecycleOperatorModel(mContext);
    }

    @Override
    public void loadDeskConfigInfo(int deskNo) {
        deskConfigBean = DeskConfigController.getDeskByDeskNo(deskNo);
        deviceInfo = DeviceInfoController.getDeviceInfo();
        if (deviceInfo == null) {
            getView().showDeviceError(0, "设备信息异常");
            return;
        }
        if (deskConfigBean == null) {
            getView().showDeviceError(0, "副柜配置异常");
            return;
        }
        deskAdress = DataTyeConvertUtil.int2Hex(deskConfigBean.getDeskNo(), 2);
        getView().setDeskName(deskConfigBean.getDeskName());
    }

    @Override
    public void recycleFinished() {
        if (isModelAttached()) {
            String recycleSum;
            if (deskConfigBean.getDeskType() == 1) {
                recycleSum = deskConfigBean.getBottleNum();
            } else {
                recycleSum = deskConfigBean.getTotalWeigth();
                if(!"".equals(recycleSum)){
                    recycleSum = String.valueOf(NumberUtil.getDoubleFromat(Double.parseDouble(recycleSum)/1000.0));
                }
            }
            getModel().recycleFinishOk(BaseApplication.getInstance().getTerminalId(), String.valueOf(deskConfigBean.getDeskNo()), String.valueOf(deskConfigBean.getDeskType()), recycleSum, BaseApplication.getInstance().getUserPhone(), new RequestCallBackListener() {
                @Override
                public void onSuccess(Object data) {
                    if (isViewAttached()) {
                        String sum = deskConfigBean.getDeskType() == 1 ? deskConfigBean.getBottleNum() : deskConfigBean.getTotalWeigth();
                        AdminOrderController.insertAdminOrder(new AdminOrder("",BaseApplication.getInstance().getTerminalId(),deskConfigBean.getDeskType(),deskConfigBean.getDeskNo(),0,BaseApplication.getInstance().getUserPhone(),sum,deskConfigBean.getTotalMoney(),System.currentTimeMillis()/1000,0));
                        int percent = MainBoardManager.getInstance().checkDeskUsePercent(deskConfigBean.getPercent(),getView().getDeskNo());
                        BalanceManager.getInstance().setZero(deskAdress);//电子秤清零
                        deskConfigBean.setPercent(0);
                        deskConfigBean.setTotalMoney("0");
                        deskConfigBean.setTotalWeigth("0");
                        deskConfigBean.setBottleNum("0");
                        deskConfigBean.setUpflag(0);
                        DeskConfigController.updateDesk(deskConfigBean);
                        LogController.insrtOperatorLog("回收员回收",BaseApplication.getInstance().getUserPhone()+"成功回收"+deskConfigBean.getDeskNo()+"副柜,回收量为："+sum+(deskConfigBean.getDeskType()==1?"个":"g"));
                        getView().recycleFinished(deskConfigBean.getDeskName(), sum);
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    if (isViewAttached()) {
                        LogController.insrtOperatorLog("回收员回收",msg);
                        getView().showToast(0, msg);
                    }
                }
            });
        }
    }

    @Override
    public void openDeviceDoor() {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            if (deskConfigBean.getDeskType() != 1) {//非饮料瓶副柜，需要电子秤打开
                //如果此时不是选择的饮料瓶，则需开门前计算当前总量
                if (!BalanceManager.getInstance().isDeviceOpened()) {
                    if (!BalanceManager.getInstance().openMainBoardDevice()) {
                        deviceErrorType = 1;//电子秤故障
                        LogController.insrtAlarmLog("回收员回收","电子秤打开失败");
                        emitter.onNext(false);
                        return;
                    }
                }
//                String weight = BalanceManager.getInstance().getWeight(0,deskAdress);
//                if("".equals(weight)){
//                    deviceErrorType = 1;//电子秤故障
//                    emitter.onNext(false);
//                }
//                lastBalanceWeight = Integer.parseInt(weight);
            }
            if (!MainBoardManager.getInstance().isDeviceOpened()) {
                if (!MainBoardManager.getInstance().openMainBoardDevice()) {
                    deviceErrorType = 2;//标记回收们故障
                    LogController.insrtAlarmLog("回收员回收","主板打开失败");
                    emitter.onNext(false);
                    return;
                }
            }
            boolean isDoorOpen = false;
            long openTime = System.currentTimeMillis();
            MainBoardManager.getInstance().openDeskRecycDoor(deskConfigBean.getDeskNo(),3);
            while (System.currentTimeMillis() - openTime < 5000) {
                if (MainBoardManager.getInstance().checkDeskRecycDoorIsClosed(deskConfigBean.getDeskNo()) == 1) {
                    isDoorOpen = true;
                    break;
                }
                Thread.sleep(100);
            }
            if (!isDoorOpen) {
                LogController.insrtAlarmLog("回收员回收","回收门打开失败");
                deviceErrorType = 2;//标记回收们故障
            }
            emitter.onNext(isDoorOpen);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (!aBoolean) {
                            if (isViewAttached()) {
                                if (deviceErrorType == 1) {
                                    getView().showDeviceError(0, "抱歉，电子秤打开故障,现已报修");
                                    deskConfigBean.setLockStatus(1);
                                    deskConfigBean.setUpflag(0);
                                    deskConfigBean.setErrorStatus(AppTool.setError(deskConfigBean.getErrorStatus(),4,"1"));
                                    DeskConfigController.updateDesk(deskConfigBean);
                                } else {
                                    getView().showDeviceError(0, "抱歉,回收门打开故障,现已报修");
                                    deskConfigBean.setLockStatus(1);
                                    deskConfigBean.setUpflag(0);
                                    deskConfigBean.setErrorStatus(AppTool.setError(deskConfigBean.getErrorStatus(),3,"1"));
                                    DeskConfigController.updateDesk(deskConfigBean);
                                }
                            }
                        }
                    }
                });
    }
}
