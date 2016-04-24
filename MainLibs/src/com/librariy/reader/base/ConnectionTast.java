package com.librariy.reader.base;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.librariy.base.AppContextBase;
import com.librariy.net.HttpClient;
import com.librariy.net.HttpRequest;
import com.librariy.net.HttpResponse;
import com.librariy.utils.Judge;
import com.librariy.utils.Log;

import android.content.Context;
/**
 * 20150727 后加入新Http请求方式，加入文件上传，Google volley框架
 * @author 易申
 *
 */
public class ConnectionTast {
	boolean userFile=false;
    public void setUserFile(boolean userFile) {
		this.userFile = userFile;
	}
	public ConnectionTast(Context context)
    {
        this.context=context;
    }
    private  Context context;
    public  void excute(ReaderBase data,ReaderCallBack callBack)
    {
            if (callBack!=null&&callBack.getContext()==null) {
                callBack.setContext(context);
            }
            if(callBack!=null)
                callBack.doStart();
          if (Judge.ListNotNull(data.getFileMap())||userFile) {
              doPostFile(data,callBack);
        } else {
            doPostVolley(data, callBack);
        }
    }
    protected Context getContext() {
        return context;
    }
    protected void doPostFile(final ReaderBase data,final ReaderCallBack callBack) {
        if(data==null)
        {   
            if(callBack!=null)
            {
                callBack.doException("数据解析错误");
                callBack.doComplete(data);
            }
            return;
        }
        HttpRequest mHttpRequest=new HttpRequest(HttpRequest.Method.POST_BIN, data.getFinalUrl());
        if (Judge.MapNotNull(data.getHeadMap())) {
            for(String s:data.getHeadMap().keySet())
            {
                if (Judge.StringNotNull(s)) {
                    mHttpRequest.setHeader(s,data.getHeadMap().get(s));
                }
            }
        }
        if (Judge.MapNotNull(data.getParams())) {
            for(String s:data.getParams().keySet())
            {
                if (Judge.StringNotNull(s)) {
                    mHttpRequest.addFormField(s,data.getParams().get(s));
                }
            }
        }
        if (Judge.ListNotNull(data.getFileMap())) {
        	mHttpRequest.addAllFormField(data.getFileMap());
        }

        
       
//        mHttpRequest.addBodyField("Sex","男");
//        mHttpRequest.addBodyField("Photo",new File("D:/temp/pic/222222.png"));
//        mHttpRequest.addBodyField("Photo",new File("D:/temp/pic/20150613103939.png"));
//        mHttpRequest.addBodyField("IdentityCard",new File("D:/temp/pic/222222.png"));
//        mHttpRequest.addBodyField("IdentityCard",new File("D:/temp/pic/20150613103939.png"));
//        mHttpRequest.addHeader("x", "198.2254444111");
//        mHttpRequest.addHeader("y", "25.2254444111");

        HttpClient.request(mHttpRequest,new HttpResponse(){
            @Override
            public void onCompleted(int responseCode, String responseText) {
//                AppContextBase.log("CallBack", responseText+"");
//                AppContextBase.log("responseCode", responseCode+"");
                if (responseCode==200) {
                    initData(responseText, data, callBack);
                } else {
                    AppContextBase.log("Reader", "error:" + "responseCode="+responseCode+"/nresponseText="+responseText);
                    if (data != null) {
                        AppContextBase.log("Reader", "error!Url:" + data.finalUrl);
                    }
                    if(callBack!=null)
                    {
//                        callBack.doException("服务器繁忙，请稍后再试");
                        callBack.doException(responseCode,responseText);
                        callBack.doComplete(null);
                    }
                }
            }
        });
    }
    protected  void doPostVolley(final ReaderBase data,final ReaderCallBack callBack) {
        if(data==null)
        {   
            if(callBack!=null)
            {
                callBack.doException("数据解析错误");
                callBack.doComplete(data);
            }
            return;
        }
        RequestQueue req = Volley.newRequestQueue(getContext());
        
        StringRequest request = new StringRequest(getMethod(data.getMethod()), data.getFinalUrl(), new Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                initData(arg0, data, callBack);
                
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
            	Log.e("LOGIN-ERROR", arg0.getMessage(), arg0);
            	int statusCode=-1;
            	String errowMsg="";
            	try {
            		statusCode=arg0.networkResponse.statusCode;
            		byte[] htmlBodyBytes = arg0.networkResponse.data;
            		errowMsg=initError(new String(htmlBodyBytes));
            		AppContextBase.log("Reader", "error:" +  errowMsg);
				} catch (Exception e) { 
					// TODO: handle exception
					e.printStackTrace();
					  AppContextBase.log("Reader", "error:" + "网络异常");
				}
            
                AppContextBase.log("Reader", "error:" + arg0.getMessage()+errowMsg);
              
                if (data != null) {
                    AppContextBase.log("Reader", "error!Url:" + data.finalUrl);
                }
                if(callBack!=null)
                {
                	if (statusCode==-1) {
                		 callBack.doException(statusCode,"您的网络异常，请检查网络情况");
					} else {
						 callBack.doException(statusCode,errowMsg+"");
					}
                   
                    callBack.doComplete(null);
                }
                
            }
        }) {
        	
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                HashMap<String, String> params=data.getParams();
                AppContextBase.log("Reader", "ajaxParams:"+params+"");
                return params;
            }

            @Override
            public String getBodyContentType() {
            	// TODO Auto-generated method stub
            	
            	 try {
            		
            		 String s=data.getContentType();
            		 return Judge.StringNotNull(s)?s:super.getBodyContentType();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
            	return super.getBodyContentType();
            	
            	
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // TODO Auto-generated method stub
                HashMap<String, String> headMap=(HashMap<String, String>) data.getHeadMap();
//                Map<String, String> headMap=super.getHeaders();
                if (data.isNeedRemoveContentType()) {
                	 headMap.remove("Content-Type");
				}
               
                AppContextBase.log("Reader", "HeadMap:"+headMap+"");
                return headMap;
            }
            
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // TODO Auto-generated method stub
                try {

                    Map<String, String> responseHeaders = response.headers;
                    // Log.i("rawCookies", cookie+"");
                    String originData = new String(response.data, getStringType());
                    return Response.success(originData, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        DefaultRetryPolicy df = new DefaultRetryPolicy(30 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(df);
        request.setTag(getContext().toString());
        req.add(request);
    };
    public String initError(String errowMsg)
    {
    	if (Judge.StringNotNull(errowMsg)) {
    		ErrorMsg error=new Gson().fromJson(errowMsg+"", new TypeToken<ErrorMsg>(){}.getType());
    		AppContextBase.log("Reader", "error:" +  errowMsg);
    		if(error!=null&&Judge.StringNotNull(error.ErrorMessage))
    		{
    			errowMsg=error.ErrorMessage;
    		}else if (error!=null&&Judge.StringNotNull(error.Message)) {
    			errowMsg=error.Message;
			}else {
				errowMsg="";
			}
		}
    	
    	return errowMsg;
    }
    /**
     * method 请求方式默认post
     * 
     * @return
     */
    protected  int getMethod(ReaderBase.Method method) {
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
    private boolean isCancel=false;
    public boolean isCancelled()
    {
        return isCancel;
    }
    public void cancel(boolean cancel)
    {
        isCancel=cancel;
    }
    protected void  initData(String originData,final ReaderBase data,final ReaderCallBack callBack) {
        AppContextBase.log("Reader", "onSuccess");
        if (data != null) {
            AppContextBase.log("Reader", "Url:" + data.finalUrl);
            AppContextBase.log("Reader", "CallBackData:" + originData);
            try {
                data.initData(originData + "");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (e != null)
                    e.printStackTrace();
                if(callBack!=null)
                {
                    callBack.doException("数据解析错误");
                    callBack.doComplete(data);
                }
               
                return;
                // hasException = false;
            }
            try {
                if (isCancelled())
                    return;

                if (data.getIsSuccess()) {
                    callBack.doSuccess(data);
                } else {
                    callBack.doFail(data);
                }

            } catch (Exception e) {
                // TODO: handle exception
                if (e != null)
                    e.printStackTrace();
                if(callBack!=null)
                {
                    callBack.doException("数据解析错误");
                }
            } finally {
                callBack.doComplete(data);
            }
        }
    }
    /**
     * 服务器报错不统一产物
     * 统一后去掉
     * @author 申
     *
     */
    public class ErrorMsg
    {
    	public String ErrorMessage;
    	public String Message;
    }
    public String getStringType()
    {
    	return "UTF-8";
    }
}
