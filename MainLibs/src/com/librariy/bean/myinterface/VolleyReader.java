package com.librariy.bean.myinterface;



import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.librariy.reader.base.ReaderBase;
import com.librariy.utils.Judge;

import android.content.Context;
import android.os.Handler;

public abstract class VolleyReader {
    /**
     * 返回原始字符串，用作缓存
     */
    public String originData;
    HashMap<String,String> bodyparams=new HashMap<String,String>();
    HashMap<String,String> urlparams=new HashMap<String,String>();
    HashMap<String,String> header=new HashMap<String,String>();
    ReaderBase reader;
    Context context;
    public int timeout=30*1000;
    int retryTimes=0 ;

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
    public VolleyReader() {
        init();
    }

    public VolleyReader(Context context) {
        this.context = context;
        init();
    }

    protected void init() {

    }
    public HashMap<String, String> getBodyparams() {
        return bodyparams;
    }
    public HashMap<String, String> getUrlparams() {
        return urlparams;
    }
    public HashMap<String, String> getHeader() {
        return header;
    }
    public void addBodyParam(String key,String value)
    {
        bodyparams.put(key, value);
    }
    public void addBodyParam(String key,Object value)
    {
        bodyparams.put(key, value+"");
    }
    
    public void addBodyParamNotNull(String key,Object value)
    {
        if(value!=null)
            bodyparams.put(key, value+"");
    }
    public void addUrlParam(String key,String value)
    {
        urlparams.put(key, value);
    }
    public void addUrlParam(String key,Object value)
    {
        urlparams.put(key, value+"");
    }
    
    public void addUrlParamNotNull(String key,Object value)
    {
        if(value!=null)
            urlparams.put(key, value+"");
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public void addHeader(String key,String value)
    {
        header.put(key, value);
    }
    Handler myHandle =new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                if(readerListener!=null)
                    readerListener.start();
                break;
            case 1:
                if(readerListener!=null)
                    readerListener.compelete();
                break;
            case 2:
                if(readerListener!=null)
                    readerListener.compelete();
                break;
            case 3:
                if(readerListener!=null)
                    readerListener.start();
                break;
            case 4:
                if(readerListener!=null)
                    readerListener.start();
                break;

            default:
                break;
            }
        };
    };
    ReaderListener readerListener;
    protected void execute(final ReaderListener l) {
        if (l != null) {
            l.start();
        }
        RequestQueue req = Volley.newRequestQueue(context);
        StringRequest reader = new StringRequest(getMethod(), getUrl(), new Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                return getBodyparams();
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // TODO Auto-generated method stub
                return getHeaders();
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // TODO Auto-generated method stub
                try {  
                    
                    Map<String, String> responseHeaders = response.headers;  
//                  Log.i("rawCookies", cookie+"");
                    originData = new String(response.data, "UTF-8");  
                    return Response.success(originData,HttpHeaderParser.parseCacheHeaders(response));  
                } catch (UnsupportedEncodingException e) {  
                    return Response.error(new ParseError(e));  
                } 
            }
        };
        DefaultRetryPolicy df=new DefaultRetryPolicy(timeout, retryTimes, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        reader.setRetryPolicy(df);
        req.add(reader);
    }
    /**
     * method 请求方式默认post
     * 
     * @return
     */
    public int getMethod() {
        return Method.POST;
    }

    /**
     * 网络请求链接地址
     * 
     * @return
     */
    public String getUrl() {
        HashMap<String, String> m=getUrlparams();
        if (Judge.MapNotNull(m)) {
            return getPostUrl()+getUrlParms(m);
        }
        return getPostUrl();
    }

    /**
     * 服务器地址
     * 
     * @return
     */
    public abstract String getPostUrl();
    private String getUrlParms(Map<String, String> parms) {
        String s="";
        if (Judge.MapNotNull(parms)) {
            s+="?";
            for(String k:parms.keySet())
            {
                if(Judge.StringNotNull(k))
                {
                    s+=k+"=";
                    String v=parms.get(k);
                    if (Judge.StringNotNull(v)) {
                        s+=v+"&";
                    } else {
                        s+="&";
                    }
                }
                
            }
            s=s.substring(0, s.length()-1);
        }
        return s;
    }
}
