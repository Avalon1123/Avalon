package com.librariy.utils;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 判断工具类，主要判断字符串 list map非空
 * @author 易申
 *
 */
public class Judge {
	public static boolean StringNotNull(String ss) {
		return (ss!=null&&(!"".equals(ss.trim())));
	}
	public static boolean StringNotNull(String ...ss) {
		boolean bb=true;
		if (ss!=null&&ss.length>0) {
			for(String k:ss)
			{
				bb=bb&&StringNotNull(k);
			}
		} else {
			return false;
		}
		return bb;
	}

	public static boolean ListNotNull(List list)
	{
		return (list!=null&&list.size()>0);
	}
	public static boolean MapNotNull(Map map)
	{
		return (map!=null&&map.size()>0);
	}
	public static boolean  isNetworkConnected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
		return mNetworkInfo.isAvailable(); 
		} 
		} 
		return false; 
		} 
//	public static boolean ObjectNotFinal(Object o)
//	{
//		boolean is=true;
//		try {
//			o=null;
//		} catch (Exception e) {
//			// TODO: handle exception
//			is=false;
//		}
//		return is;
//	}
}
