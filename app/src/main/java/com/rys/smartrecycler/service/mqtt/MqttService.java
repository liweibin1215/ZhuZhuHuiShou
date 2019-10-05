package com.rys.smartrecycler.service.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.IoTApiClientConfig;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRegisterRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.log.IDGenerater;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.controller.SystemSetController;
import com.rys.smartrecycler.event.DataUpEvent;
import com.rys.smartrecycler.event.LoginEvent;
import com.rys.smartrecycler.event.MqttEvent;
import com.rys.smartrecycler.tool.AppTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MqttService extends Service {
    private String productKey = "";//产品类型a1jVP4SVGEK
    private String deviceName = "";//设备名称
    private String deviceSecret = "";//设备秘钥
    private boolean isDeviceIniting = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LinkKit.getInstance().deinit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mqttEvent(MqttEvent event){
        if(event != null && event.getCode() == 1000){
            productKey  = event.getProductKey();
            deviceName =  event.getDeviceName();
            deviceSecret = event.getDeviceSecret();
            if(!"".equals(productKey) && !"".equals(deviceSecret)){
                initMqtt();
            }
        }else if(event != null && event.getCode() == 1001){
            productKey  = SystemSetController.getSysInfo("productKey");
            deviceName = SystemSetController.getSysInfo("deviceName");
            deviceSecret = SystemSetController.getSysInfo("deviceSecret");
            if(!"".equals(productKey) && !"".equals(deviceSecret)){
                initMqtt();
            }
        }
    }

    public void initMqtt() {
        if(isDeviceIniting){
            return;
        }
        isDeviceIniting = true;
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = productKey;
        deviceInfo.deviceName = deviceName;
        deviceInfo.deviceSecret = deviceSecret;
        IoTApiClientConfig userData = new IoTApiClientConfig();
        IoTMqttClientConfig clientConfig = new IoTMqttClientConfig(productKey, deviceName, deviceSecret);
        Map<String, ValueWrapper> propertyValues = new HashMap<>();
        LinkKitInitParams params = new LinkKitInitParams();
        params.deviceInfo = deviceInfo;
        params.propertyValues = propertyValues;
        params.connectConfig = userData;
        params.mqttClientConfig = clientConfig;
        LinkKit.getInstance().init(this, params, new ILinkKitConnectListener() {
            @Override
            public void onError(AError error) {
                // 初始化失败 error包含初始化错误信息
                Log.d("mqtt消息", "初始化失败");
                BaseApplication.getInstance().setMqttConnect(false);
                isDeviceIniting = false;
            }

            @Override
            public void onInitDone(Object data) {
                Log.d("mqtt消息", "初始化成功");
                cubscribeRRPC();//订阅消息，并及时回复
                isDeviceIniting = false;
            }
        });
    }


    public void cubscribeRRPC() {
        MqttRrpcRegisterRequest registerRequest = new MqttRrpcRegisterRequest();
        registerRequest.topic = "/sys/" + productKey + "/" + deviceName + "/rrpc/request/+";
        registerRequest.payloadObj = "已收到消息";
        registerRequest.replyTopic = "/sys/" + productKey + "/" + deviceName + "/rrpc/response/";
        LinkKit.getInstance().subscribeRRPC(registerRequest, new IConnectRrpcListener() {
            @Override
            public void onSubscribeSuccess(ARequest aRequest) {
                Log.d("mqtt消息", "订阅成功");
                BaseApplication.getInstance().setMqttConnect(true);
            }

            @Override
            public void onSubscribeFailed(ARequest aRequest, AError aError) {
                Log.d("mqtt消息", "订阅失败");
                BaseApplication.getInstance().setMqttConnect(false);
            }

            @Override
            public void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle) {
                MqttRrpcRequest vo = (MqttRrpcRequest) aRequest;
                String data = new String((byte[])vo.payloadObj);
                String result = parseServerCmd(data);
                if (iConnectRrpcHandle != null) {
                    AResponse aResponse = new AResponse();
                    aResponse.data = result;//回复测试数据
                    String messageId = vo.topic.substring(vo.topic.lastIndexOf("/") + 1);
                    iConnectRrpcHandle.onRrpcResponse(vo.replyTopic+messageId, aResponse);
                }
            }

            @Override
            public void onResponseSuccess(ARequest aRequest) {
                Log.d("mqtt消息", "回复成功");
            }

            @Override
            public void onResponseFailed(ARequest aRequest, AError aError) {
                Log.d("mqtt消息", "回复失败");
            }
        });
    }

    public String parseServerCmd(String data){
        if(data == null || "".equals(data)){
            return packageFomalBack();
        }
        try {
            JSONObject object = new JSONObject(data);
            String methon = object.optString("method");
            if("userlogin".equals(methon)){//登录
                JSONObject content = object.optJSONObject("data");
                int type = content.optInt("type");
                int id = content.optInt("id");
                String userAccount = content.optString("phone");
                if(type == 1){//小程序
                    if(BaseApplication.getInstance().getLoginStatus() == 1){
                        EventBus.getDefault().post(new LoginEvent(1000,1,id,userAccount));
                        return packageMqttBack(200,"登录成功");
                    }else{
                        return packageMqttBack(400,"登录失败，请切换到登录页面");
                    }
                }else if(type ==2){//回收员
                    if(BaseApplication.getInstance().getLoginStatus() == 2){
                        EventBus.getDefault().post(new LoginEvent(1001,2,id,userAccount));
                        return packageMqttBack(200,"登录成功");
                    }else{
                        return packageMqttBack(400,"登录失败，请切换到登录页面");
                    }
                }

            }else if("adupdate".equals(methon)){//广告更新
                EventBus.getDefault().post(new DataUpEvent(1000,""));
            }else if("reboot".equals(methon)){//重启
                LogController.insrtOperatorLog("通知","远程重启系统");
                AppTool.systemReboot();
            }else if("upgrade".equals(methon)){//升级

            }
        } catch (JSONException e) {
        }
        return packageMqttBack(200,"操作成功");
    }


    public String packageMqttBack(int code,String msg){
        JSONObject object = new JSONObject();
        try {
            object.put("id",0);
            object.put("code",code);
            object.put("data",msg);
            return object.toString();
        } catch (JSONException e) {
            return packageFomalBack();
        }
    }
    public String packageFomalBack(){
        return "{\"id\":\"" + 1 + "\", \"code\":\"200\"" + ",\"data\":{} }";
    }

//################################################################################################
    public void sendMsg(String Topic,String msg){
        try {
            MqttPublishRequest request = new MqttPublishRequest();
            request.qos = 0;
            request.isRPC = false;
            request.topic = Topic;
            request.msgId = String.valueOf(IDGenerater.generateId());
            request.payloadObj = msg;
            LinkKit.getInstance().publish(request, new IConnectSendListener() {
                @Override
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    Log.d("mqtt消息", "发布成功"+aResponse.data);
                }

                @Override
                public void onFailure(ARequest aRequest, AError aError) {
                    Log.d("mqtt消息", "发布失败");
                }
            });
        } catch (Exception e) {
        }
    }

    public void subcribe() {
        try {
            MqttSubscribeRequest subscribeRequest = new MqttSubscribeRequest();
            subscribeRequest.isSubscribe = true;
            subscribeRequest.topic = "/" + productKey + "/" + deviceName + "/user/mqtt2";
            LinkKit.getInstance().subscribe(subscribeRequest, new IConnectSubscribeListener() {
                @Override
                public void onSuccess() {
                    Log.d("mqtt消息", "订阅成功");
                }

                @Override
                public void onFailure(AError aError) {
                    Log.d("mqtt消息", "订阅失败 ");
                    BaseApplication.getInstance().setMqttConnect(false);
                }
            });
        } catch (Exception e) {
        }
    }

    private IConnectNotifyListener notifyListener = new IConnectNotifyListener() {
        @Override
        public void onNotify(String s, String s1, AMessage aMessage) {
            String result  = new String((byte[])aMessage.data);
            Log.d("mqtt消息", result);
        }
        @Override
        public boolean shouldHandle(String s, String s1) {
            return true;
        }

        @Override
        public void onConnectStateChange(String s, ConnectState connectState) {

        }
    };

}
