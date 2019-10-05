package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.inter.OnDeskSelectListener;
import com.rys.smartrecycler.tool.DeskProgressView;
import com.rys.smartrecycler.view.base.BaseTitleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 创作时间： 2019/5/16 on 下午5:20
 * <p>
 * 描述：首页/投递选择/登录
 * <p>
 * 作者：lwb
 */

public class RecycleChoiceFragment extends BaseTitleFragment implements View.OnClickListener, OnDeskSelectListener {
    public static final String TAG = "RecycleChoiceFragment";
    private List<DeskConfigBean> vos = new ArrayList<>();//当前已配置的副柜
    private LinearLayout lvContent;
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
        lvContent = conventView.findViewById(R.id.cv_content);
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycle_choice;
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
        for (int i = 0;i<vos.size();i++){
            initDeskInfo(i,vos.get(i).getDeskName(),vos.get(i).getPercent());
        }
    }

    public void initDeskInfo(int deskNo,String deskName,int curProgress){
        DeskProgressView view = new DeskProgressView(mContext,deskNo,deskName,curProgress,160,800);
        view.setOnDeskSelectListener(this);
        lvContent.addView(view);
    }

    public void startNextStep(int deskNo){
        Bundle bundle = new Bundle();
        bundle.putInt("deskNo",deskNo);
        RecycleOperatorFragment frontDeliveryFragment = new RecycleOperatorFragment();
        frontDeliveryFragment.setArguments(bundle);
        LogController.insrtOperatorLog("回收员回收","回收员选择回收"+deskNo+"号副柜");
        startFragmentAndFinishSelf(TAG,frontDeliveryFragment,RecycleOperatorFragment.TAG,R.id.fy_content_id);
    }

    @Override
    public void onSelectSuccess(int position) {
        if(position >= 0){
            if(vos.size() > position){
                if(vos.get(position).getPercent() <= 0){
                    Toast.makeText(mContext,"该副柜暂无投递物品",Toast.LENGTH_LONG).show();
                    return;
                }
                startNextStep(vos.get(position).getDeskNo());
            }
        }
    }
}
