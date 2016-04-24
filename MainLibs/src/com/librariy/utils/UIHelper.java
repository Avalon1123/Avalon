/**   
 * Copyright (c) 2013-3-14 by droidsde  
 * 
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject 
 * to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT 
 * WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT 
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @Title: UIHelper.java 
 * @version 
 */
package com.librariy.utils;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.librariy.dialog.LoadingDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;
import cn.sharesdk.R;

/**
 * @author xjin 程序关闭后完全退出Activity
 */
public class UIHelper {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void exitApp() {
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            if (null != activity) {
                activity.finish();
            }
        }
    }

    /**
     * 获取客户端版本号
     * 
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (final NameNotFoundException e) {
            // Application not installed.
        }
        return "";
    }

    /**
     * 获取客户端版本号
     * 
     * @return
     */
    public static String getVersionCode(Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode + "";
        } catch (final NameNotFoundException e) {
            // Application not installed.
        }
        return "";
    }

    private static LoadingDialog mProgressDialog;
    private static Toast mToast;

    public static synchronized void showDialog(Context context, int resId) {
        String message = (context != null && resId > 0) ? context.getString(resId) : "正在加载数据";
        UIHelper.showDialog(context, message);
    }

    public static synchronized void showDialog(Context context, String message) {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        if (context == null)
            return;
        mProgressDialog = new LoadingDialog(context, R.style.CustomProgressDialog);
        mProgressDialog.setMsg(TextUtils.isEmpty(message) ? "" : message);
        mProgressDialog.show();
    }

    public static synchronized void showDialog(Context context) {
        UIHelper.showDialog(context, "正在加载数据");
    }

    public static synchronized void dismissDialog() {
        if (mProgressDialog == null)
            return;
        mProgressDialog.dismiss();
        mProgressDialog=null;
    }

    public static synchronized void showToast(Context context, int resId) {
        UIHelper.showToast(context, context.getString(resId));
    }

    public static synchronized void showToast(Context context, String text) {
        UIHelper.showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static synchronized void dismissToast() {
        if (mToast == null) return;
        mToast.cancel();
        mToast=null;
    }

    public static synchronized void showToast(Context context, String text, int type) {
        if (mToast != null)
            mToast.cancel();
        if (context == null)
            return;
        mToast = Toast.makeText(context, text, type);
        mToast.show();
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * 
     * @paramresId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static String formatPrice(float price) {
        DecimalFormat df = new DecimalFormat("#0.00");
        DecimalFormat dfint = new DecimalFormat("#");
        String str_price = price + "";
        if (price > 100000000) {
            if (price / 100000000 > 100) {
                str_price = dfint.format(price / 100000000.0) + "亿";
            } else {
                str_price = df.format(price / 100000000.0) + "亿";
            }
        } else if (price > 100000) {
            str_price = df.format(price / 100000.0) + "万";
        }
        return str_price;
    }

    public static String formatPrice(long price) {
        DecimalFormat df = new DecimalFormat("#0.00");
        DecimalFormat dfint = new DecimalFormat("#");
        String str_price = price + "";
        if (price > 100000000) {
            if (price / 100000000 > 100) {
                str_price = dfint.format(price / 100000000.0) + "亿";
            } else {
                str_price = df.format(price / 100000000.0) + "亿";
            }
        } else if (price > 100000) {
            str_price = df.format(price / 100000.0) + "万";
        }
        return str_price;
    }

    public static String formatVolumn(long vol) {
        DecimalFormat df = new DecimalFormat("#0.00");
        DecimalFormat dfint = new DecimalFormat("#");
        String str_price = vol + "";
        if (vol > 100000000) {
            if (vol / 100000000 > 100) {
                str_price = dfint.format(vol / 100000000.0) + "亿";
            } else {
                str_price = df.format(vol / 100000000.0) + "亿";
            }
        } else if (vol > 100000) {
            str_price = df.format(vol / 10000.0) + "万";
        } else {
            str_price = dfint.format(vol);
        }
        return str_price;
    }

    public static String formatTradeVolumn(long vol) {
        DecimalFormat df = new DecimalFormat("#0.00");
        DecimalFormat dfint = new DecimalFormat("#");
        String str_price = vol + "";
        if (vol > 100000000) {
            if (vol / 100000000 > 100) {
                str_price = dfint.format(vol / 100000000.0) + "亿";
            } else {
                str_price = df.format(vol / 100000000.0) + "亿";
            }
        } else if (vol > 100000) {
            str_price = dfint.format(vol / 10000.0) + "万";
        } else {
            str_price = dfint.format(vol);
        }
        return str_price;
    }

    public static String formatVolumn(double vol) {
        DecimalFormat df = new DecimalFormat("#0.00");
        DecimalFormat dfint = new DecimalFormat("#");
        String str_price = vol + "";
        if (vol > 100000000) {
            if (vol / 100000000 > 100) {
                str_price = dfint.format(vol / 100000000.0) + "亿";
            } else {
                str_price = df.format(vol / 100000000.0) + "亿";
            }
        } else if (vol > 100000) {
            str_price = df.format(vol / 10000.0) + "万";
        } else {
            str_price = dfint.format(vol);
        }
        return str_price;
    }
}
