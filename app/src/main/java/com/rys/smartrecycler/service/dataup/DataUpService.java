package com.rys.smartrecycler.service.dataup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lwb.framelibrary.net.download.FileDownLoadObserver;
import com.lwb.framelibrary.net.execption.ApiException;
import com.lwb.framelibrary.net.response.base.HttpResponse;
import com.lwb.framelibrary.net.subscriber.BaseHttpSubscriber;
import com.lwb.framelibrary.tool.NetUtil;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.constant.CommonConstant;
import com.rys.smartrecycler.db.controller.AdvController;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.controller.SystemSetController;
import com.rys.smartrecycler.db.retbean.AdvInfo;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.db.retbean.LogBean;
import com.rys.smartrecycler.event.AdvertismentEvent;
import com.rys.smartrecycler.event.DataUpEvent;
import com.rys.smartrecycler.event.MqttEvent;
import com.rys.smartrecycler.net.CommonHttpManager;
import com.rys.smartrecycler.net.CommonHttpParse;
import com.rys.smartrecycler.net.bean.AdvBean;
import com.rys.smartrecycler.net.bean.DeviceInitBean;
import com.rys.smartrecycler.tool.AppTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据上传服务
 */
public class DataUpService extends Service {
    private boolean runFlag = true;
    private String terminalId = "";
    private String password = "";
    private String mToken = "";
    private boolean isDeviceInit = false;
    private boolean isUploadLog = false;
    private boolean isUploadDeskInfo = false;
    private boolean isAdvRequest = false;
    private boolean isGuideAdvRequest = false;
    private boolean isDownloadAdv = false;
    private long isCheckMqtt = 0;
    private long lastUnLockTime = 0;
    private String  currentDownload = "";
    private boolean isNeedDownAdv = true;
    private boolean isNeedDownGuideAdv = true;
    private int isNeedDown = 0;
    private Thread dataUpThread = new Thread() {
        public void run() {
            while (runFlag && !isInterrupted()) {
                try {
                    if (!"".equals(terminalId) && !"".equals(mToken)) {
                        updateLogInfo();//日志上传
                        uploadDeskInfo();//副柜信息上传
                        getAdvInfos();//首页广告信息下载
                        getGuideAdvInfos();//操作页面广告信息下载
                        downloadAdv();//广告下载
                    } else {//获取token
                        if (!"".equals(terminalId) && !"".equals(password) && NetUtil.isNetAvaliable(DataUpService.this)) {
                            deviceInit(terminalId, password);//获取token
                        }
                    }
                    if (runFlag && !isInterrupted()) {
                        Thread.sleep(10000);
                    }
                    timeOperator();
                } catch (InterruptedException e) {
                    runFlag = false;
                } catch (Exception e) {
                    try {
                        if (runFlag)
                            Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!dataUpThread.isAlive()) {
            dataUpThread.start();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        terminalId = BaseApplication.getInstance().getTerminalId();
        password = BaseApplication.getInstance().getTerminalIdPwd();
        mToken = BaseApplication.getInstance().getToken();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dataUpThread != null) {
            runFlag = false;
            dataUpThread.interrupt();
            dataUpThread = null;
        }
        EventBus.getDefault().unregister(this);
    }

    public void deviceInit(String deviceSn, String devicePwd) {
        if (isDeviceInit) {
            return;
        }
        isDeviceInit = true;
        CommonHttpManager.commonObserve(CommonHttpManager.getInstance().getmApiService().deviceInit(deviceSn, devicePwd)).subscribe(new BaseHttpSubscriber<DeviceInitBean>() {

            @Override
            public void onSuccess(DeviceInitBean deviceInitBean) {
                if (deviceInitBean != null) {
                    mToken = deviceInitBean.getToken();
                    BaseApplication.getInstance().setToken(deviceInitBean.getToken());
                    if (deviceInitBean.getMqtt() != null) {
                        SystemSetController.addSystemSet("productKey", deviceInitBean.getMqtt().getProductKey(), "产品类型");
                        SystemSetController.addSystemSet("deviceName", deviceInitBean.getMqtt().getDeviceName(), "设备名称");
                        SystemSetController.addSystemSet("deviceSecret", deviceInitBean.getMqtt().getDeviceSecret(), "设备秘钥");
                        EventBus.getDefault().post(new MqttEvent(1000, deviceInitBean.getMqtt().getProductKey(), deviceInitBean.getMqtt().getDeviceName(), deviceInitBean.getMqtt().getDeviceSecret()));
                    }
                }
                isDeviceInit = false;
            }

            @Override
            public void onError(int code, String errorMsg) {
                isDeviceInit = false;
            }
        });
    }

    public void updateLogInfo() {
        if (isUploadLog) {
            return;
        }
        isUploadLog = true;
        final List<LogBean> vos = LogController.getNeedUpload();
        if (vos == null || vos.size() <= 0) {
            isUploadLog = false;
            return;
        }
        String number = BaseApplication.getInstance().getTerminalId();
        JSONArray jsonArray = new JSONArray();
        try {
            for (LogBean vo : vos) {
                JSONObject object = new JSONObject();
                object.put("number", number);
                object.put("type", vo.getLogType());
                object.put("module", vo.getLogName());
                object.put("content", vo.getLogDesc());
                jsonArray.put(object);
            }
        } catch (JSONException e) {
        }
        Map<String, Object> params = new HashMap<>();
        params.put("content", jsonArray.toString());
        CommonHttpManager.observeWithNoDataTrans(CommonHttpManager.getInstance().getmApiService().appLogUpload(params)).subscribe(new BaseHttpSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                HttpResponse baseBean = CommonHttpParse.parseCommonHttpResult(s);
                if (baseBean != null && baseBean.isSuccess()) {
                    LogController.deleteLogBean(vos);
                } else {
                    updateLogConfigUpflag(vos, 10);
                }
                isUploadLog = false;
            }

            @Override
            public void onError(int code, String errorMsg) {
                updateLogConfigUpflag(vos, 10);
                isUploadLog = false;
            }
        });
    }

    public void uploadDeskInfo() {
        if (isUploadDeskInfo) {
            return;
        }
        isUploadDeskInfo = true;
        final List<DeskConfigBean> vos = DeskConfigController.getAllDesks();
        if (vos == null || vos.size() <= 0) {
            isUploadDeskInfo = false;
            return;
        }
        int fullNum = 0;
        String isFull;
        JSONArray jsonArray = new JSONArray();
        for (DeskConfigBean vo : vos) {
            if (vo.getFullStatus() == 1) {
                fullNum++;
            }
            if (vo.getUpflag() == 0) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("index", vo.getDeskNo());
                    object.put("type", AppTool.getTypeName(String.valueOf(vo.getDeskType())));
                    object.put("sum", vo.getDeskType() == 1 ? vo.getBottleNum() : vo.getTotalWeigth());
                    object.put("err_type", vo.getErrorStatus());
                    object.put("is_full", vo.getFullStatus());
                    object.put("percent", vo.getPercent());
                    jsonArray.put(object);
                } catch (JSONException e) {
                }
            }
        }
        if (jsonArray.length() <= 0) {
            isUploadDeskInfo = false;
            return;
        }
        if (fullNum == 0) {
            isFull = "0";//无箱满
        } else if (fullNum == vos.size()) {
            isFull = "2";//全部箱满
        } else {
            isFull = "1";//未全满
        }
        Map<String, Object> params = new HashMap<>();
        params.put("number", BaseApplication.getInstance().getTerminalId());
        params.put("is_full", isFull);
        params.put("attach", jsonArray.toString());
        CommonHttpManager.observeWithNoDataTrans(CommonHttpManager.getInstance().getmApiService().uploadDeskInfo(params)).subscribe(new BaseHttpSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                HttpResponse baseBean = CommonHttpParse.parseCommonHttpResult(s);
                if (baseBean != null && baseBean.isSuccess()) {
                    updateDeskConfigUpflag(vos, 1);
                } else {
                    updateDeskConfigUpflag(vos, 10);//锁定
                }
                isUploadLog = false;
            }

            @Override
            public void onError(int code, String errorMsg) {
                updateDeskConfigUpflag(vos, 10);//锁定
                isUploadLog = false;
            }
        });
    }

    public void getAdvInfos() {
        if (!isNeedDownAdv || isAdvRequest) {
            return;
        }
        isAdvRequest = true;
        CommonHttpManager.commonObserve(CommonHttpManager.getInstance().getmApiService().getAdvInfos(1, BaseApplication.getInstance().getTerminalId())).subscribe(new BaseHttpSubscriber<AdvBean>() {
            @Override
            public void onSuccess(AdvBean vo) {
                if (vo != null) {
                    List<AdvInfo> vos = AdvController.getAllAdvInfos(1);
                    if (vos == null || vos.size() <= 0 || !vo.getUpdate_at().equals(vos.get(0).getUpdate_at())) {
                        if(vos != null && vos.size() > 0){
                            AdvController.deleteAll(vos);
                        }
                        AdvController.insertAdvInfosWithUniquea(new AdvInfo(vo.getId(), vo.getName(), vo.getImg(), "", vo.getVideo(), "", vo.getStart_time(), vo.getEnd_time(), vo.getCreate_at(),vo.getUpdate_at(), 0, 0, "", 1));
                    }
                }
                isNeedDown++;
                isNeedDownAdv = false;
                isAdvRequest = true;
            }

            @Override
            public void onError(int code, String errorMsg) {
                if(code == ApiException.Code_Data_Null){
                    isNeedDownAdv = false;
                }
                isNeedDown++;
                isAdvRequest = true;
            }
        });
    }

    public void getGuideAdvInfos() {
        if (!isNeedDownGuideAdv || isGuideAdvRequest) {
            return;
        }
        isGuideAdvRequest = true;
        CommonHttpManager.commonObserve(CommonHttpManager.getInstance().getmApiService().getAdvInfos(2, BaseApplication.getInstance().getTerminalId())).subscribe(new BaseHttpSubscriber<AdvBean>() {
            @Override
            public void onSuccess(AdvBean vo) {
                if (vo != null) {
                    List<AdvInfo> vos = AdvController.getAllAdvInfos(2);
                    if (vos.size() <= 0 || !vo.getUpdate_at().equals(vos.get(0).getUpdate_at())) {
                        AdvController.deleteAll(vos);
                        AdvController.insertAdvInfosWithUniquea(new AdvInfo(vo.getId(), vo.getName(), vo.getImg(), "", vo.getVideo(), "", vo.getStart_time(), vo.getEnd_time(), vo.getCreate_at(),vo.getUpdate_at(), 0, 0, "", 2));
                    }
                }
                isNeedDownGuideAdv = false;
                isGuideAdvRequest = false;
                isNeedDown++;
            }

            @Override
            public void onError(int code, String errorMsg) {
                if(code == ApiException.Code_Data_Null){
                    isNeedDownGuideAdv = false;
                }
                isGuideAdvRequest = false;
                isNeedDown++;
            }
        });
    }

    public void downloadAdv() {
        if (isDownloadAdv || isNeedDown < 2) {
            return;
        }
        isDownloadAdv = true;
        currentDownload = getNeedDownloadAdrss();//获取待下载的地址
        if("".equals(currentDownload)){
            isDownloadAdv = false;
            return;
        }
        String fileName = currentDownload.substring(currentDownload.lastIndexOf("/") + 1);
        CommonHttpManager.downloadFile(CommonHttpManager.getInstance().getmApiService().downLoadFile(currentDownload), CommonConstant.SYSTEM_SOURSE_PATH, fileName, new FileDownLoadObserver<File>() {
            @Override
            public int onDownLoadSuccess(File file) {
                advDownloadSuccess(true);
                isDownloadAdv = false;
                return 0;
            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
                advDownloadSuccess(false);
                isDownloadAdv = false;
            }

            @Override
            public void onProgress(int progress, long total) {

            }
        });
    }

    public String getNeedDownloadAdrss(){
        List<AdvInfo> vos = AdvController.getAllNeedDownloadInfos();
        if (vos == null || vos.size() <= 0) {
            return "";
        }
        String lastDownload = "";
        int position = -1;
        for (int i = 0; i < vos.size(); i++) {
            lastDownload = vos.get(i).getLastDownloadImg();
            String[] picStrs = vos.get(i).getImg().split(",");
            if (picStrs != null && picStrs.length > 0) {
                for (int j = 0; j < picStrs.length; j++) {
                    if(picStrs[j] == null || !picStrs[j].startsWith("http")){
                        continue;
                    }
                    if("".equals(lastDownload)){
                        return picStrs[j];
                    }
                    if(position == -1 && lastDownload.equals(picStrs[j])){
                        position = j;
                        continue;
                    }
                    if(position != -1){
                        return picStrs[j];
                    }
                }
            }
            String[] veidoStrs = vos.get(i).getVideo().split(",");
            if (veidoStrs != null && veidoStrs.length > 0) {
                for (int j = 0; j < veidoStrs.length; j++) {
                    if(veidoStrs[j] == null || !veidoStrs[j].startsWith("http")){
                        continue;
                    }
                    if("".equals(lastDownload)){
                        return veidoStrs[j];
                    }
                    if(position == -1 && lastDownload.equals(veidoStrs[j])){
                        position = j;
                        continue;
                    }
                    if(position != -1){
                        return veidoStrs[j];
                    }
                }
            }
        }
        for (int i = 0; i < vos.size(); i++) {
            String[] picStrs = vos.get(i).getImg().split(",");
            if (picStrs != null && picStrs.length > 0) {
                for (int j = 0; j < picStrs.length; j++) {
                    if("".equals(picStrs[j]) || !picStrs[j].startsWith("http")){
                        continue;
                    }
                    if(!"".equals(picStrs[j])){
                        return picStrs[j];
                    }
                }
            }
            String[] veidoStrs = vos.get(i).getVideo().split(",");
            if (veidoStrs != null && veidoStrs.length > 0) {
                for (int j = 0; j < veidoStrs.length; j++) {
                    if("".equals(veidoStrs[j]) || !veidoStrs[j].startsWith("http")){
                        continue;
                    }
                    if(!"".equals(veidoStrs[j])){
                        return veidoStrs[j];
                    }
                }
            }
        }
        for (int i=0;i<vos.size();i++){
            vos.get(i).setUpflag(1);
        }
        AdvController.updateAdvInfos(vos);
        EventBus.getDefault().post(new AdvertismentEvent(1000,"ok"));
        return "";
    }

    public void advDownloadSuccess(boolean isSuccess){
        List<AdvInfo> vos = AdvController.getAllNeedDownloadInfos();
        if (vos == null || vos.size() <= 0) {
            isDownloadAdv = false;
            return;
        }
        int position = -1;
        StringBuilder builder = new StringBuilder();
        String fileName = currentDownload.substring(currentDownload.lastIndexOf("/") + 1);
        for (int i=0;i < vos.size();i++){
            String[] picStrs = vos.get(i).getImg().split(",");
            for (int j=0;j<picStrs.length;j++){
                if(picStrs[j] == null || !picStrs[j].startsWith("http")){
                    continue;
                }
                if(picStrs[j].equals(currentDownload)){
                    position = j;
                    if(!isSuccess){
                        vos.get(i).setErrorTimes(vos.get(i).getErrorTimes()+1);
                        if(vos.get(i).getErrorTimes() > 20){
                            EventBus.getDefault().post(new AdvertismentEvent(1000,"ok"));
                            vos.get(i).setUpflag(10);
                        }
                        builder.append(builder.length() <= 0?picStrs[j]:(","+picStrs[j]));
                    }else{
                        if(vos.get(i).getImgPath() == null  || "".equals(vos.get(i).getImgPath())){
                            vos.get(i).setImgPath(fileName);
                        }else{
                            vos.get(i).setImgPath(vos.get(i).getImgPath()+","+fileName);
                        }
                    }
                }else{
                    builder.append(builder.length() <= 0?picStrs[j]:(","+picStrs[j]));
                }
            }
            vos.get(i).setImg(builder.toString());
            builder = new StringBuilder();
            if(position < 0){
                String[] veidoStrs = vos.get(i).getVideo().split(",");
                for (int j=0;j<veidoStrs.length;j++){
                    if(veidoStrs[j] == null || !veidoStrs[j].startsWith("http")){
                        continue;
                    }
                    if(veidoStrs[j].equals(currentDownload)){
                        position = j;
                        if(!isSuccess){
                            vos.get(i).setErrorTimes(vos.get(i).getErrorTimes()+1);
                            if(vos.get(i).getErrorTimes() > 20){
                                vos.get(i).setUpflag(10);
                            }
                            builder.append(builder.length() <= 0?veidoStrs[j]:(","+veidoStrs[j]));
                        }else{
                            if(vos.get(i).getVideoPath() == null || "".equals(vos.get(i).getVideoPath())){
                                vos.get(i).setVideoPath(fileName);
                            }else{
                                vos.get(i).setVideoPath(","+fileName);
                            }
                        }
                    }else{
                        builder.append(builder.length() <= 0?veidoStrs[j]:(","+veidoStrs[j]));
                    }
                }
                vos.get(i).setVideo(builder.toString());
            }
            if(position >= 0){
                if(isSuccess){
                    vos.get(i).setLastDownloadImg("");
                }else{
                    vos.get(i).setLastDownloadImg(currentDownload);
                }
                AdvController.updateAdvInfos(vos.get(i));
            }
        }
    }

    public void updateDeskConfigUpflag(List<DeskConfigBean> vos, int upflag) {
        for (int i = 0; i < vos.size(); i++) {
            vos.get(i).setUpflag(upflag);
        }
        DeskConfigController.updateDeskInfos(vos);
    }

    public void updateLogConfigUpflag(List<LogBean> vos, int upflag) {
        for (int i = 0; i < vos.size(); i++) {
            vos.get(i).setUpflag(upflag);
        }
        LogController.updateLogInfo(vos);
    }

    public void updateAdvoInfosUpflag(List<AdvInfo> vos, int upflag) {
        for (int i = 0; i < vos.size(); i++) {
            vos.get(i).setUpflag(upflag);
        }
        AdvController.updateAdvInfos(vos);
    }
    public void timeOperator() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastUnLockTime > 300000) {
            List<DeskConfigBean> vos = DeskConfigController.getAllNeedUnLockDesks();
            if (vos.size() > 0) {
                updateDeskConfigUpflag(vos, 0);
            }
            List<LogBean> logs = LogController.getAllNeedUnLockLogBean();
            if (logs.size() > 0) {
                updateLogConfigUpflag(logs, 0);
            }
            List<AdvInfo> advInfos = AdvController.getNeedUnlockInfos();
            if (advInfos.size() > 0){
                updateAdvoInfosUpflag(advInfos,0);
            }
            lastUnLockTime = System.currentTimeMillis();
            if (isCheckMqtt != 0 && !BaseApplication.getInstance().isMqttConnect()) {
                EventBus.getDefault().post(new MqttEvent(1001, "", "", ""));
            }else{
                isCheckMqtt = 1;
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mqttEvent(DataUpEvent event){
        if(event != null && event.getCode() == 1000){
            isNeedDownAdv = true;
            isNeedDownGuideAdv = true;
        }
    }
}
