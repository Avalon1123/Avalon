package com.librariy.base;

import java.lang.ref.WeakReference;

import com.librariy.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;

/**
 * Fragment 父类，加入一些便捷方法和 恢复时savedInstanceState处理 加入在viewpager中重用处理
 * 
 * @author 易申
 */
public abstract class FragmentBase extends Fragment {

    protected Bundle bundle = new Bundle();
    protected boolean isIntialized = false;

    public boolean isIntialized() {
        return isIntialized;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (savedInstanceState != null && savedInstanceState.containsKey("bundle") && savedInstanceState.getBundle("bundle") != null){
            bundle = savedInstanceState.getBundle("bundle");
        }
        if (view != null&&isCache) {
            return view;
        }        
        this.view=this.onSuperCreateView(inflater, container, savedInstanceState);
        if(view==null) return null;
        this.superInit();
        try {
            this.IntialComponent(view, savedInstanceState);
            this.IntialListener(view, savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.afterInit();
        ViewTreeObserver vx = view.getViewTreeObserver();
        vx.addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!isIntialized) {
                    if (onLoadListener != null){
                        onLoadListener.onFragmentLoaded();
                    }
                    isIntialized = true;
                    onLoaded();
                }
                return true;
            }
        });
        return view;
    }
    protected View onSuperCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId=getViewId();
        if(layoutId>0){
            return inflater.inflate(getViewId(), container, false);
        }else{
            return null;
        }
    }
    public void showDialog() {
        if (getActivity() != null)
            UIHelper.showDialog(getActivity());
    }

    public void dismissDialog() {
        UIHelper.dismissDialog();
    }
    protected void superInit() {

    }

    protected void onLoaded() {

    }

    protected void afterInit() {

    }

    /**
     * 设置为true 则不会销毁view主要用于viewpager重用
     */
    protected boolean isCache = false;

    public abstract SharedPreferences getSharedPreferences();

    // public SharedPreferences getUserInfo() {
    // return getSharedPreferences("userInfo");
    // }

    public void setCache(boolean isCache) {
        this.isCache = isCache;
    }

    protected abstract int getViewId();

    protected abstract void IntialComponent(View v, Bundle savedInstanceState) throws Exception ;

    protected abstract void IntialListener(View v, Bundle savedInstanceState) throws Exception ;

    public View findViewById(int id) {
        return view.findViewById(id);
    }
    public Intent getIntent() {
        if (getActivity() != null)
            return getActivity().getIntent();
        else {
            return null;
        }
    }

    // public void toast(String msg,int time)
    // {
    // Toast.makeText(getActivity(), msg, time).show();
    // }

    public void replace(int id, Fragment fragment) {

        // getFragmentManager().beginTransaction().replace(id,
        // fragment).commitAllowingStateLoss();
        getChildFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
        // getFragmentManager().beginTransaction().replace(id,
        // fragment).commit();
    }

    public void childReplace(int id, Fragment fragment) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(id, fragment).commitAllowingStateLoss();
    }

    public void hide() {
        hide(this);
    }

    public void hide(Fragment fragment) {

        getFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    public void show(Fragment fragment) {

        getFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
    }

    public void show() {
        show(this);
    }

    public Fragment getByTag(String tag) {
        return getFragmentManager().findFragmentByTag(tag);
    }

    public void toast(String msg) {
        // this.toast(msg, 200);
        UIHelper.showToast(getActivity(), msg);
    }

    public onLoadListener onLoadListener;

    public void setOnLoadListener(onLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public interface onLoadListener {
        void onFragmentLoaded();
    }

    public Bundle getSaveInstance() {
        return this.bundle;
    }

    public Object getSaveObject(String key, Object def) {
        if (this.bundle == null) {
            return def;
        }
        if (!this.bundle.containsKey(key))
            return def;
        return this.bundle.get(key);
    }

    public String getSaveString(String key, String def) {
        return (String) getSaveObject(key, def);
    }

    public String getSaveString(String key) {
        return (String) getSaveObject(key, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putBundle("bundle", bundle);
        // if(LemaoUtils.getLotteryApp()!=null)
        // {
        // try {
        // outState.putParcelable("app", LemaoUtils.getLotteryApp());
        // } catch (Exception e) {
        // // TODO: handle exception
        // e.printStackTrace();
        // }
        //
        // // outState.putSerializable("app", LemaoUtils.getLotteryApp());
        // }

        super.onSaveInstanceState(outState);

    }

    public static class MsgHandler extends Handler {
        private WeakReference<Activity> mActivity;

        MsgHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        if (isCache) {
            if (view != null) {
                ((ViewGroup) view.getParent()).removeAllViews();
            }
        }
        super.onDestroyView();
    }

    /**
     * back 拦截 true表示不拦截
     * 
     * @return
     */
    public boolean onBackPressed() {
        return true;
    }
}
