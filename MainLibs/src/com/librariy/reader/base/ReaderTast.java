package com.librariy.reader.base;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import com.librariy.base.AppContextBase;
import com.librariy.http.AjaxCallBack;
import com.librariy.http.FinalHttp;
import com.librariy.reader.base.ReaderBase.Method;
import com.librariy.utils.Judge;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * 从服务器取数据
 * 需在自己项目实现 getContext（）；toast（）；
 * @author 易申
 *
 */
public abstract class ReaderTast {
	 ReaderBase reader;
	Handler myHandle =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				doStart();
				break;
			
			case 1:
			    doPost();
				break;
			case 2:
				doException();;
				break;
			default:
				break;
			}
		}
		};
	public ReaderTast()
	{
		
	}
	protected void  doPost() {
	    if(reader==null)
        {
            toast("数据传递异常，reader生成失败");
            return;
        }
	     AjaxCallBack<Object> callBack= new AjaxCallBack<Object>() {
	            @Override
	            public void onStart() {
	                // TODO Auto-generated method stub
	                if (isCancelled()) {
	                    return;
	                }
	            
	                super.onStart();
	                
	            }
	            @Override
	            public void onFailure(Throwable t, int errorNo, String strMsg) {
	                // TODO Auto-generated method stub
	                super.onFailure(t, errorNo, strMsg);
	                AppContextBase.log("Reader", "errorNo:"+errorNo+"errorMsg:"+strMsg);
	                if (reader!=null) {
	                    AppContextBase.log("Reader", "error!Url:"+reader.finalUrl);
	                }
	                if (t!=null) 
	                    t.printStackTrace();
	                doException(errorNo);
	                doComplete(null);
//	              hasException = true;
	            }
	            @Override
	            public void onSuccess(Object t) {
	                // TODO Auto-generated method stub
	                super.onSuccess(t);
	                 AppContextBase.log("Reader", "onSuccess");
	                if (reader!=null) {
	                    AppContextBase.log("Reader", "Url:"+reader.finalUrl);
	                    AppContextBase.log("Reader", "CallBackData:"+t);
	                    try {
	                        reader.initData(t+"");
	                    } catch (Exception e) {
	                        // TODO Auto-generated catch block
	                        if(e!=null)
	                            e.printStackTrace();
	                        doException("数据解析错误");
	                        doComplete(reader);
	                        return;
//	                      hasException = false;
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
	        };  
	        
	        /**
	         * 正式开始网络连接
	         */
	        FinalHttp http = new FinalHttp();
	        Map<String, String> headTempMap = reader.getHeadMap();
	        AppContextBase.log("Reader", "HeadMap:"+headTempMap+"");
	        AppContextBase.log("Reader", "ajaxParams:"+reader.ajaxParams+"");
	        for (Map.Entry me : headTempMap.entrySet()) {
	            String key = (String) me.getKey();
	            String value = (String) me.getValue();
	            http.addHeader(key, value);
	        }
	        if (Judge.StringNotNull(reader.getContentType())) {
	        	 http.addHeader("Content-Type", reader.getContentType());
			}
	       
	        Method method= reader.getMethod();
	        try {
	            switch (method) {
	            case POST:
	                http.post(reader.finalUrl, reader.ajaxParams, callBack);
	                break;
	            case GET:
	                http.get(reader.finalUrl, reader.ajaxParams, callBack);
	                break;
	            case PUT:
	                http.put(reader.finalUrl, reader.ajaxParams, callBack);
	                break;
	            case DELETE:
	                http.delete(reader.finalUrl, callBack);
	                break;
	            default:
	                break;
	            }
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
    }
	
	public  void execute( final String ...params) {
		if(isCancel)
			return;
		myHandle.sendEmptyMessage(0);
		  
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					reader=doReader(params);

	
					} catch (Exception e) {
						if (e != null)
							e.printStackTrace();  
						myHandle.sendEmptyMessage(2);
				
						return ;
					}finally {
						myHandle.sendEmptyMessage(1);
//						doComplete(null);
					}
			}
		}).start();;
		
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
	
	
	
//	boolean hasException = false;

	public abstract void toast(String msg);

	

	public abstract Context getContext();
	
	
	/**
	 * 开始线程
	 */
	public void doStart()
	{
		
	}
	
	/**
	 * 构造Reader
	 * @param params AsyncTask的参数
	 * @return
	 * @throws SocketTimeoutException
	 * @throws IOException
	 * @throws Exception
	 */
	public abstract ReaderBase doReader(String... params)
			throws  Exception;

	/**
	 * 后台线程结束后，在没有异常情况下调用本方法
	 * 
	 * @param reader
	 *            TODO
	 */
	public abstract void doSuccess(ReaderBase reader);
	
	
	/**
	 * 无论成功与否，都调用
	 * @param reader
	 */
	public void doComplete(ReaderBase reader) {
		if (isCancelled()) {
			return;
		}
	}
	
	/**
	 * 网络连接成功，但是服务器返回错误信息
	 * @param reader
	 */
	public void doFail(ReaderBase reader) {
		toast(reader.getErrorMessage());

	}

	/**
	 * 通用错误统一toast网络异常
	 * 网络异常，或者服务器连接异常或者服务器挂了
	 */
	public void doException() {
		
		String badInernetString = "您的网络不通，暂时无法访问服务器";
		if (getContext() != null && !isBackground(getContext()))
			toast(badInernetString);
		else {

		}
//		doComplete(null);
//		this.cancel(true);
	}
	/**
	 * 
	 * 兼容老的报错方式
	 * 新的调用方式可以不管
	 */
	public void doException(int errorNo) {
		
		doException();
//		doComplete(null);
//		this.cancel(true);
	}
	/**
	 * 数据解析错误
	 * 
	 */
	public void doException(String error) {
		
		if (getContext() != null && !isBackground(getContext()))
			toast(error);
		else {
			
		}
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
	
//	
//	private static class MyHandler extends Handler {  
//        WeakReference<Context> mActivity;  
//          
//        MyHandler(Context context){  
//            this.mActivity = new WeakReference<Context>(context);  
//        }  
//          
//        @Override  
//        public void handleMessage(Message msg) {  
//            switch(msg.what){  
//            case 0:
//            	
//            	break;
//            
//            }  
//        }  
//    }  
}
