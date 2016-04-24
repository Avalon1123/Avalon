package com.librariy.utils;


import com.librariy.base.ClientInfo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
/**
 * 读取AndroidManifest.xml信息
 * @author 易申
 *
 */
public class GetManifest {
	Activity activity;
	   /**
  * activity MetaData读取
  */
	public GetManifest(Activity a) {
		this.activity=a;
	}
 public  String GetActivityMetaData(String value) {
     ActivityInfo info;
    
     
     try {
         info = activity.getPackageManager().getActivityInfo(activity.getComponentName(),
                 PackageManager.GET_META_DATA);


         String msg = info.metaData.get(value)+"";

         return msg;

     } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return "";
     }
 }
 
 // 获取ApiKey
 public static String getMetaValue(Context context, String metaKey) {
     Bundle metaData = null;
     String apiKey = null;
     if (context == null || metaKey == null) {
         return null;
     }
     try {
         ApplicationInfo ai = context.getPackageManager()
                 .getApplicationInfo(context.getPackageName(),
                         PackageManager.GET_META_DATA);
         if (null != ai) {
             metaData = ai.metaData;
         }
         if (null != metaData) {
             apiKey = metaData.getString(metaKey);
         }
     } catch (NameNotFoundException e) {

     }
     return apiKey;
 }
 /**
  * appliction MetaData读取
  */
 public  String GetApplicationMetaData(String value) {
     ApplicationInfo info;
     try {
         info = activity.getPackageManager().getApplicationInfo(
         		activity.getPackageName(), PackageManager.GET_META_DATA);

         
         String msg = info.metaData.get(value)+"";
       return msg;


     } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return "";
     }
 }
 /**
  * 测试版本信息
  */
 public  void GetVersion(){
     PackageInfo pkg;
     try {
         pkg = activity.getPackageManager().getPackageInfo(activity.getApplication().getPackageName(), 0);
         String appName = pkg.applicationInfo.loadLabel(activity.getPackageManager()).toString(); 
//         String versionName = pkg.versionName; 
         ClientInfo.VERSION_NUM=pkg.versionCode;
         ClientInfo.VERSION=pkg.versionName;
         ClientInfo.SOURCE=GetApplicationMetaData("UMENG_CHANNEL");
//         System.out.println("appName:" + appName);
//         System.out.println("versionName:" + versionName);
     } catch (NameNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     } 
  }
 
 
// 输出如下：
//
// 16:37:54.442: I/System.out(10213): appName:Test1
// 16:37:54.442: I/System.out(10213): versionName:1.0
// 16:37:54.442: I/System.out(10213): tel:cdma-13366350377
// 16:37:54.442: I/System.out(10213): channel:eben
// 16:37:54.452: I/System.out(10213): name:自强不息
// 16:37:54.452: I/System.out(10213): city:北京
}
