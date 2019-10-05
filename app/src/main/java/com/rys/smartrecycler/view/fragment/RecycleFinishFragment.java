package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.control.api.RecycleFinishApi;
import com.rys.smartrecycler.control.presenter.RecycleFinishPresenter;
import com.rys.smartrecycler.db.controller.DeskConfigController;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;
import com.rys.smartrecycler.device.board.MainBoardManager;
import com.rys.smartrecycler.tool.AppTool;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.squareup.picasso.Picasso;

/**
 * 创作时间： 201
 * <p>
 * 9/5/16 on 下午5:20
 * <p>
 * 描述：首页
 * <p>
 * 作者：lwb
 */
public class RecycleFinishFragment extends BaseTitleFragment<RecycleFinishApi.View,RecycleFinishApi.Presenter<RecycleFinishApi.View>> implements View.OnClickListener, RecycleFinishApi.View{
    public static final String TAG = "RecycleFinishFragment";
    private TextView tvDesk,tvDeskName,tvRecycleNum,tvInfoMsg;
    private ImageView imgRcycleIcon;
    private int deskNo=0;
    private String deskName="";
    private String recycleNum="";
    private DeskConfigBean deskConfigBean;
    @Override
    protected void onClickBack() {

    }
    @Override
    protected int doThingsBeforeExit() {
        return 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            deskNo = bundle.getInt("deskNo");
            deskName = bundle.getString("deskName");
            recycleNum = bundle.getString("recycleNum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        initView();
        if(deskNo > 0){
            setCurrentDesk(deskNo);
            setCurrentDeskName(deskName);
            setCuurentRecycleNum(recycleNum);
            setCurrentMsg("回收成功");
            showCurrentResultIcon(true);
        }else{
            setCurrentMsg("回收结束");
            showCurrentResultIcon(false);
        }
        return conventView;
    }

    @Override
    public RecycleFinishApi.Presenter<RecycleFinishApi.View> createPresenter() {
        return new RecycleFinishPresenter(mContext);
    }

    public void initView(){
        conventView.findViewById(R.id.btn_recycle_exit).setOnClickListener(this);
        conventView.findViewById(R.id.btn_recycle_go_on).setOnClickListener(this);
        tvDesk = conventView.findViewById(R.id.tv_recycle_desk);
        tvDeskName = conventView.findViewById(R.id.tv_recycle_name);
        tvRecycleNum = conventView.findViewById(R.id.tv_recycle_num);
        tvInfoMsg = conventView.findViewById(R.id.tv_recycle_result);
        imgRcycleIcon = conventView.findViewById(R.id.ig_recycle_result);
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycle_finish;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_recycle_exit:
                checkRecycleDoorIsClosed(0);
                break;
            case R.id.btn_recycle_go_on:
                checkRecycleDoorIsClosed(1);
                break;
        }
    }

    public void checkRecycleDoorIsClosed(int type){
        if(deskConfigBean == null){
            deskConfigBean = DeskConfigController.getDeskByDeskNo(deskNo);
        }
        if(!MainBoardManager.getInstance().isDeviceOpened()){
            if(!MainBoardManager.getInstance().openMainBoardDevice()){
                LogController.insrtAlarmLog("用户投递","主板打开失败");
                deskConfigBean.setLockStatus(1);
                deskConfigBean.setErrorStatus(AppTool.setError(deskConfigBean.getErrorStatus(),6,"1"));
                DeskConfigController.updateDesk(deskConfigBean);
                return ;
            }
        }
        if(MainBoardManager.getInstance().checkDeskRecycDoorIsClosed(deskNo) != 1){
            Toast.makeText(mContext,"请先关闭回收门",Toast.LENGTH_SHORT).show();
            showToast(1,"'");
            return ;
        }
        if(type == 0){
            LogController.insrtOperatorLog("用户投递", BaseApplication.getInstance().getUserPhone()+"用户点击退出投递");
            startFragmentAndFinishSelf(this,new FrontHomeFragment(),FrontHomeFragment.TAG,R.id.fy_content_id);
        }else{
            LogController.insrtOperatorLog("用户投递",BaseApplication.getInstance().getUserPhone()+"用户点击继续投递");
            startFragmentAndFinishSelf(this,new RecycleChoiceFragment(),RecycleChoiceFragment.TAG,R.id.fy_content_id);
        }
    }


    @Override
    public void setCurrentDesk(int desk) {
        tvDesk.setText(desk+"号柜");
    }

    @Override
    public void setCurrentDeskName(String deskName) {
        tvDeskName.setText(deskName);
    }

    @Override
    public void setCuurentRecycleNum(String recycleNum) {
        tvRecycleNum.setText(recycleNum);
    }

    @Override
    public void setCurrentMsg(String msg) {
        tvInfoMsg.setText(msg);
    }

    @Override
    public void showCurrentResultIcon(boolean isOk) {
        if(isOk){
            Picasso.with(mContext).load(R.mipmap.ic_delivery_ok).into(imgRcycleIcon);
        }else{
            Picasso.with(mContext).load(R.mipmap.ic_delivery_failed).into(imgRcycleIcon);
        }
    }
}
