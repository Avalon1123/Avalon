package com.librariy.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class BitmapTool {
	/** bitmap转byte */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/** byte转bitmap */
	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * @描述 bitmap缩放
	 * @param bitmap
	 *            bitmap对象
	 * @param width
	 *            要缩放的宽度
	 * @param height
	 *            要缩放的高度
	 * 
	 * */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/** drawbale转bitmap */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
	public static Bitmap getimage(String srcPath,float width,float height,int size_kb) {  
	    if(width==0||height==0||size_kb<1)
	        return null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > width) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / width);  
        } else if (w < h && h > height) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / height);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap,size_kb);//压缩好比例大小后再进行质量压缩  
    } 
	public static Bitmap compressImage(Bitmap image,int size) {  
	    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>size) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
	/** 获得圆角图片 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/** 获得带倒影的图片 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
	/** 
     * bitmap转为base64 
     * @param bitmap 
     * @return 
     */  
    public static String bitmapToBase64(Bitmap bitmap) {  
  
        String result = null;  
        ByteArrayOutputStream baos = null;  
        try {  
            if (bitmap != null) {  
                baos = new ByteArrayOutputStream();  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
  
                baos.flush();  
                baos.close();  
  
                byte[] bitmapBytes = baos.toByteArray();  
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (baos != null) {  
                    baos.flush();  
                    baos.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  
  
    /** 
     * base64转为bitmap 
     * @param base64Data 
     * @return 
     */  
    public static Bitmap base64ToBitmap(String base64Data) {  
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
    }  
    
    
    public static Bitmap compressImageFromFile(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
  
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = 800f;//  
        float ww = 600f;//  
        int be = 1;  
        if (w > h && w > ww) {  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置采样率  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
        newOpts.inPurgeable = true;// 同时设置才会有效  
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收  
          
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
//      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩  
                                    //其实是无效的,大家尽管尝试  
        return bitmap;  
    }
    /**
     * 压缩图片 变文件
     * @param src
     * @return
     */
    public static String  saveBitmap2file(String src){  
        CompressFormat format= Bitmap.CompressFormat.JPEG; 
        UUID uuid = UUID.randomUUID();
       final String name=Utils.HOUSE_CACHE_FILE_PATH + uuid+".jpg";
       int quality = 100;  
       OutputStream stream = null;  
       try {  
    	   
               stream = new FileOutputStream(name);  
       } catch (FileNotFoundException e) {  
               // TODO Auto-generated catch block  
               e.printStackTrace();  
       }  
       Bitmap bmp=compressImageFromFile(src);
       bmp.compress(format, quality, stream); 
       return  name;
       }  
}
