package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lwb.framelibrary.net.subscriber.BaseSubscriber;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.device.board.MainBoardManager;
import com.rys.smartrecycler.tool.AppTool;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 创作时间： 2019/5/16 on 下午5:20
 * <p>
 * 描述：首页/投递选择/登录/开始投递/投递结束
 * <p>
 * 结果：投递成功、投递失败
 * <p>
 * 作者：lwb
 */
public class FrontDeliveryEndFragment extends BaseTitleFragment implements View.OnClickListener{
    public static final String TAG = "FrontDeliveryEndFragment";
    private int deskType =0;
    private int    result = 0;
    private String deskName = "";
    private String sum = "";
    private String money = "";
    private TextView tvName,tvSum,tvMoney,tvInfo;
    private ImageView imgIcon;
    private int deskNo;
    private DeskConfigBean deskConfigBean;
    private boolean isClosing = false;
    @Override
    protected int doThingsBeforeExit() {
        return 0;
    }
    @Override
    protected void onClickBack() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            deskNo = bundle.getInt("deskNo");
            deskType = bundle.getInt("deskType");
            result = bundle.getInt("result");
            deskName = bundle.getString("deskName");
            sum = bundle.getString("deliveryNum");
            money = bundle.getString("deliveryMoney");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1001);
        tvName = conventView.findViewById(R.id.tv_delivery_name);
        tvSum = conventView.findViewById(R.id.tv_delivery_num);
        tvMoney = conventView.findViewById(R.id.tv_delivery_money);
        tvInfo = conventView.findViewById(R.id.tv_delivery_result);
        imgIcon = conventView.findViewById(R.id.ig_delivery_result);
        initView();
        if(result == 0){
            tvInfo.setText("投递成功，谢谢您的使用!");
            Picasso.with(mContext).load(R.mipmap.ic_delivery_ok).into(imgIcon);
        }else{
            if(deskType == 1){
                LogController.insrtOperatorLog("用户投递", BaseApplication.getInstance().getUserPhone()+"未检测到用户投递的物品");
                tvInfo.setText("投递结束，没有识别到您的物品投入噢!");
            }else{
                LogController.insrtOperatorLog("用户投递", BaseApplication.getInstance().getUserPhone()+"用户投递物品太轻");
                tvInfo.setText("您投递的物品太轻，最低需要60g以上噢!");
            }
            Picasso.with(mContext).load(R.mipmap.ic_delivery_failed).into(imgIcon);
        }
        loadDeskInfo();
        return conventView;
    }

    public void initView(){
        conventView.findViewById(R.id.btn_delivery_exit).setOnClickListener(this);
        conventView.findViewById(R.id.btn_delivery_go_on).setOnClickListener(this);
        tvName.setText(deskName);
        tvSum.setText(sum);
        tvMoney.setText(money+"元");
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_front_delivery_end;
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
        switch (view.getId()){
            case R.id.btn_delivery_exit:
                if(isClosing){
                    Toast.makeText(mContext,"正在关闭投递门，请稍后..",Toast.LENGTH_SHORT).show();
                    return;
                }
                LogController.insrtOperatorLog("用户投递", BaseApplication.getInstance().getUserPhone()+"用户点击结束投递");
                startFragmentAndFinishSelf(this,new FrontHomeFragment(),FrontHomeFragment.TAG,R.id.fy_content_id);
                break;
            case R.id.btn_delivery_go_on:
                if(isClosing){
                    Toast.makeText(mContext,"正在关闭投递门，请稍后..",Toast.LENGTH_SHORT).show();
                    return;
                }
                LogController.insrtOperatorLog("用户投递", BaseApplication.getInstance().getUserPhone()+"用户点击继续投递");
                goOnDelivery();
                break;
        }
    }

    public void goOnDelivery(){
        Bundle bundle = new Bundle();
        bundle.putInt("loginType",1);//标记已经登录过，继续投递
        FrontChoiceFragment frontChoiceFragment = new FrontChoiceFragment();
        frontChoiceFragment.setArguments(bundle);
        startFragmentAndFinishSelf(this,frontChoiceFragment,FrontChoiceFragment.TAG,R.id.fy_content_id);
    }

    public void loadDeskInfo(){
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            isClosing = true;
            deskConfigBean = DeskConfigController.getDeskByDeskNo(deskNo);
            if(deskConfigBean == null){
                emitter.onNext(false);
                return;
            }
            if(!MainBoardManager.getInstance().isDeviceOpened()){
                if(!MainBoardManager.getInstance().openMainBoardDevice()){
                    LogController.insrtAlarmLog("用户投递","主板打开失败");
                    deskConfigBean.setLockStatus(1);
                    deskConfigBean.setErrorStatus(AppTool.setError(deskConfigBean.getErrorStatus(),6,"1"));
                    DeskConfigController.updateDesk(deskConfigBean);
                    emitter.onNext(false);
                    return;
                }
            }
            boolean isDoorOpen = false;
            long openTime = System.currentTimeMillis();
            MainBoardManager.getInstance().closeDeskDoor(deskConfigBean.getDeskNo());
            while (System.currentTimeMillis() - openTime < 6000){
                if(MainBoardManager.getInstance().checkDeskDoorStatus(deskConfigBean.getDeskNo()) == 0){
                    isDoorOpen = true;
                    break;
                }
                Thread.sleep(100);
            }
            if(!isDoorOpen){
                LogController.insrtAlarmLog("用户投递","投递口关闭失败");
                deskConfigBean.setLockStatus(1);
                deskConfigBean.setErrorStatus(AppTool.setError(deskConfigBean.getErrorStatus(),2,"1"));
                DeskConfigController.updateDesk(deskConfigBean);
            }
            emitter.onNext(isDoorOpen);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        isClosing = false;
                    }
                });

    }
}
