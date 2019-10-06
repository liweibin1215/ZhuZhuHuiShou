package com.rys.smartrecycler.control.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.lwb.framelibrary.net.subscriber.BaseSubscriber;
import com.lwb.framelibrary.utils.DataTyeConvertUtil;
import com.lwb.framelibrary.view.base.BasePresenterImpl;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.control.api.FrontDeliveryApi;
import com.rys.smartrecycler.control.model.FrontDeliveryModel;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.controller.UserOrderController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.db.retbean.UserOrder;
import com.rys.smartrecycler.device.balance.BalanceManager;
import com.rys.smartrecycler.device.board.MainBoardManager;
import com.rys.smartrecycler.inter.RequestCallBackListener;
import com.rys.smartrecycler.tool.AppTool;
import com.rys.smartrecycler.tool.NumberUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建时间：2019/5/24
 * 作者：李伟斌
 * 功能描述:
 */
public class FrontDeliveryPresenter extends BasePresenterImpl<FrontDeliveryApi.View,FrontDeliveryApi.Model> implements FrontDeliveryApi.Presenter<FrontDeliveryApi.View>{
    private DeskConfigBean deskConfigBean;
    private int deviceErrorType = 0;//标记故障类型
    private int lastBalanceWeight = 0;//标记开门前的重量
    private int currentBalanceWeight = 0;//当前称重重量
    private int currentDeliverNum = 0;//当前投放瓶子个数
    private String deskAdress = "";
    private MyBoardTask myBoardTask;//接收异步任务
    private double totalMoney = 0;
    private String num;
    public FrontDeliveryPresenter(Context context) {
        super(context);
    }

    @Override
    public FrontDeliveryApi.Model attachModel() {
        return new FrontDeliveryModel(mContext);
    }

    @Override
    public void loadDeskConfigInfo(int deskNo) {
        deskConfigBean = DeskConfigController.getDeskByDeskNo(deskNo);
        if(deskConfigBean == null){
            LogController.insrtAlarmLog("用户投递","设备配置异常");
            getView().showDeviceError(0,"设备配置异常");
            return;
        }
        deskAdress = DataTyeConvertUtil.int2Hex(deskConfigBean.getDeskNo(),2);
        switch(deskConfigBean.getDeskType()){
            case 1:
                getView().initDeskType1Icon();
                break;
            case 2:
                getView().initDeskType2Icon();
                break;
            case 3:
                getView().initDeskType3Icon();
                break;
            case 4:
                getView().initDeskType4Icon();
                break;
            case 5:
                getView().initDeskType5Icon();
                break;
            case 6:
                getView().initDeskType6Icon();
                break;
        }
    }

    @Override
    public void confirmDeliveryOk() {
        if(isModelAttached()){

            if(deskConfigBean.getDeskType() == 1){
                if(currentDeliverNum <= 0){
                    getView().deliveryFinished(deskConfigBean.getDeskType(),1,deskConfigBean.getDeskName(),"0个","0");
                    return;
                }
                num = String.valueOf(currentDeliverNum);
            }else{
                if(currentBalanceWeight-lastBalanceWeight < 60){
                    getView().deliveryFinished(deskConfigBean.getDeskType(),1,deskConfigBean.getDeskName(),"0g","0");
                    return;
                }
                num = String.valueOf(NumberUtil.getDoubleFromat((currentBalanceWeight-lastBalanceWeight)/1000.0));
            }
            double price = !"".equals(deskConfigBean.getUnitPrice())?Double.parseDouble(deskConfigBean.getUnitPrice()):0.01;
            if(deskConfigBean.getDeskType() == 1){
                totalMoney = NumberUtil.getDoubleFromat(price*currentDeliverNum);
                int totalSum = "".equals(deskConfigBean.getBottleNum())?currentDeliverNum:Integer.parseInt(deskConfigBean.getBottleNum())+currentDeliverNum;
                deskConfigBean.setBottleNum(String.valueOf(totalSum));
            }else{
                int totalWeight = 0;
                String lastWeight = deskConfigBean.getTotalWeigth();
                totalMoney = NumberUtil.getDoubleFromat(price*((currentBalanceWeight-lastBalanceWeight)/1000.0));
                if(!"".equals(lastWeight)){
                    totalWeight = Integer.parseInt(lastWeight)+currentBalanceWeight-lastBalanceWeight;
                }else{
                    totalWeight = currentBalanceWeight-lastBalanceWeight;
                }
                deskConfigBean.setTotalWeigth(String.valueOf(totalWeight));
            }
            if(!"".equals(deskConfigBean.getTotalMoney())){
                double money = totalMoney+Double.parseDouble(deskConfigBean.getTotalMoney());
                deskConfigBean.setTotalMoney(String.valueOf(money));
            }else{
                deskConfigBean.setTotalMoney(String.valueOf(totalMoney));
            }
            getModel().createDeliveryOrder(BaseApplication.getInstance().getTerminalId(), String.valueOf(deskConfigBean.getDeskNo()), String.valueOf(deskConfigBean.getDeskType()), num,  BaseApplication.getInstance().getUserPhone(),totalMoney, new RequestCallBackListener() {
                @Override
                public void onSuccess(Object data) {
                    if(isViewAttached()){
                        int percent = MainBoardManager.getInstance().checkDeskUsePercent(deskConfigBean.getPercent(),deskConfigBean.getDeskNo());
                        if(percent > 90){
                            deskConfigBean.setFullStatus(1);
                        }else{
                            deskConfigBean.setFullStatus(0);
                        }
                        deskConfigBean.setUpflag(0);
                        deskConfigBean.setPercent(percent);
                        DeskConfigController.updateDesk(deskConfigBean);
                        UserOrderController.insertUserOrder(new UserOrder("",BaseApplication.getInstance().getTerminalId(),deskConfigBean.getDeskType(),deskConfigBean.getDeskNo(),0,BaseApplication.getInstance().getUserPhone(),num,String.valueOf(totalMoney),System.currentTimeMillis()/1000,0));
                        if(deskConfigBean.getDeskType() == 1){
                            getView().deliveryFinished(deskConfigBean.getDeskType(),0,deskConfigBean.getDeskName(),currentDeliverNum+"个",String.valueOf(totalMoney));
                        }else{
                            getView().deliveryFinished(deskConfigBean.getDeskType(),0,deskConfigBean.getDeskName(),(currentBalanceWeight-lastBalanceWeight)+"g",String.valueOf(totalMoney));
                        }
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    if(isViewAttached()){
                        LogController.insrtOperatorLog("用户投递",msg);
                        getView().showToast(0,msg);
                    }
                }
            });
        }
    }

    @Override
    public void openDeviceDoor(){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                if (deskConfigBean.getDeskType() != 1) {//非饮料瓶副柜，需要电子秤打开
                    //如果此时不是选择的饮料瓶，则需开门前计算当前总量
                    if (!BalanceManager.getInstance().isDeviceOpened()) {
                        if (!BalanceManager.getInstance().openMainBoardDevice()) {
                            LogController.insrtAlarmLog("用户投递", "电子秤打开失败");
                            deviceErrorType = 1;//电子秤故障
                            emitter.onNext(false);
                            return;
                        }
                    }
                    String weight = BalanceManager.getInstance().getWeight(0, deskAdress);
                    if ("".equals(weight)) {
                        LogController.insrtAlarmLog("用户投递", "重量获取失败");
                        deviceErrorType = 1;//电子秤故障
                        emitter.onNext(false);
                        return;
                    }
                    lastBalanceWeight = Integer.parseInt(weight);
                    if (lastBalanceWeight < 0) {
                        BalanceManager.getInstance().setZero(deskAdress);
                        weight = BalanceManager.getInstance().getWeight(0, deskAdress);
                        if ("".equals(weight)) {
                            LogController.insrtAlarmLog("用户投递", "重量获取失败");
                            deviceErrorType = 1;//电子秤故障
                            emitter.onNext(false);
                            return;
                        }
                        lastBalanceWeight = Integer.parseInt(weight);
                    }
                }
                if (!MainBoardManager.getInstance().isDeviceOpened()) {
                    if (!MainBoardManager.getInstance().openMainBoardDevice()) {
                        LogController.insrtAlarmLog("用户投递", "主板打开失败");
                        deviceErrorType = 2;//标记回收们故障
                        emitter.onNext(false);
                        return;
                    }
                }
                boolean isDoorOpen = false;
                long openTime = System.currentTimeMillis();
                MainBoardManager.getInstance().openDeskDoor(deskConfigBean.getDeskNo());
                while (System.currentTimeMillis() - openTime < 6000) {
                    if (MainBoardManager.getInstance().checkDeskDoorStatus(deskConfigBean.getDeskNo()) == 0) {
                        isDoorOpen = true;
                        break;
                    }
                    Thread.sleep(100);
                }
                if (!isDoorOpen) {
                    LogController.insrtAlarmLog("用户投递", "投递口打开失败");
                    deviceErrorType = 2;//标记回收们故障
                }
                emitter.onNext(isDoorOpen);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if(aBoolean){
                            startReciver();
                        }else{
                            if(isViewAttached()){
                                if(deviceErrorType == 1){
                                    deskConfigBean.setErrorStatus(AppTool.setError(deskConfigBean.getErrorStatus(),4,"1"));
                                    getView().showDeviceError(0,"抱歉,电子秤打开故障,现已报修");
                                }else{
                                    deskConfigBean.setErrorStatus(AppTool.setError(deskConfigBean.getErrorStatus(),2,"1"));
                                    getView().showDeviceError(0,"抱歉,投递门打开故障,现已报修");
                                }
                                deskConfigBean.setLockStatus(1);
                                deskConfigBean.setUpflag(0);
                                DeskConfigController.updateDesk(deskConfigBean);
                            }
                        }
                    }
                });
    }

    @Override
    public void stopReceivertask() {
        if(myBoardTask != null){
            myBoardTask.cancel(true);
            myBoardTask.stopMyTask();
            myBoardTask = null;
        }
    }

    public void startReciver(){
        if(myBoardTask != null){
            myBoardTask.stopMyTask();
            myBoardTask = null;
        }
        myBoardTask = new MyBoardTask(deskConfigBean.getDeskType()== 1?0:1);
        myBoardTask.execute();
    }


    /**
     * 异步任务
     */
    public class MyBoardTask extends AsyncTask<String,String,Integer>{
        private boolean runflag = true;
        private int     cmdType = 0;
        public MyBoardTask(int cmdType){
            this.cmdType = cmdType;
        }
        public void stopMyTask(){
            this.runflag = false;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            while (!isCancelled() && runflag){
                try {
                    if(cmdType == 0){
                        if(MainBoardManager.getInstance().recvBottleIn(1000) >0){
                            publishProgress("1");
                        }
                    }else{
                        publishProgress(BalanceManager.getInstance().getWeight(lastBalanceWeight,deskAdress));
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(cmdType == 0){
                if(values.length >0 && "1".equals(values[0])){
                    currentDeliverNum++;
                    if(isViewAttached())
                        getView().setCurrentDeliveryNum(currentDeliverNum+"个");
                }
            }else{
                if(values.length >0 && !"".equals(values[0])){
                    if(isViewAttached()){
                        currentBalanceWeight = Integer.parseInt(values[0]);
                        getView().setCurrentDeliveryNum((currentBalanceWeight - lastBalanceWeight) +"g");
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }
}
