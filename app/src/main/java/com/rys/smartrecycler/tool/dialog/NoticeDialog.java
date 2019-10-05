package com.rys.smartrecycler.tool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.rys.smartrecycler.R;


/**
 * Created by jdd on 2018/11/23.
 */

public class NoticeDialog extends Dialog {
    private Context mContext;
    private int time = 10;
    private TextView tv_timer;
    private TextView tv_alert;
    private boolean isNeedFeedback;     //是否需要回调
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    time--;
                    if (time <= 0) {
                        dismiss();
                    } else {
                        tv_timer.setText(time + "s");
                        sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    };

    public NoticeDialog(Context context) {
        super(context, R.style.Dialog_bocop);
        this.mContext = context;
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_notice, null);
        setContentView(contentView);
        tv_timer = (TextView) contentView.findViewById(R.id.tv_timer);
        tv_alert = (TextView) contentView.findViewById(R.id.tv_alert);
        contentView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(String alertMsg, int type) {
        super.show();
        if (!"".equals(alertMsg))
            tv_alert.setText(alertMsg);
        switch (type) {
            case 0:
                time = 10;
                break;
            case 1:
                time = 2;
                break;
            case 2:
                time = 5;
                break;
        }
        tv_timer.setText(time + "s");
        isNeedFeedback = false;
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public void show(String alertMsg, int time, BackOrTimeoutListener backOrTimeoutListener) {
        super.show();
        if (!"".equals(alertMsg))
            tv_alert.setText(alertMsg);
        this.time = time;
        tv_timer.setText(time + "s");
        this.backOrTimeoutListener = backOrTimeoutListener;
        isNeedFeedback = true;
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public void dismiss() {
        mHandler.removeMessages(0);
        if (isNeedFeedback && backOrTimeoutListener != null)
            backOrTimeoutListener.onBackOrTimeout();
        super.dismiss();
    }

    public void realease() {
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
        mHandler = null;

    }

    public interface BackOrTimeoutListener {
        void onBackOrTimeout();
    }

    private BackOrTimeoutListener backOrTimeoutListener;
}
