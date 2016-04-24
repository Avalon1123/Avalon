package com.librariy.reader.base;

import java.io.UnsupportedEncodingException;
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
import com.librariy.base.AppContextBase;

/**
 * 从服务器取数据 需在自己项目实现 getContext（）；toast（）；
 * 
 * @author 易申
 * 
 */
public abstract class VolleyReaderTast extends ReaderTast {
    String originData;
    public int timeout = 30 * 1000;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    int retryTimes = 0;

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    protected void doPost() {
        if (reader == null) {
            toast("数据传递异常，reader生成失败");
            return;
        }
        RequestQueue req = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(getMethod(reader.getMethod()), reader.getFinalUrl(), new Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                AppContextBase.log("Reader", "onSuccess");
                if (reader != null) {
                    AppContextBase.log("Reader", "Url:" + reader.finalUrl);
                    AppContextBase.log("Reader", "CallBackData:" + arg0);
                    try {
                        reader.initData(arg0 + "");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        if (e != null)
                            e.printStackTrace();
                        doException("数据解析错误");
                        doComplete(reader);
                        return;
                        // hasException = false;
                    }
                    try {
                        if (isCancelled())
                            return;

                        if (reader.getIsSuccess()) {
                            doSuccess(reader);
                        } else {
                            doFail(reader);
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        if (e != null)
                            e.printStackTrace();
                        doException();
                    } finally {
                        doComplete(reader);
                    }
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                AppContextBase.log("Reader", "error:" + arg0.getMessage());
                if (reader != null) {
                    AppContextBase.log("Reader", "error!Url:" + reader.finalUrl);
                }
                doException();
                doComplete(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                return reader.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // TODO Auto-generated method stub
                return reader.getHeadMap();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // TODO Auto-generated method stub
                try {

                    Map<String, String> responseHeaders = response.headers;
                    // Log.i("rawCookies", cookie+"");
                    originData = new String(response.data, "UTF-8");
                    return Response.success(originData, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        DefaultRetryPolicy df = new DefaultRetryPolicy(timeout, retryTimes, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(df);
        req.add(request);
    };

    /**
     * method 请求方式默认post
     * 
     * @return
     */
    public int getMethod(ReaderBase.Method method) {
        switch (method) {
        case POST:
            return Method.POST;
        case GET:
            return Method.GET;
        case DELETE:
            return Method.DELETE;
        case PUT:
            return Method.PUT;

        default:
            break;
        }
        return Method.POST;
    }
    //
    // private static class MyHandler extends Handler {
    // WeakReference<Context> mActivity;
    //
    // MyHandler(Context context){
    // this.mActivity = new WeakReference<Context>(context);
    // }
    //
    // @Override
    // public void handleMessage(Message msg) {
    // switch(msg.what){
    // case 0:
    //
    // break;
    //
    // }
    // }
    // }
}
