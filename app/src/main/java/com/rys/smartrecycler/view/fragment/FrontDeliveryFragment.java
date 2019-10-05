package com.rys.smartrecycler.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.control.api.FrontDeliveryApi;
import com.rys.smartrecycler.control.presenter.FrontDeliveryPresenter;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.tool.AppTool;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.squareup.picasso.Picasso;


/**
 * 创作时间： 2019/5/16 on 下午5:20
 * <p>
 * 描述：首页/投递选择/登录/开始投递
 * <p>
 * 作者：lwb
 */
public class FrontDeliveryFragment extends BaseTitleFragment<FrontDeliveryApi.View,FrontDeliveryApi.Presenter<FrontDeliveryApi.View>> implements View.OnClickListener, FrontDeliveryApi.View{
    private int deskNo = 0;//副柜编号
    public static final String TAG = "FrontDeliveryFragment";
    private CardView cvError,cvIcon1,cvIcon2;
    private TextView tvErrorMsg;
    private int      errorConfirmType = 0;
    private Button   btnErrorConfirm,btnDeliveryFinish;
    private ImageView imgIcon1,imgIcon2,imgIcon3,imgIcon4,imgIcon5,imgIcon6,imgIcon7,imgIcon8,imgIcon9;
    private LinearLayout llIcon5,llIcon6;
    private TextView tvDeskName,tvDeliveryNum;
    private RelativeLayout rlTitle;
    @Override
    protected int doThingsBeforeExit() {
        return 0;
    }
    @Override
    protected void onClickBack() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            deskNo = bundle.getInt("deskNo");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1001);
        initView();
        getPresenter().loadDeskConfigInfo(deskNo);
        getPresenter().openDeviceDoor();
        return conventView;
    }

    public void initView(){
        btnDeliveryFinish = conventView.findViewById(R.id.btn_delivery_finish);
        btnDeliveryFinish.setOnClickListener(this);
        btnErrorConfirm = conventView.findViewById(R.id.btn_error_confirm);
        btnErrorConfirm.setOnClickListener(this);
        cvError = conventView.findViewById(R.id.cv_errot_content);
        cvIcon1 = conventView.findViewById(R.id.cv_icon_content1);
        cvIcon2 = conventView.findViewById(R.id.cv_icon_content2);
        tvErrorMsg = conventView.findViewById(R.id.tv_error_info);
        tvDeskName = conventView.findViewById(R.id.tv_desk_name);
        tvDeliveryNum = conventView.findViewById(R.id.tv_deliver_num);
        llIcon5 = conventView.findViewById(R.id.ll_icon_logo5);
        llIcon6 = conventView.findViewById(R.id.ll_icon_logo6);
        rlTitle = conventView.findViewById(R.id.rl_icon_title);
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_front_delivery;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public FrontDeliveryApi.Presenter<FrontDeliveryApi.View> createPresenter() {
        return new FrontDeliveryPresenter(mContext);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_delivery_finish:
                getPresenter().confirmDeliveryOk();
                break;
            case R.id.btn_error_confirm:
                btnErrorConfirm();
                break;
        }
    }



    @Override
    public void initDeskType1Icon() {
        cvIcon1.setVisibility(View.VISIBLE);
        imgIcon1 = conventView.findViewById(R.id.ig_icon_logo1);
        imgIcon2 = conventView.findViewById(R.id.ig_icon_logo2);
        imgIcon3 = conventView.findViewById(R.id.ig_icon_logo3);
        imgIcon4 = conventView.findViewById(R.id.ig_icon_logo4);
        ((TextView)conventView.findViewById(R.id.tv_icon_info1)).setText("矿泉水");
        ((TextView)conventView.findViewById(R.id.tv_icon_info2)).setText("可乐果汁");
        ((TextView)conventView.findViewById(R.id.tv_icon_info3)).setText("易拉罐");
        ((TextView)conventView.findViewById(R.id.tv_icon_info4)).setText("牛奶瓶");
        Picasso.with(mContext).load(R.mipmap.ic_bottle1).into(imgIcon1);
        Picasso.with(mContext).load(R.mipmap.ic_bottle2).into(imgIcon2);
        Picasso.with(mContext).load(R.mipmap.ic_bottle3).into(imgIcon3);
        Picasso.with(mContext).load(R.mipmap.ic_bottle4).into(imgIcon4);
        setDeskName("饮料瓶");
    }

    @Override
    public void initDeskType2Icon() {
        cvIcon2.setVisibility(View.VISIBLE);
        imgIcon7 = conventView.findViewById(R.id.ig_icon_logo7);
        imgIcon8 = conventView.findViewById(R.id.ig_icon_logo8);
        imgIcon9 = conventView.findViewById(R.id.ig_icon_logo9);
        ((TextView)conventView.findViewById(R.id.tv_icon_info7)).setText("纸张");
        ((TextView)conventView.findViewById(R.id.tv_icon_info8)).setText("数据报刊");
        ((TextView)conventView.findViewById(R.id.tv_icon_info9)).setText("废纸张");
        Picasso.with(mContext).load(R.mipmap.ic_pager1).into(imgIcon7);
        Picasso.with(mContext).load(R.mipmap.ic_pager2).into(imgIcon8);
        Picasso.with(mContext).load(R.mipmap.ic_pager3).into(imgIcon9);
        setDeskName("纸类");
    }

    @Override
    public void initDeskType3Icon() {
        cvIcon1.setVisibility(View.VISIBLE);
        imgIcon1 = conventView.findViewById(R.id.ig_icon_logo1);
        imgIcon2 = conventView.findViewById(R.id.ig_icon_logo2);
        imgIcon3 = conventView.findViewById(R.id.ig_icon_logo3);
        imgIcon4 = conventView.findViewById(R.id.ig_icon_logo4);
        imgIcon5 = conventView.findViewById(R.id.ig_icon_logo5);
        imgIcon6 = conventView.findViewById(R.id.ig_icon_logo6);
        ((TextView)conventView.findViewById(R.id.tv_icon_info1)).setText("内衣");
        ((TextView)conventView.findViewById(R.id.tv_icon_info2)).setText("长短袖");
        ((TextView)conventView.findViewById(R.id.tv_icon_info3)).setText("毛衣");
        ((TextView)conventView.findViewById(R.id.tv_icon_info4)).setText("裤子");
        ((TextView)conventView.findViewById(R.id.tv_icon_info5)).setText("鞋子");
        ((TextView)conventView.findViewById(R.id.tv_icon_info6)).setText("包包");
        llIcon5.setVisibility(View.VISIBLE);
        llIcon6.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(R.mipmap.ic_textile1).into(imgIcon1);
        Picasso.with(mContext).load(R.mipmap.ic_textile2).into(imgIcon2);
        Picasso.with(mContext).load(R.mipmap.ic_textile3).into(imgIcon3);
        Picasso.with(mContext).load(R.mipmap.ic_textile4).into(imgIcon4);
        Picasso.with(mContext).load(R.mipmap.ic_textile5).into(imgIcon5);
        Picasso.with(mContext).load(R.mipmap.ic_textile6).into(imgIcon6);
        setDeskName("纺织物");
    }

    @Override
    public void initDeskType4Icon() {
        cvIcon1.setVisibility(View.VISIBLE);
        imgIcon1 = conventView.findViewById(R.id.ig_icon_logo1);
        imgIcon2 = conventView.findViewById(R.id.ig_icon_logo2);
        imgIcon3 = conventView.findViewById(R.id.ig_icon_logo3);
        imgIcon4 = conventView.findViewById(R.id.ig_icon_logo4);
        ((TextView)conventView.findViewById(R.id.tv_icon_info1)).setText("盆");
        ((TextView)conventView.findViewById(R.id.tv_icon_info2)).setText("油桶");
        ((TextView)conventView.findViewById(R.id.tv_icon_info3)).setText("玩具");
        ((TextView)conventView.findViewById(R.id.tv_icon_info4)).setText("水管");
        Picasso.with(mContext).load(R.mipmap.ic_plastic1).into(imgIcon1);
        Picasso.with(mContext).load(R.mipmap.ic_plastic2).into(imgIcon2);
        Picasso.with(mContext).load(R.mipmap.ic_plastic3).into(imgIcon3);
        Picasso.with(mContext).load(R.mipmap.ic_plastic4).into(imgIcon4);
        setDeskName("生活塑料");
    }

    @Override
    public void initDeskType5Icon() {
        cvIcon2.setVisibility(View.VISIBLE);
        imgIcon7 = conventView.findViewById(R.id.ig_icon_logo7);
        imgIcon8 = conventView.findViewById(R.id.ig_icon_logo8);
        imgIcon9 = conventView.findViewById(R.id.ig_icon_logo9);
        ((TextView)conventView.findViewById(R.id.tv_icon_info7)).setText("锅");
        ((TextView)conventView.findViewById(R.id.tv_icon_info8)).setText("餐具");
        ((TextView)conventView.findViewById(R.id.tv_icon_info9)).setText("电线");
        Picasso.with(mContext).load(R.mipmap.ic_metal1).into(imgIcon7);
        Picasso.with(mContext).load(R.mipmap.ic_metal2).into(imgIcon8);
        Picasso.with(mContext).load(R.mipmap.ic_metal3).into(imgIcon9);
        setDeskName("金属");

    }

    @Override
    public void initDeskType6Icon() {
        cvIcon2.setVisibility(View.VISIBLE);
        imgIcon7 = conventView.findViewById(R.id.ig_icon_logo7);
        imgIcon8 = conventView.findViewById(R.id.ig_icon_logo8);
        imgIcon9 = conventView.findViewById(R.id.ig_icon_logo9);
        ((TextView)conventView.findViewById(R.id.tv_icon_info7)).setText("杯子");
        ((TextView)conventView.findViewById(R.id.tv_icon_info8)).setText("酒瓶");
        ((TextView)conventView.findViewById(R.id.tv_icon_info9)).setText("镜子");
        Picasso.with(mContext).load(R.mipmap.ic_glass1).into(imgIcon7);
        Picasso.with(mContext).load(R.mipmap.ic_glass2).into(imgIcon8);
        Picasso.with(mContext).load(R.mipmap.ic_metal3).into(imgIcon9);
        setDeskName("玻璃");
    }

    @Override
    public void showDeviceError(int type, String msg) {
        cvIcon1.setVisibility(View.GONE);
        cvIcon2.setVisibility(View.GONE);
        cvError.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.GONE);
        setErrorInfo(msg);
        errorConfirmType = type;
    }

    @Override
    public void setErrorInfo(String errorInfo) {
        tvErrorMsg.setText(errorInfo);
    }

    @Override
    public void setDeskName(String deskName) {
        tvDeskName.setText(deskName);
    }

    @Override
    public void setCurrentDeliveryNum(String deliveryNum) {
        tvDeliveryNum.setText(deliveryNum);
    }

    @Override
    public void deliveryFinished(int deskType,int result,String deskName,String num,String money){
        Bundle bundle = new Bundle();
        bundle.putInt("deskNo",deskNo);
        bundle.putInt("deskType",deskType);
        bundle.putInt("result",result);
        bundle.putString("deskName",deskName);
        bundle.putString("deliveryNum",num);//
        bundle.putString("deliveryMoney",money);//
        FrontDeliveryEndFragment frontDeliveryEndFragment = new FrontDeliveryEndFragment();
        frontDeliveryEndFragment.setArguments(bundle);
        LogController.insrtOperatorLog("用户投递", BaseApplication.getInstance().getUserPhone()+"投递完成，投递类型："+ AppTool.getTypeName(String.valueOf(deskType))+"投递量："+num);
        startFragmentAndFinishSelf(TAG,frontDeliveryEndFragment,FrontDeliveryEndFragment.TAG,R.id.fy_content_id);
    }

    public void btnErrorConfirm(){
        if(errorConfirmType == 0){
            startFragmentAndFinishSelf(this,new FrontHomeFragment(),FrontHomeFragment.TAG,R.id.fy_content_id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().stopReceivertask();
    }
}
