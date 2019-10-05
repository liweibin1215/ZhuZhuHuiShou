package com.lwb.framelibrary.widget.edittext;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述: 地址输入限制，只允许汉字、字母、数字以及"-"、"xX"等的输入
 * limitType ：
 * 0：无限制
 * 1：只能输入汉字、字母、数字、“-”
 * 2:只能输入汉字、字母
 * 3:只能输入数字、“xX”(针对身份证号的输入)
 * 4:只能输入字母、数字、
 * 5:纯数字
 * 6:纯字母
 */

public class InLimitEditText extends AppCompatEditText {
    private int limitType = 0;
    public InLimitEditText(Context context) {
        super(context,null);
    }

    public InLimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs,android.R.attr.editTextStyle);
    }
    public InLimitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.InLimitEditText, defStyleAttr, 0);
        limitType = ta.getInteger(R.styleable.InLimitEditText_limitType,0);
        ta.recycle();*/
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new mInputConnecttion(limitType,super.onCreateInputConnection(outAttrs),
                false);
    }

    /**
     * 实例调用此函数修改希望可输入的类型
     * @param limitType
     */
    public void setLimitType(int limitType){
        this.limitType = limitType;
    }

}

class mInputConnecttion extends InputConnectionWrapper implements
        InputConnection {
    private int limitType = 0;
    public mInputConnecttion(int limitType,InputConnection target, boolean mutable) {
        super(target, mutable);
        this.limitType = limitType;
    }
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        if(limitType == 0){//无限制
            return super.commitText(text, newCursorPosition);
        }else if (limitType == 1 && !text.toString().matches("[a-zA-Z0-9\u4e00-\u9fa5-]+")) { // 只能输入汉字、字母、数字、“-”
            return false;
        }else if(limitType == 2 && !text.toString().matches("[a-zA-Z\u4e00-\u9fa5]+")){ // 只能输入汉字、字母
            return false;
        }else if(limitType == 3 && !text.toString().matches("[0-9xX]+")){// 只能输入数字、“xX”(针对身份证号的输入)
            return false;
        }else if(limitType == 4 && !text.toString().matches("[a-zA-Z0-9]+")){// 只能输入字母、数字、
            return false;
        }else if(limitType == 5 && !text.toString().matches("[0-9]")){//纯数字
            return false;
        }else if(limitType == 6 && !text.toString().matches("[a-zA-Z]")){//纯字母
            return false;
        }
        return super.commitText(text, newCursorPosition);
    }
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        return super.sendKeyEvent(event);
    }
    @Override
    public boolean setSelection(int start, int end) {
        return super.setSelection(start, end);
    }

}
