package com.librariy.reader.base;

import java.util.List;

import com.librariy.utils.Judge;
import com.librariy.utils.UIHelper;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public abstract class ReaderCallBack {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public ReaderCallBack(Context context) {
        this.context = context;
    }

    public ReaderCallBack() {
    }

    /**
     * 开始线程
     */
    public void doStart() {
        if (getContext() != null && ifNeedDialog) {
            UIHelper.showDialog(getContext());
        }
    }

    /**
     * 后台线程结束后，在没有异常情况下调用本方法
     * 
     * @param reader
     *            TODO
     */
    public abstract void doSuccess(ReaderBase reader);

    /**
     * 无论成功与否，都调用
     * 
     * @param reader
     */
    public void doComplete(ReaderBase reader) {
        if (ifNeedDialog)
            UIHelper.dismissDialog();
        if (isCancelled()) {
            return;
        }
    }

    private boolean isCancel = false;

    public boolean isCancelled() {
        return isCancel;
    }

    public void cancel(boolean cancel) {
        isCancel = cancel;
    }

    /**
     * 网络连接成功，但是服务器返回错误信息
     * 
     * @param reader
     */
    public void doFail(ReaderBase reader) {
        toast(reader.getErrorMessage());

    }

    /**
     * 通用错误统一toast网络异常 网络异常，或者服务器连接异常或者服务器挂了
     */
    public void doException(String msg) {

        String badInernetString = "暂时无法访问服务器，请稍后再试";
        if (getContext() != null && !isBackground(getContext()))
            toast(Judge.StringNotNull(msg)?msg:badInernetString);
        else {

        }
    }
    /**
     * 原始错误
     */
    public void doException(int errorCode,String errorMsg) {
    	doException(errorMsg);
    }
    	

    public void toast(String msg) {
        if (context != null)
            UIHelper.showToast(context, msg);
    }

    protected Context getContext() {
        return context;
    }

    /**
     * 判断应用是在后台还是前台
     * 
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);

                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    private boolean ifNeedDialog = true;

    public ReaderCallBack needDialog(boolean needDialog) {
        this.ifNeedDialog = needDialog;
        return this;
    }
}
