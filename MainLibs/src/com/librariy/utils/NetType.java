package com.librariy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * 判断是否 wap访问
 * @author 易申
 *
 */
public class NetType {
	private Context mContext;
	public static final String TAG_STRING = "NETTYPE" ;
	public NetType(Context context){
		mContext = context ;
	}

	public boolean isWap() {
		ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService( Context.CONNECTIVITY_SERVICE ); 
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE ); 
        
        if( mobNetInfo != null ) 
        { 
        	if (mobNetInfo.getState()==NetworkInfo.State.CONNECTED) {
        		if(mobNetInfo.getExtraInfo()!=null){
            		if (mobNetInfo.getExtraInfo().indexOf("wap")>0) {
            			return  true;
    				} 
            		else {
    					return false;
    				}
        		}else {
    				return false;
    			}

			}
        	else {
				return false;
			}
         
        } else {
        	 return false;
		}


        
       
		
	}
	
	
}
