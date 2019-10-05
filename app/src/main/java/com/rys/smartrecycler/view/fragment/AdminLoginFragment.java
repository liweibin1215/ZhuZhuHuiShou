package com.rys.smartrecycler.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lwb.framelibrary.utils.ActivityUtil;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.controller.DeviceInfoController;
import com.rys.smartrecycler.db.retbean.DeviceInfo;
import com.rys.smartrecycler.view.activity.entrance.AdminHomeActivity;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
/**
 * 创作时间： 2019/5/23 on 下午3:20
 * <p>
 * 描述：首页/投递选择/登录
 * <p>
 * 作者：lwb
 */
public class AdminLoginFragment extends BaseTitleFragment implements View.OnClickListener{
    public static final String TAG = "AdminLoginFragment";
    private EditText etPwd;
    private String   adminPwd = "";
    @Override
    protected int doThingsBeforeExit() {
        return 0;
    }
    @Override
    protected void onClickBack() {
        startFragmentAndFinishSelf(this,new FrontHomeFragment(),FrontHomeFragment.TAG,R.id.fy_content_id);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        etPwd = conventView.findViewById(R.id.edit_intput);
        conventView.findViewById(R.id.btn_confirm).setOnClickListener(this);
        DeviceInfo vo = DeviceInfoController.getDeviceInfo();
        adminPwd = (vo == null?"":vo.getAdminPwd());
        etPwd.setText("admin123");
        return conventView;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_admin_login;
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
        if(view.getId() == R.id.btn_confirm){
            String password = etPwd.getText().toString();
            if(adminPwd != null && !"".equals(adminPwd)){
                if(password.equals(adminPwd)){
                    ActivityUtil.jump(mContext, AdminHomeActivity.class);
                }
            }else if("admin123".equals(password)){
                etPwd.setText("");
                ActivityUtil.jump(mContext, AdminHomeActivity.class);
            }
        }
    }
}
