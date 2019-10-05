package com.lwb.framelibrary.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseJumpFragment extends Fragment {
    public Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    /**
     * 跳转到另外一个fragment的方法并且关闭自己
     * @param curreentTag 当前fragment的TAG
     * @param newFragment 目标fragment
     * @param TAG 		  目标fragment的TAG
     */
    public void startFragmentAndFinishSelf(String curreentTag, Fragment newFragment, String TAG, int id){
        FragmentManager manager = getFragmentManager();
        if(manager!=null){
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment currentFragment = manager.findFragmentByTag(curreentTag);
            Fragment targetFragment = manager.findFragmentByTag(TAG);
            if(targetFragment != null){
                transaction.remove(currentFragment).show(targetFragment);
                transaction.commitAllowingStateLoss();
            }else{
                transaction.replace(id,newFragment,TAG);
                transaction.commitAllowingStateLoss();
            }
        }

    }

    /**
     * 跳转到另外一个fragment的方法并且关闭自己
     * @param curreent 当前fragment的TAG
     * @param newFragment 目标fragment
     * @param TAG 		  目标fragment的TAG
     */
    public void startFragmentAndFinishSelf(Fragment curreent, Fragment newFragment, String TAG, int id){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment targetFragment = manager.findFragmentByTag(TAG);
        if(targetFragment != null){
            transaction.remove(curreent).show(targetFragment);
            transaction.commitAllowingStateLoss();
        }else{
            transaction.remove(curreent).add(id,newFragment,TAG);
            transaction.commitAllowingStateLoss();
        }
    }
    /**
     * 跳转到另外一个fragment的方法并且隐藏自己
     * @param current 当前fragment
     * @param newFragment 目标fragment
     * @param TAG 		  目标fragment的TAG
     */
    public void startFragmentAndHideSelf(Fragment current, Fragment newFragment, String TAG, int id){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment targetFragment = manager.findFragmentByTag(TAG);
        if(targetFragment != null){
            transaction.hide(current).show(targetFragment);
            transaction.commitAllowingStateLoss();
        }else{
            transaction.hide(current).add(id,newFragment,TAG);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 跳转到另外一个fragment的方法bing并且隐藏自身
     * @param curreentTag
     * @param newFragment
     * @param TAG
     * @param bundle 跳转传参
     */
    public void startFragmentAndHideSelf(String curreentTag, Fragment newFragment, String TAG, Bundle bundle, int id){
        FragmentManager manager = getFragmentManager();
        if(manager!=null){
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment currentFragment = manager.findFragmentByTag(curreentTag);
            Fragment targetFragment = manager.findFragmentByTag(TAG);
            if(currentFragment != newFragment){
                if(targetFragment == null){
                    newFragment.setArguments(bundle);
                    transaction.hide(currentFragment).add(id,newFragment,TAG);
                    transaction.commitAllowingStateLoss();
                }else{
                    targetFragment.setArguments(bundle);
                    transaction.hide(currentFragment).show(targetFragment);
                    transaction.commitAllowingStateLoss();
                }
            }
        }
    }
    /**
     * 跳转到另外一个fragment的方法并且关闭自己
     * @param curreentTag 当前fragment的TAG
     * @param newFragment 目标fragment
     * @param TAG 		  目标fragment的TAG
     * @param bundle     跳转传参
     */
    public void startFragmentAndFinishSelf(String curreentTag, Fragment newFragment, String TAG, Bundle bundle, int id){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = manager.findFragmentByTag(curreentTag);
        Fragment targetFragment = manager.findFragmentByTag(TAG);
        if(targetFragment != null){
            targetFragment.setArguments(bundle);
            transaction.remove(currentFragment).show(targetFragment);
            transaction.commitAllowingStateLoss();
        }else{
            newFragment.setArguments(bundle);
            transaction.replace(id,newFragment,TAG);
            transaction.commitAllowingStateLoss();
        }
    }
    /**
     * 关闭自己
     * @param TAG
     */
    public void justFinishedSelf(String TAG){
        Fragment f = getFragmentManager().findFragmentByTag(TAG);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
    }
    public void justHideSelf(Fragment current){
        if (current != null) {
            getFragmentManager().beginTransaction().hide(current).commitAllowingStateLoss();
        }
    }
}
