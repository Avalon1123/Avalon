package com.librariy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.librariy.base.AppContextBase;
import com.librariy.bean.OptionItem;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import cn.sharesdk.R;

public class Utils {

    /**
     * 照片缓存目录
     */
    public final static String CACHE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/xiaofund";
    /**
     * 房源压缩照片缓存目录
     */
    public final static String HOUSE_CACHE_FILE_PATH = CACHE_FILE_PATH + "/house";

    /**
     * ReleaseHouse缓存目录
     */
    public final static String RELEASE_CACHE_FILE_PATH = CACHE_FILE_PATH + "/release";

    /**
     * 获取支持银行列表
     * 
     * @return
     */
    public static List<String> getbankList() {
        List<String> bankList = new ArrayList<String>();
        bankList.add("中国银行");
        bankList.add("中国建设银行");
        bankList.add("中国工商银行");
        bankList.add("中国农业银行");
        bankList.add("招商银行");
        bankList.add("交通银行");
        bankList.add("浦发银行");
        bankList.add("兴业银行");
        bankList.add("广发银行");
        bankList.add("中信银行");
        bankList.add("平安银行");
        bankList.add("中国邮政储蓄银行");
        bankList.add("中国光大银行");
        bankList.add("中国民生银行");
        bankList.add("华夏银行");
        bankList.add("武汉农村商业银行");
        bankList.add("汉口银行");

        return bankList;
    }
    public static List<OptionItem> getBankListWithIcon() {
        List<OptionItem> bankList = new ArrayList<OptionItem>();
        bankList.add(new OptionItem("中国银行",R.drawable.ic_bank_zhongguo));
        bankList.add(new OptionItem("中国建设银行",R.drawable.ic_bank_jianshe));
        bankList.add(new OptionItem("中国工商银行",R.drawable.ic_bank_gongshang));
        bankList.add(new OptionItem("中国农业银行",R.drawable.ic_bank_nongye));
        bankList.add(new OptionItem("招商银行",R.drawable.ic_bank_zhaoshang));
        bankList.add(new OptionItem("交通银行",R.drawable.ic_bank_jiaotong));
        bankList.add(new OptionItem("浦发银行",R.drawable.ic_bank_pufa));
        bankList.add(new OptionItem("兴业银行",R.drawable.ic_bank_xingye));
        bankList.add(new OptionItem("广发银行",R.drawable.ic_bank_guangfa));
        bankList.add(new OptionItem("中信银行",R.drawable.ic_bank_zhongxin));
        bankList.add(new OptionItem("平安银行",R.drawable.ic_bank_pingan));
        bankList.add(new OptionItem("中国邮政储蓄银行",R.drawable.ic_bank_youzheng));
        bankList.add(new OptionItem("中国光大银行",R.drawable.ic_bank_guangda));
        bankList.add(new OptionItem("中国民生银行",R.drawable.ic_bank_minsheng));
        bankList.add(new OptionItem("华夏银行",R.drawable.ic_bank_huaxia));
        bankList.add(new OptionItem("武汉农村商业银行",R.drawable.ic_bank_wh_nongcunsy));
        bankList.add(new OptionItem("汉口银行",R.drawable.ic_bank_hankou));
        return bankList;
    }

    /**
     * 返回 上传服务器 ArrayList 结构数据 类型为bitmap
     * 
     * @param tag
     * @param b1
     * @return
     */
    public static Map<String, Bitmap> getBitmapMap(String tag, List<Bitmap> b1) {
        Map<String, Bitmap> m = new ArrayMap<String, Bitmap>();
        int i = 0;
        for (Bitmap b : b1) {
            if (b != null) {
                m.put(tag + "[" + i + "]", b);
                i++;
            }
        }
        return m;
    }

    private static final Pattern PHONE_NO = Pattern.compile("^[1]\\d{10}$");

    private static final Pattern IDENTITY_NO = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");

    private static final Pattern MAIL = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

    public static boolean isPhoneNOValid(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        final Matcher matcher = PHONE_NO.matcher(phone);
        return matcher.matches();
    }

    public static boolean isIdentityNOValid(final String idno) {
        if (TextUtils.isEmpty(idno)) {
            return false;
        }
        final Matcher matcher = IDENTITY_NO.matcher(idno);
        return matcher.matches();
    }

    public static boolean isMailNOValid(final String mail) {
        if (TextUtils.isEmpty(mail)) {
            return false;
        }
        final Matcher matcher = MAIL.matcher(mail);
        return matcher.matches();
    }

    /** 将给定字符串中间字符从指定位置用省略号代替,前后保留四位字符,若位数小于或等于8位，则直接返回原来字符串 */
    public static String replaceSubString(String str) {
        StringBuffer sb = new StringBuffer();
        if (str.length() <= 8) {
            return str;
        }
        try {
            sb.append(str.substring(0, 4)).append("******").append(str.substring(str.length() - 4, str.length()));
            return sb.toString();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;

    }

    public static String readFile(String file) {
        StringBuffer s = new StringBuffer("");

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                s.append(line);
                s.append("\n");
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s.toString();

    }

    public static void writeFileToSD(Context context, String fileName, String filePath, String fileValue) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            UIHelper.showToast(context, "SD card is not avaiable/writeable right now");
            return;
        }
        try {
            File path = new File(filePath);
            File FILE_PATH = new File(CACHE_FILE_PATH);
            File file = new File(filePath + "/" + fileName);
            if (!FILE_PATH.exists()) {
                Log.d("TestFile" + fileName, "Create the path:" + filePath);
                FILE_PATH.mkdir();
            }
            if (!path.exists()) {
                Log.d("TestFile" + fileName, "Create the path:" + filePath);
                path.mkdir();
            }
            if (!file.exists()) {
                Log.d("TestFile" + fileName, "Create the file:" + fileName);
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);
            byte[] buf = fileValue.getBytes();
            stream.write(buf);
            stream.close();

        } catch (Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
            UIHelper.showToast(context, "文件写入失败");
            e.printStackTrace();
        }
    }
    public static void writeFileToSD(Context context, String fileName, String filePath, byte[] buf) {
    	String sdStatus = Environment.getExternalStorageState();
    	if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
    		UIHelper.showToast(context, "SD card is not avaiable/writeable right now");
    		return;
    	}
    	try {
    		File path = new File(filePath);
    		File FILE_PATH = new File(CACHE_FILE_PATH);
    		File file = new File(filePath + "/" + fileName);
    		if (!FILE_PATH.exists()) {
    			Log.d("TestFile" + fileName, "Create the path:" + filePath);
    			FILE_PATH.mkdir();
    		}
    		if (!path.exists()) {
    			Log.d("TestFile" + fileName, "Create the path:" + filePath);
    			path.mkdir();
    		}
    		if (!file.exists()) {
    			Log.d("TestFile" + fileName, "Create the file:" + fileName);
    			file.createNewFile();
    		}
    		FileOutputStream stream = new FileOutputStream(file);
    		stream.write(buf);
    		stream.close();
    		
    	} catch (Exception e) {
    		Log.e("TestFile", "Error on writeFilToSD.");
    		UIHelper.showToast(context, "文件写入失败");
    		e.printStackTrace();
    	}
    }

    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            AppContextBase.log("deleteFile", "删除文件失败", "d");
            ;
        }
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        return hexString(hash);
    }

    public static final String hexString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            buffer.append(hexString(bytes[i]));
        }
        return buffer.toString();
    }

    public static final String hexString(byte byte0) {
        char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char ac1[] = new char[2];
        ac1[0] = ac[byte0 >>> 4 & 0xf];
        ac1[1] = ac[byte0 & 0xf];
        String s = new String(ac1);
        return s;
    }

    /**
     * 获取百度推送最高权限的包名
     */

    public final static String SERVICE_ACTION = "com.baidu.android.pushservice.action.PUSH_SERVICE";

    public static String getHighPriorityPackage(Context context) {
        Intent i = new Intent(SERVICE_ACTION);
        List<ResolveInfo> localList = context.getPackageManager().queryIntentServices(i, 0);
        String myPkgName = context.getPackageName();
        String pkgName = "";
        long pkgPriority = 0;
        for (ResolveInfo info : localList) {
            if (!info.serviceInfo.exported) {
                continue;
            }
            String pkg = info.serviceInfo.packageName;
            if (!info.serviceInfo.exported) {
                continue;
            }
            Context context1;
            try {
                context1 = context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
            } catch (NameNotFoundException e) {
                continue;
            }
            String spName = new StringBuilder().append(pkg).append(".push_sync").toString();
            SharedPreferences sp = context1.getSharedPreferences(spName, Context.MODE_WORLD_READABLE);
            long priority = sp.getLong("priority2", 0L);
            if (priority > pkgPriority || (myPkgName.equals(pkg) && priority >= pkgPriority)) {
                pkgPriority = priority;
                pkgName = pkg;
            }
        }
        return pkgName;
    }

    /**
     * @param 给定的服务名
     * @描述 判断指定的服务是否在运行，如果在运行，则返回true
     */
    public static boolean isAppointedServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }

        }
        return false;
    }
    
    public static Bundle getBundle(String key,String value)
    {
    	Bundle b=new Bundle();
    	b.putString(key, value);
    	return b;
    }
}
