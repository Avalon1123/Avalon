package com.librariy.utils;
import java.lang.reflect.Field;

import com.librariy.base.AppContextBase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;
/**
 * 单位转换工具
 * @author Administrator
 *
 */
public class DimenTool {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		if(context==null)
			return 0;
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	 
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		if(context==null)
			return 0;
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static float dip2px_f(Context context, float dpValue) {
		if(context==null)
			return 0;
		final float scale = context.getResources().getDisplayMetrics().density;
		return dpValue * scale ;
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static float px2dip_f(Context context, float pxValue) {
		if(context==null)
			return 0;
		final float scale = context.getResources().getDisplayMetrics().density;
		return pxValue / scale ;
	}

	
	/**
	 * 获取手机的宽(像素)
	 */
	public static int getWidthPx(Context context) {
//		return context.getResources().getDisplayMetrics().widthPixels;
		if (AppContextBase.getScreen_WidthPx()<=0) {
			if(context==null)
				return 0;
			DisplayMetrics dm = new DisplayMetrics(); 
			
			try {
				  Activity a=(Activity) context;
				  a.getWindowManager().getDefaultDisplay().getMetrics(dm);
			} catch (Exception e) {
				// TODO: handle exception
				return 0;
			}
	      
	      
			AppContextBase.setScreen_HeightPx(dm.heightPixels);
			AppContextBase.setScreen_WidthPx(dm.widthPixels);
		}

		return AppContextBase.getScreen_WidthPx();
	}
	/**
	 * 获取手机的高(像素)
	 */
	public static int getHeightPx(Context context) {
		if (AppContextBase.getScreen_WidthPx()<=0) {
			if(context==null)
				return 0;
			DisplayMetrics dm = new DisplayMetrics(); 
			
			try {
				  Activity a=(Activity) context;
				  a.getWindowManager().getDefaultDisplay().getMetrics(dm);
			} catch (Exception e) {
				// TODO: handle exception
				return 0;
			}
	      
	      
			AppContextBase.setScreen_HeightPx(dm.heightPixels);
			AppContextBase.setScreen_WidthPx(dm.widthPixels);
		}

		return AppContextBase.getScreen_HeightPx();
		
		
	}
	/**
	 * 获取屏幕可显示区域高度
	 */
	    public static int getDisplayHeight(Context context) {
	        Rect frame = new Rect();
	        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	        int statusBarHeight = getStatusBarHeight(context);
	    //得到屏幕的整个高度
	        int mFullDisplayHeight = ((Activity)context).getWindowManager().getDefaultDisplay()
	                .getHeight();
	        
	        int contentViewTop = ((Activity)context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	        int titleBarHeight = contentViewTop - statusBarHeight;
	    //得到可显示屏幕高度
	        int mDisplayHeight = mFullDisplayHeight - statusBarHeight;
	        if(titleBarHeight>0)
	        	mDisplayHeight=mDisplayHeight - titleBarHeight;
	        // Log.d("TEST", "status bar height:" + statusBarHeight);
	        // Log.d("TEST", "mFullDisplayHeight====" + mFullDisplayHeight);
	        return mDisplayHeight;
	    }
	    public static int getStatusBarHeight(Context context){ 
            Class<?> c = null; 
            Object obj = null; 
            Field field = null; 
            int x = 0, statusBarHeight = 0; 
            try { 
                c = Class.forName("com.android.internal.R$dimen"); 
                obj = c.newInstance(); 
                field = c.getField("status_bar_height"); 
                x = Integer.parseInt(field.get(obj).toString()); 
                statusBarHeight = context.getResources().getDimensionPixelSize(x);  
//                Log.v("@@@@@@", "the status bar height is : " + statusBarHeight); 
            } catch (Exception e1) { 
                e1.printStackTrace(); 
            }  
            return statusBarHeight; 
        }
	
}
