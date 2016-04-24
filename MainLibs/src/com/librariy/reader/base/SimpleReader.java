package com.librariy.reader.base;

import java.util.List;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public abstract class  SimpleReader extends StringRequest{
    
    public SimpleReader(int method, String url, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        // TODO Auto-generated constructor stub
    }
    public SimpleReader(String url, Listener<String> listener, ErrorListener errorListener) {
        super(url, listener, errorListener);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 判断应用是在后台还是前台
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
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
}
