package com.rys.smartrecycler.view.fragment.admin;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.lwb.devices.serialport.SerialPortFinder;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.db.controller.SystemSetController;
import com.rys.smartrecycler.view.base.BaseTitleFragment;
import com.rys.smartrecycler.view.fragment.AdminHomeFragment;

/**
 * 创作时间： 2019/5/23 on 下午3:20
 * <p>
 * 描述：设备调试
 * <p>
 * 作者：lwb
 */
public class DeviceDebugFragment extends BaseTitleFragment {
    public static final String TAG = "DeviceDebugFragment";
    private Spinner boardCom;
    private Spinner balanceCom;
    private Spinner smodCom;
    private String boardComString;
    private String balanceString;
    private String smodComString;
    private String[] devPaths;
    private Button btnBoard,btnBalance,btnSmod;

    @Override
    protected int doThingsBeforeExit() {
        return 1;
    }

    @Override
    protected void onClickBack() {
        startFragmentAndFinishSelf(this,new AdminHomeFragment(),AdminHomeFragment.TAG,R.id.fy_admin_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        mAcFragmentCall.excuteFragmentAction(1000,"设备调试");
        initView();
        initData();
        initSpinner();
        initListber();
        return conventView;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_device_debug;
    }

    @Override
    public String initChildTag() {
        return TAG;
    }

    private void initView() {
        boardCom = conventView.findViewById(R.id.et_board);
        balanceCom = conventView.findViewById(R.id.et_balance);
        smodCom = conventView.findViewById(R.id.et_smod);
        btnBoard = conventView.findViewById(R.id.btn_board);
        btnBalance = conventView.findViewById(R.id.btn_balance);
        btnSmod = conventView.findViewById(R.id.btn_smod);
    }
    private void initData() {
        SerialPortFinder finder = new SerialPortFinder();
        devPaths = finder.getAllDevicesPath();
    }
    private void initSpinner() {
        ArrayAdapter<String> comAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, devPaths);
        comAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        boardCom.setAdapter(comAdapter);
        balanceCom.setAdapter(comAdapter);
        smodCom.setAdapter(comAdapter);
        boardComString = SystemSetController.getSysInfo("MainboardCom");
        balanceString = SystemSetController.getSysInfo("BalanceCom");
        smodComString = SystemSetController.getSysInfo( "SmogAlarmCom");
        for (int i = 0; i < devPaths.length; i++) {
            if (devPaths[i].equals(boardComString)) {
                boardCom.setSelection(i);
            }
            if (devPaths[i].equals(balanceString)) {
                balanceCom.setSelection(i);
            }
            if (devPaths[i].equals(smodComString)) {
                smodCom.setSelection(i);
            }
        }
    }

    /**
     * 监听初始化
     */
    public void initListber(){
        btnBoard.setOnClickListener(v -> {
            String choiced = boardCom.getSelectedItem().toString();
            if(!boardComString.equals(choiced)){
                SystemSetController.addSystemSet("MainboardCom",choiced,"主板串口");

            }
            startFragmentAndFinishSelf(this,new BoardDebugFragment(),BoardDebugFragment.TAG,R.id.fy_admin_id);


        });
        btnBalance.setOnClickListener(v ->{
            String choiced = balanceCom.getSelectedItem().toString();
            if(!balanceString.equals(choiced)){
                SystemSetController.addSystemSet("BalanceCom",choiced,"电子秤串口");

            }
            startFragmentAndFinishSelf(this,new BalanceDebugFragment(),BalanceDebugFragment.TAG,R.id.fy_admin_id);
        });

        btnSmod.setOnClickListener(v ->{
            String choiced = smodCom.getSelectedItem().toString();
            if(!smodComString.equals(choiced)){
                SystemSetController.addSystemSet("SmogAlarmCom",choiced,"报警器串口");

            }
            startFragmentAndFinishSelf(this,new SmodFragment(),SmodFragment.TAG,R.id.fy_admin_id);

        });
    }
}
