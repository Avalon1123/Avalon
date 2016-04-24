package com.librariy.view;

import com.librariy.utils.IHttp;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;

public class MyWebView extends HijackWebView {
    String firstUrl="";
    String url;
    IHttp  httpListener;
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    public boolean IsBacktoReloadPoint()
    {
        return firstUrl.equals(getUrl());
    }
    public void setProcessListener(IHttp httpListener) {
        this.httpListener = httpListener;
        
    }
    @Override
    public void Intial() {
        // TODO Auto-generated method stub
        super.Intial();
        WebSettings setting=getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDomStorageEnabled(true);
        setProceesChangeListener(new IHttp() {
            
            @Override
            public void OnPostStart() {
                // TODO Auto-generated method stub
                
                if(httpListener!=null)
                    httpListener.OnPostStart();
            }
            
            @Override
            public void OnPostCompelete() {
                // TODO Auto-generated method stub
                if(httpListener!=null)
                    httpListener.OnPostCompelete();
            }
            
            @Override
            public void OnPostProcess(int newProgress) {
                // TODO Auto-generated method stub
//              Log.i("newProgress", newProgress+"");
                if(httpListener!=null)
                    httpListener.OnPostProcess(newProgress);
            }

            @Override
            public void OnException(Exception e) {
                if(httpListener!=null)
                    httpListener.OnException(e);
                // TODO Auto-generated method stub
                
            }

        
        });
        
    
    }
}
