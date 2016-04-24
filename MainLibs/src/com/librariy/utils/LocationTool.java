package com.librariy.utils;



import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
/**
 * 位置工具，获取用户经纬度
 * @author 易申
 *
 */
public class LocationTool {
	
		public static boolean isFirst=true;
		/**
		 * 得到位置<br>
		 * 需要下面权限
		 *    android.permission.ACCESS_FINE_LOCATION<br>
              android.permission.ACCESS_COARSE_LOCATION
		 * @return 位置<br>

		 */
		
		public   static  Location getLocation(Context context) {   
		       LocationManager locMan = (LocationManager) context   
		               .getSystemService(Context.LOCATION_SERVICE);  
//		       if (!locMan.isProviderEnabled(LocationManager.GPS_PROVIDER) &&isFirst) {
//		    	   new AlertDialog.Builder(AppContext.I.getHomeActivity())
//					.setTitle("开启GPS")
//					.setMessage("为了更精准的确定您的位置，请打开GPS")
//					.setPositiveButton("开启GPS",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									
//									Intent intent = new Intent();  
//							        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
//							        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//							        try   
//							        {  
//							        	AppContext.I.getHomeActivity().startActivity(intent);  
//							                      
//							              
//							        } catch(ActivityNotFoundException ex)   
//							        {  
//							              
//							            // The Android SDK doc says that the location settings activity  
//							            // may not be found. In that case show the general settings.  
//							              
//							            // General settings activity  
//							            intent.setAction(Settings.ACTION_SETTINGS);  
//							            try {  
//							            	AppContext.I.getHomeActivity().startActivity(intent);  
//							            } catch (Exception e) {
//							            	e.printStackTrace();
//							            	
//							            }  
//							        }
//							        dialog.cancel();
//								}
//							}).setNegativeButton("取消", null)
//					.create().show();
//			}
		       
		       Location location = locMan   
		                .getLastKnownLocation(LocationManager.GPS_PROVIDER);  
	
		       if (location== null ){   
		            location = locMan   
		           .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		            Log.i("LocationTool", "---location: use NET"); 
		       }   
		       //Log.i("LocationTool", "---location:"  + location.toString());   
		        return  location;   
		    } 

}
