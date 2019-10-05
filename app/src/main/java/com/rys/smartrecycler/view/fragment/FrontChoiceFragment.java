package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 创作时间： 2019/5/16 on 下午5:20
 * <p>
 * 描述：首页/投递选择
 * <p>
 * 作者：lwb
 */
public class FrontChoiceFragment extends BaseTitleFragment implements View.OnClickListener{
    public static final String TAG = "FrontChoiceFragment";
    private int[] iconId = new int[]{R.mipmap.ic_bottle,R.mipmap.ic_paper,R.mipmap.ic_textile,R.mipmap.ic_plastic,R.mipmap.ic_metal,R.mipmap.ic_glass};
    private int[] tvNameId = new int[]{R.id.tv_goods1,R.id.tv_goods2,R.id.tv_goods3,R.id.tv_goods4,R.id.tv_goods5,R.id.tv_goods6};
    private int[] tvPriceId = new int[]{R.id.tv_price1,R.id.tv_price2,R.id.tv_price3,R.id.tv_price4,R.id.tv_price5,R.id.tv_price6};
    private int[] tvFullId = new int[]{R.id.ic_full_1,R.id.ic_full_2,R.id.ic_full_3,R.id.ic_full_4,R.id.ic_full_5,R.id.ic_full_6};
    private RelativeLayout llGoods1,llGoods2,llGoods3,llGoods4,llGoods5,llGoods6;
    private ImageView imgGoods1,imgGoods2,imgGoods3,imgGoods4,imgGoods5,imgGoods6;
    private List<DeskConfigBean> vos = new ArrayList<>();//当前已配置的副柜
    private int loginType = 0;
    @Override
    protected int doThingsBeforeExit() {
        return 0;

    }
    @Override
    protected void onClickBack() {
        startFragmentAndFinishSelf(TAG,new FrontHomeFragment(),FrontHomeFragment.TAG,R.id.fy_content_id);
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            loginType = bundle.getInt("loginType");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1001);
        initView();
        initData();
        return conventView;
    }
    public void initView(){
        llGoods1 = conventView.findViewById(R.id.ll_goods1);
        llGoods2 = conventView.findViewById(R.id.ll_goods2);
        llGoods3 = conventView.findViewById(R.id.ll_goods3);
        llGoods4 = conventView.findViewById(R.id.ll_goods4);
        llGoods5 = conventView.findViewById(R.id.ll_goods5);
        llGoods6 = conventView.findViewById(R.id.ll_goods6);
        imgGoods1 = conventView.findViewById(R.id.ig_goods1);
        imgGoods2 = conventView.findViewById(R.id.ig_goods2);
        imgGoods3 = conventView.findViewById(R.id.ig_goods3);
        imgGoods4 = conventView.findViewById(R.id.ig_goods4);
        imgGoods5 = conventView.findViewById(R.id.ig_goods5);
        imgGoods6 = conventView.findViewById(R.id.ig_goods6);
        llGoods1.setOnClickListener(this);
        llGoods2.setOnClickListener(this);
        llGoods3.setOnClickListener(this);
        llGoods4.setOnClickListener(this);
        llGoods5.setOnClickListener(this);
        llGoods6.setOnClickListener(this);
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_front_choice_type;
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
        iconId = null;
        tvNameId = null;
        tvPriceId = null;
    }

    public void startNextStep(int deskNo){
        Bundle bundle = new Bundle();
        bundle.putInt("deskNo",deskNo);
        if(loginType == 0){
            LogController.insrtOperatorLog("投递日志","用户投递选择"+deskNo+"号副柜");
            FrontLoginFragment frontLoginFragment = new FrontLoginFragment();
            frontLoginFragment.setArguments(bundle);
            startFragmentAndFinishSelf(TAG,frontLoginFragment,FrontLoginFragment.TAG,R.id.fy_content_id);
        }else{
            LogController.insrtOperatorLog("投递日志", BaseApplication.getInstance().getUserPhone()+"继续投递,选择"+deskNo+"号副柜");
            FrontDeliveryFragment frontDeliveryFragment = new FrontDeliveryFragment();
            frontDeliveryFragment.setArguments(bundle);
            startFragmentAndFinishSelf(TAG,frontDeliveryFragment,FrontDeliveryFragment.TAG,R.id.fy_content_id);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_goods1:
                startNextStep(vos.get(0).getDeskNo());
                break;
            case R.id.ll_goods2:
                startNextStep(vos.get(1).getDeskNo());
                break;
            case R.id.ll_goods3:
                startNextStep(vos.get(2).getDeskNo());
                break;
            case R.id.ll_goods4:
                startNextStep(vos.get(3).getDeskNo());
                break;
            case R.id.ll_goods5:
                startNextStep(vos.get(4).getDeskNo());
                break;
            case R.id.ll_goods6:
                startNextStep(vos.get(5).getDeskNo());
                break;
        }
    }

    public void initData(){
        vos = DeskConfigController.getAllDesks();
        if(vos == null || vos.size() <= 0){
            return;
        }
        String price = "";
        for (int i = 0;i<vos.size();i++){
            if(vos.get(i).getDeskType() == 1){
                price = vos.get(i).getUnitPrice()+"元/个";
            }else{
                price = vos.get(i).getUnitPrice()+"元/公斤";
            }
            initDeskInfo(vos.get(i).getDeskType(),i,vos.get(i).getDeskName(),price,vos.get(i).getFullStatus(),vos.get(i).getLockStatus());
        }
    }

    public void initDeskInfo(int deskType,int position,String deskName,String deskPrice,int fullStatus,int lockStatus){
        switch (position){
            case 0:
                initGoods(llGoods1,fullStatus,lockStatus,imgGoods1,deskType,position);
                break;
            case 1:
                initGoods(llGoods2,fullStatus,lockStatus,imgGoods2,deskType,position);

                break;
            case 2:
                initGoods(llGoods3,fullStatus,lockStatus,imgGoods3,deskType,position);
                break;
            case 3:
                initGoods(llGoods4,fullStatus,lockStatus,imgGoods4,deskType,position);
                break;
            case 4:
                initGoods(llGoods5,fullStatus,lockStatus,imgGoods5,deskType,position);
                break;
            case 5:
                initGoods(llGoods6,fullStatus,lockStatus,imgGoods6,deskType,position);
                break;
        }
        ((TextView)conventView.findViewById(tvNameId[position])).setText(deskName);
        ((TextView)conventView.findViewById(tvPriceId[position])).setText(deskPrice);
    }

    public void initGoods(RelativeLayout llGoods,int fullStatus,int lockStatus,ImageView imgGoods,int resPosition,int position){
        llGoods.setVisibility(View.VISIBLE);
        if(lockStatus == 1){
            llGoods.setEnabled(false);
            //标记故障
            conventView.findViewById(tvFullId[position]).setVisibility(View.VISIBLE);
        }else if(fullStatus == 1){
            llGoods.setEnabled(false);
            //标记箱满
            conventView.findViewById(tvFullId[position]).setVisibility(View.VISIBLE);
        }
        Picasso.with(getActivity()).load(iconId[resPosition-1]).into(imgGoods);
    }
}
