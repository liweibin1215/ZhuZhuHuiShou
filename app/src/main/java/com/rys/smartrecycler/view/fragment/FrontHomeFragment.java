package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
/**
 * 创作时间： 201
 *
 * 9/5/16 on 下午5:20
 * <p>
 * 描述：首页(进入用户投递、管理员回收、维修进入)
 * <p>
 * 作者：lwb
 */
public class FrontHomeFragment extends BaseTitleFragment implements View.OnClickListener{
    public static final String TAG = "FrontHomeFragment";
    private int clickTimes = 0;
    private long lastClickTime = 0;
    @Override
    protected int doThingsBeforeExit() {
        return -1;
    }

    @Override
    protected void onClickBack() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1000);
        mAcFragmentCall.showTimeInfo("");
        isHomeFragment = true;
        BaseApplication.getInstance().setLoginInfoOff();//标记退出登录状态
        initView();
        return conventView;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_menu;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void initView(){
        conventView.findViewById(R.id.rl_client).setOnClickListener(this);
        conventView.findViewById(R.id.rl_admin).setOnClickListener(this);
        conventView.findViewById(R.id.btn_admin).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_client:
                LogController.insrtOperatorLog("投递日志","用户点击开始投递");
                startFragmentAndFinishSelf(this,new FrontChoiceFragment(),FrontChoiceFragment.TAG,R.id.fy_content_id);
                break;
            case R.id.rl_admin:
                LogController.insrtOperatorLog("回收日志","用户点击回收员");
                startFragmentAndFinishSelf(this,new RecycleLoginFragment(),RecycleLoginFragment.TAG,R.id.fy_content_id);
                break;
            case R.id.btn_admin:
                checkAdminLogin();
                break;
        }
    }

    public void checkAdminLogin(){
        if(System.currentTimeMillis() - lastClickTime < 500){
            lastClickTime = System.currentTimeMillis();
            clickTimes++;
            if(clickTimes >=5){
                clickTimes = 0;
                LogController.insrtOperatorLog("后台日志","维修人员进入后台管理中心");
                startFragmentAndFinishSelf(FrontHomeFragment.this,new AdminLoginFragment(),AdminLoginFragment.TAG,R.id.fy_content_id);
            }
        }else{
            lastClickTime = System.currentTimeMillis();
            clickTimes = 1;
        }
    }
}
