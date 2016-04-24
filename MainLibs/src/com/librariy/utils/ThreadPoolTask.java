package com.librariy.utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
public abstract class ThreadPoolTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void executeOnThreadPool(Params... params) {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
        }else{
            super.execute(params);
        }
    }
}
