package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rys.smartrecycler.R;
import com.rys.smartrecycler.control.api.RecycleOperatorApi;
import com.rys.smartrecycler.control.presenter.RecycleOperatorPresenter;
import com.rys.smartrecycler.view.base.BaseTitleFragment;

/**
 * 创作时间： 2019/5/16 on 下午5:20
 * <p>
 * 描述：首页/投递选择/登录
 * <p>
 * 作者：lwb
 */
public class RecycleOperatorFragment extends BaseTitleFragment<RecycleOperatorApi.View,RecycleOperatorApi.Presenter<RecycleOperatorApi.View>> implements View.OnClickListener, RecycleOperatorApi.View{
    public static final String TAG = "RecycleOperatorFragment";
    private TextView tvErrorMsg;
    private CardView cvErrorContent,cv_recycle;
    private RelativeLayout llTitle;
    private TextView tvDeskName;
    private int deskNo;
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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1001);
        initView();
        getPresenter().loadDeskConfigInfo(deskNo);
        getPresenter().openDeviceDoor();
        return conventView;
    }

    public void initView(){
        conventView.findViewById(R.id.btn_recycle_finish).setOnClickListener(this);
        conventView.findViewById(R.id.cv_errot_content).setOnClickListener(this);
        tvErrorMsg = conventView.findViewById(R.id.tv_error_info);
        cvErrorContent = conventView.findViewById(R.id.cv_errot_content);
        cv_recycle = conventView.findViewById(R.id.cv_recycle);
        llTitle = conventView.findViewById(R.id.ll_title);
        tvDeskName = conventView.findViewById(R.id.tv_recycle_title);
    }
    @Override
    public RecycleOperatorApi.Presenter<RecycleOperatorApi.View> createPresenter() {
        return new RecycleOperatorPresenter(mContext);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycle_operator;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recycle_finish:
                getPresenter().recycleFinished();
                break;
            case R.id.cv_errot_content:
                startFragmentAndFinishSelf(this,new FrontHomeFragment(),FrontHomeFragment.TAG,R.id.fy_content_id);
                break;
        }
    }

    @Override
    public void showDeviceError(int type, String msg) {
        llTitle.setVisibility(View.GONE);
        cv_recycle.setVisibility(View.GONE);
        setErrorInfo(msg);
        cvErrorContent.setVisibility(View.VISIBLE);
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
    public void recycleFinished(String deskName, String recycleNum) {
        Bundle bundle = new Bundle();
        bundle.putInt("deskNo",deskNo);
        bundle.putString("deskName",deskName);
        bundle.putString("recycleNum",recycleNum);
        RecycleFinishFragment recycleFinishFragment = new RecycleFinishFragment();
        recycleFinishFragment.setArguments(bundle);
        startFragmentAndFinishSelf(this,recycleFinishFragment,RecycleFinishFragment.TAG,R.id.fy_content_id);
    }

    @Override
    public int getDeskNo() {
        return deskNo;
    }

}
