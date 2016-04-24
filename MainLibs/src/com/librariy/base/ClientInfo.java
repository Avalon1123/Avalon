package com.librariy.base;

import java.util.HashMap;
import java.util.Map;
/**
 * 头信息
 * @author 易申
 *
 */
public class ClientInfo {
	public static Map<String, String> headMap=new HashMap<String, String>();
	/**
	 * 渠道
	 */
	public static String  SOURCE ="";
	/**
	 * 版本号
	 */
	public static int  VERSION_NUM =0;
	/**
	 * 外部版本
	 */
	public static String  VERSION ="";
	
	/**
	 * 客服端设备ID
	 */
	public static String  DEVICE_ID = "0000";
	
	/**
	 * 当前手机分辨率
	 */
	public static String  SCREEN = "0000";
	
	
	/**
	 * 版本信息
	 */
	public static String systemInfo = "android";
	
	/**
	 * 接入点是否是使用wap方式
	 */
	public static boolean  iswap = false ;
	
	/**
	 * 机器型号
	 */
	public static String  MODEL  = "0000";
	
	/**
	 * 经度
	 */
	public static String LAT = "0000";
	/**
	 * 纬度
	 */
	public static String LONG = "0000";
	
	/**
	 * Ip
	 */
	public static String MAC_IP  = "0000";
	
	
	
}
