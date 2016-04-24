package com.librariy.widget.camera;

import java.io.File;
import java.io.FileOutputStream;

import com.librariy.utils.Log;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import cn.sharesdk.R;

public class ZorCameraConfig {
    public static final String TAG = ZorCameraConfig.class.getSimpleName();
    public static int Rsid_Ablum = R.drawable.ic_gallery;
    public static int Rsid_Gallery = R.drawable.ic_gallery;
    public static int Rsid_Camera = R.drawable.ic_camera;
    public static int Rsid_Check_On = R.drawable.ic_checkbox_on;
    public static int Rsid_Check_Off = R.drawable.ic_checkbox_off;
    private static File mCacheDir = null;

    public static void initialize(Context mContext) {
        mCacheDir = new File(mContext.getExternalCacheDir(), "LocalImagePicker_cache");
        if (!mCacheDir.isDirectory()) {
            mCacheDir.mkdirs();
        }
        long duration = 24 * 3600 * 1000;
        for (File f : mCacheDir.listFiles()) {
            if (System.currentTimeMillis() - f.lastModified() > duration) {
                f.delete();
            }
        }
    }

    public static int getColor(Context mContext, int resId) {
        return mContext.getResources().getColor(resId);
    }

    public static Bitmap getBitmap(String path, float width, float height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        int inSampleSizeWidth = (int) (options.outWidth / (float) width);
        int inSampleSizeHeight = (int) (options.outHeight / (float) height);
        options.inSampleSize = (inSampleSizeWidth > inSampleSizeHeight) ? inSampleSizeWidth : inSampleSizeHeight;
        if (options.inSampleSize < 1) {
            options.inSampleSize = 1;
        }
        return BitmapFactory.decodeFile(path, options);
    }

    public static Drawable getDrawable(Resources mResources, String path, float width, float height) {
        Bitmap mBitmap = ZorCameraConfig.getBitmap(path, width, height);
        if (mBitmap == null) {
            Drawable b = mResources.getDrawable(Rsid_Gallery);
            b.setBounds(0, 0, (int) width, (int) height);
            return b;
        } else {
            BitmapDrawable b = new BitmapDrawable(mResources, mBitmap);
            b.setBounds(0, 0, (int) width, (int) height);
            return b;
        }
    }

    public static String saveToCacheDir(String oldPath, int targetWidth, int targetHeight, boolean mAutoFill) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(oldPath, options);

            float heightRatio = (float) options.outHeight / (float) targetHeight;
            float widthRatio = (float) options.outWidth / (float) targetWidth;
            float targetRatio = widthRatio > heightRatio ? widthRatio : heightRatio;
            options.inSampleSize = Math.round(targetRatio > 1 ? targetRatio : 1);
            RectF imageRectF = new RectF(0, 0, options.outWidth / targetRatio, options.outHeight / targetRatio);
            // imageRectF.offset((targetWidth-imageRectF.width())/2,
            // (targetHeight-imageRectF.height())/2);
            options.inJustDecodeBounds = false;
            Bitmap srcBitmap = BitmapFactory.decodeFile(oldPath, options);
            Bitmap target;
            if (mAutoFill) {
                target = Bitmap.createBitmap(targetWidth, targetHeight, Config.RGB_565);
                imageRectF.offset((targetWidth - imageRectF.width()) / 2, (targetHeight - imageRectF.height()) / 2);
            } else {
                target = Bitmap.createBitmap((int) imageRectF.width(), (int) imageRectF.height(), Config.RGB_565);
            }
            Canvas canvas = new Canvas(target);
            // canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(srcBitmap, null, imageRectF, null);
            srcBitmap.recycle();

            File retFile = new File(mCacheDir, System.currentTimeMillis() + ".jpeg");
            target.compress(Bitmap.CompressFormat.JPEG, 90, new FileOutputStream(retFile));
            // target.compress(Bitmap.CompressFormat.PNG, 90, new
            // FileOutputStream(new
            // File(mCacheDir,System.currentTimeMillis()+".png")));
            target.recycle();
            Log.d(TAG, String.format("图片处理成功：[%s] -> [%s K]#[%s]", oldPath, retFile.length() / 1024, retFile));
            return retFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "图片处理失败：" + oldPath + ", 原因：", e);
            return "";
        }
    }

    public static String saveToCacheDir(Bitmap srcBitmap) {
        try {
            File retFile = new File(mCacheDir, System.currentTimeMillis() + ".jpeg");
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 90, new FileOutputStream(retFile));
            srcBitmap.recycle();
            Log.d(TAG, String.format("图片处理成功：[%s]", retFile));
            return retFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "图片处理失败，原因：", e);
            return "";
        }
    }
}
