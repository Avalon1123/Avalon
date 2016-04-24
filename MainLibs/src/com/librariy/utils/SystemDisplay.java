package com.librariy.utils;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 单位转换工具
 * 
 * @author Administrator
 *
 */
public class SystemDisplay extends DisplayMetrics{
    private static String TAG="SystemDisplay";
    public final int statusBarHeightPixels;
    private SystemDisplay(DisplayMetrics mDisplayMetrics,int statusBarHeightPixels){
        if(mDisplayMetrics!=null) super.setTo(mDisplayMetrics);
        this.statusBarHeightPixels=statusBarHeightPixels;
    }
    public static SystemDisplay getInstance(Context mContext) {
        try {
            Resources r=(mContext == null)?Resources.getSystem():mContext.getResources();          
            int resourceId = r.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return new SystemDisplay(r.getDisplayMetrics(),r.getDimensionPixelSize(resourceId));
            }else{
                return new SystemDisplay(r.getDisplayMetrics(),0);
            }
        } catch (Exception e) {
            Log.e(TAG,"getInstance()", e);
            return new SystemDisplay(null,0);
        }
    }
    public float getDimension(int unit, float value) {
        return TypedValue.applyDimension(unit, value, this);
    }    
    // 获取屏幕显示区域
    public RectF getDisplayRect(boolean includeStatusBar) {
        RectF mRect = new RectF(0, 0, 0, 0);
        try {
            mRect.right = this.widthPixels;
            mRect.bottom = this.heightPixels;
            if (includeStatusBar) {
                return mRect;
            }
            mRect.top = this.statusBarHeightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mRect;
    }
}
