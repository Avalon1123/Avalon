package com.librariy.reader.base;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
/**
 *考虑到不需要AsyncTask 暂弃用
 * 从服务器取数据
 * 需在自己项目实现 getContext（）；toast（）；
 * @author 易申
 *
 */
public abstract class OldReaderTast extends AsyncTask<String, Integer, ReaderBase> {

	boolean hasException = false;

	public abstract void toast(String msg);

	

	public abstract Context getContext();

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		hasException = false;
		
	}
	@Override
	protected ReaderBase doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (isCancelled())
			return null;
		try {
			 final ReaderBase reader=doReader(params);

//			reader.setCallBack(new AjaxCallBack<Object>() {
//				@Override
//				public void onStart() {
//					// TODO Auto-generated method stub
//					if (isCancelled()) {
//						return;
//					}
//					doStart();
//					super.onStart();
//					
//				}
//				@Override
//				public void onFailure(Throwable t, int errorNo, String strMsg) {
//					// TODO Auto-generated method stub
//					super.onFailure(t, errorNo, strMsg);
//					
//					if (t!=null) 
//						t.printStackTrace();
//					hasException = true;
//				}
//				@Override
//				public void onSuccess(Object t) {
//					// TODO Auto-generated method stub
//					super.onSuccess(t);
//					if (reader!=null) {
//						try {
//							reader.initData(t+"");
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							if(e!=null)
//								e.printStackTrace();
//							hasException = false;
//						}
//					}
//					
//					
//					try {
//						if (isCancelled())
//							return;
//						if (hasException) {
//							doException();
//							doComplete(reader);
//							return;
//						}
//
//						if (reader.getIsSuccess()) {
//							doSuccess(reader);
//						} else {
//							doFail(reader);
//						}
//
//					} catch (Exception e) {
//						// TODO: handle exception
//						if (e != null)
//							e.printStackTrace();
//						doException();
//					} finally {
//						doComplete(reader);
//					}
//				}
//			});
//			reader.start();
			return reader;
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
			hasException = true;
			return null;
		}
	}

	@Override
	protected void onPostExecute(ReaderBase reader) {
		// TODO Auto-generated method stub


	}
	/**
	 * 开始网络请求
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
			throws SocketTimeoutException, IOException, Exception;

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
		this.cancel(true);
		String badInernetString = "网络异常";
		if (getContext() != null && !isBackground(getContext()))
			toast(badInernetString);
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
}
