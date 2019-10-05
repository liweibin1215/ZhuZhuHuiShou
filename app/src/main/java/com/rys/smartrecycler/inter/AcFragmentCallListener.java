package com.rys.smartrecycler.inter;

/**
 * 创作时间： 2019/5/16 on 下午4:42
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public interface AcFragmentCallListener {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void excuteFragmentAction(int type,String... params);
    void showTimeInfo(String time);
}
