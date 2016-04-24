package com.librariy.base;


import com.librariy.reader.base.ComplieMode;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import cn.sharesdk.R;

public abstract class AppContextBase extends Application{
    static ComplieMode compleMode=ComplieMode.DEBUG;
    public static boolean isDebug()
    {
        return compleMode==ComplieMode.DEBUG;
    }
    /**
     * 是否强制显示广告滑动页
     */
    public static final boolean needShowGuide=true;
    public static void log(String tag,String msg,String type)
    {
        if(compleMode==ComplieMode.REALEASE)
            return;
        if ("e".equals(type)) {
            Log.e(tag, msg);
        } else if ("d".equals(type)){
            Log.d(tag, msg);
        }
        else {
            Log.i(tag, msg);
        }
    }
    public static void log(String tag,String msg)
    {
        log(tag, msg, "i");
    }
	private static int screen_WidthPx = 0;

	protected void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions defaultOptions() {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.considerExifParams(true);
		builder.showImageOnFail(R.drawable.pictures_no);
		builder.showImageOnLoading(R.drawable.pictures_no);
        builder.showImageForEmptyUri(R.drawable.pictures_no);
		return builder.build();
	}
	public static DisplayImageOptions defaultOptions(int failImg) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.considerExifParams(true);
		builder.showImageOnFail(failImg);
		builder.showImageOnLoading(failImg);
		builder.showImageForEmptyUri(failImg);
		return builder.build();
	}
	public static void setScreen_WidthPx(int screen_WidthPx) {
		AppContextBase.screen_WidthPx = screen_WidthPx;
	}

	public static int getScreen_WidthPx() {
		return screen_WidthPx;
	}

	private static int screen_HeightPx = 0;

	public static void setScreen_HeightPx(int screen_HeightPx) {
		AppContextBase.screen_HeightPx = screen_HeightPx;
	}

	public static int getScreen_HeightPx() {
		return screen_HeightPx;
	}
}
