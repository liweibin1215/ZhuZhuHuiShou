package com.rys.smartrecycler.inter;

import android.view.MotionEvent;

/**
 * 创作时间： 2019/5/16 on 下午5:12
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public interface Observer {
    void myTouched(MotionEvent event);
}
