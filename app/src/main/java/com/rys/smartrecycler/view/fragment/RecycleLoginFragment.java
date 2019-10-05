package com.rys.smartrecycler.view.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.control.api.RecycleLoginApi;
import com.rys.smartrecycler.control.presenter.RecycleLoginPresenter;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.event.LoginEvent;
import com.rys.smartrecycler.tool.qrcode.DesECB;
import com.rys.smartrecycler.tool.qrcode.EncodingHandler;
import com.rys.smartrecycler.view.base.BaseTitleFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创作时间： 201
 * <p>
 * 9/5/16 on 下午5:20
 * <p>
 * 描述：首页
 * <p>
 * 作者：lwb
 */
public class RecycleLoginFragment extends BaseTitleFragment<RecycleLoginApi.View,RecycleLoginApi.Presenter<RecycleLoginApi.View>> implements View.OnClickListener, RecycleLoginApi.View{
    public static final String TAG = "RecycleLoginFragment";
    private TextView tvTypeSaoma,tvTypePhone;
    private LinearLayout llSaomaLogin;
    private RelativeLayout rlPhoneLogin;
    private ImageView imgQrcode,igTypeSaoma,igTypePhone;
    private EditText etPhone,etPwd;
    private TextView tvGetPwd;
    @Override
    protected int doThingsBeforeExit() {
        return 0;
    }
    @Override
    protected void onClickBack() {
        startFragmentAndFinishSelf(this,new FrontHomeFragment(),FrontHomeFragment.TAG,R.id.fy_content_id);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1001);
        initView();
        createWechatQRCode(createAdress());
        EventBus.getDefault().register(this);
        BaseApplication.getInstance().setLoginStatus(2);
        return conventView;
    }

    public void initView(){
        tvTypeSaoma = conventView.findViewById(R.id.tv_title_saoma);
        tvTypePhone = conventView.findViewById(R.id.tv_title_account);
        llSaomaLogin = conventView.findViewById(R.id.ll_login_scan);
        rlPhoneLogin = conventView.findViewById(R.id.rl_login_phone);
        imgQrcode = conventView.findViewById(R.id.img_home_login);
        igTypeSaoma = conventView.findViewById(R.id.ig_login_saoma);
        igTypePhone = conventView.findViewById(R.id.ig_login_phone);
        etPhone = conventView.findViewById(R.id.et_phone);
        etPwd = conventView.findViewById(R.id.et_pwssword);
        tvGetPwd = conventView.findViewById(R.id.tv_get_pwd);
        tvTypeSaoma.setOnClickListener(this);
        tvTypePhone.setOnClickListener(this);
        tvGetPwd.setOnClickListener(this);
        conventView.findViewById(R.id.btn_confirm).setOnClickListener(this);

    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycle_login;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public RecycleLoginApi.Presenter<RecycleLoginApi.View> createPresenter() {
        return new RecycleLoginPresenter(mContext);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().releaseTimeHandler();
        EventBus.getDefault().unregister(this);
        BaseApplication.getInstance().setLoginStatus(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event){
        if(event != null && event.getCode() == 1001){
            BaseApplication.getInstance().setLoginInfo(true,event.getLoginUser(),event.getUserId());
            LogController.insrtOperatorLog("回收员回收", BaseApplication.getInstance().getUserPhone()+"回收员扫码登录成功");
            startFragmentAndFinishSelf(TAG,new RecycleChoiceFragment(),FrontDeliveryFragment.TAG,R.id.fy_content_id);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_saoma:
                llSaomaLogin.setVisibility(View.VISIBLE);
                rlPhoneLogin.setVisibility(View.GONE);
                tvTypeSaoma.setTextColor(getActivity().getResources().getColor(R.color.black_theme_one));
                igTypeSaoma.setBackgroundColor(getActivity().getResources().getColor(R.color.black_theme_one));
                tvTypePhone.setTextColor(getActivity().getResources().getColor(R.color.black_theme_thr));
                igTypePhone.setBackgroundColor(getActivity().getResources().getColor(R.color.black_theme_thr));
                break;
            case R.id.tv_title_account:
                llSaomaLogin.setVisibility(View.GONE);
                rlPhoneLogin.setVisibility(View.VISIBLE);
                tvTypeSaoma.setTextColor(getActivity().getResources().getColor(R.color.black_theme_thr));
                igTypeSaoma.setBackgroundColor(getActivity().getResources().getColor(R.color.black_theme_thr));
                tvTypePhone.setTextColor(getActivity().getResources().getColor(R.color.black_theme_one));
                igTypePhone.setBackgroundColor(getActivity().getResources().getColor(R.color.black_theme_one));
                break;
            case R.id.btn_confirm:
                startFragmentAndFinishSelf(TAG,new RecycleChoiceFragment(),FrontDeliveryFragment.TAG,R.id.fy_content_id);
//                getPresenter().startGoLogin();
                break;
            case R.id.tv_get_pwd:
                getPresenter().startGetPwdInfo();
                break;
        }
    }

    @Override
    public void setCurrentPwdInfo(String info) {
        tvGetPwd.setText(info);
    }

    @Override
    public void loginSuccess() {
        LogController.insrtOperatorLog("回收员回收", BaseApplication.getInstance().getUserPhone()+"回收员账号登录成功");
        startFragmentAndFinishSelf(TAG,new RecycleChoiceFragment(),FrontDeliveryFragment.TAG,R.id.fy_content_id);
    }

    @Override
    public String getLoginPhone() {
        return etPhone.getText().toString().trim();
    }

    @Override
    public void setLoginPhone(String phone) {
        etPhone.setText(phone);
    }

    @Override
    public String getLoginPwd() {
        return etPwd.getText().toString().trim();
    }

    @Override
    public void setLoginPwd(String pwd) {
        etPwd.setText(pwd);
    }
    public void createWechatQRCode(String adress) {
        if (!"".equals(adress)) {
            Bitmap qrCodeBitmap;
            try {
                qrCodeBitmap = EncodingHandler.createQRCode(DesECB.encode("1234567890", adress), 250);
                imgQrcode.setImageBitmap(qrCodeBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }
    public String createAdress(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SmartBoxSn",BaseApplication.getInstance().getTerminalId());//设备编号
            jsonObject.put("ReqTime", String.valueOf(System.currentTimeMillis()/1000));//当前时间戳，用于判断有效期
            jsonObject.put("LoginType","1");//登录类型 0用户登陆、1回收员登录
            jsonObject.put("Version","v_1.0");//当前协议版本
            return jsonObject.toString();
        } catch (JSONException e) {
            return "副柜配置异常";
        }
    }

}
