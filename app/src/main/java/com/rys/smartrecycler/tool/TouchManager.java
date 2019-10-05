package com.rys.smartrecycler.tool;

import android.view.MotionEvent;

import com.rys.smartrecycler.inter.BaseObManager;
import com.rys.smartrecycler.inter.Observer;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 创作时间： 2019/5/16 on 下午5:20
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class TouchManager implements BaseObManager{

    private Vector<Observer> vector = new Vector<>();

    @Override
    public void addObserver(Observer ob) {
        vector.add(ob);
    }
    @Override
    public void removeObserver(Observer ob) {
        vector.remove(ob);
    }
    @Override
    public void notifyObservers(MotionEvent ev) {
        Enumeration<Observer> enumo = vector.elements();
        while (enumo.hasMoreElements()) {
            enumo.nextElement().myTouched(ev);
        }
    }
    @Override
    public void removeAll() {
        vector.removeAllElements();
        vector.clear();
        vector = null;
    }
}
