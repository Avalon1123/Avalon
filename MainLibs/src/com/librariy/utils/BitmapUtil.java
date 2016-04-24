package com.librariy.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Display;

public class BitmapUtil {
	public static byte[] decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 1920 * 1080);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight, 400 * 400);
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
					(int) (opts.outWidth * scale),
					(int) (opts.outHeight * scale), true);

			bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bmp2.recycle();

			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		// return baos.toByteArray();
	}
	/**
	 * Bitmap转Bytes
	 * @param bm
	 * @return
	 */
	 public static byte[] Bitmap2Bytes(Bitmap bm) {
		          ByteArrayOutputStream baos = new ByteArrayOutputStream();
		          bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		          return baos.toByteArray();
		     }
	private static double getScaling(int src, int des) {
		/**
		 * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
		 */
		double scale = Math.sqrt((double) des / (double) src);
		return scale;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 把图片按照屏幕宽度缩放，使图片宽度缩放到屏幕宽度
	 * 
	 * @param activity
	 *            上下文activity
	 * @param imageView
	 *            设置图片的ImageView
	 * @param id
	 *            需要缩放的drawable的id
	 */
	public static Bitmap setScaleImage(Activity activity, Bitmap bm) {

		// 得到当前屏幕分辨率（得到屏幕宽，按宽与图片的比例进行缩放，目的是使图片宽占满屏幕）
		Display display = activity.getWindowManager().getDefaultDisplay();
		int srcWidth = display.getWidth();
		int srcHeight = display.getHeight();
		// 缩放比例
		float scaleWidth = (float) srcWidth / bm.getWidth();
		float scaleHeight = (float) srcHeight / bm.getHeight();
		// 缩放动作
		Matrix matrix = new Matrix();
		if (scaleWidth > scaleHeight) {
			matrix.postScale(scaleWidth, scaleWidth);
		} else {
			matrix.postScale(scaleHeight, scaleHeight);
		}
		// 重构图片
		// 1.原始bitmap；2.3.第一个像素的x/y坐标；4.5.图片的原始宽高；
		// 6.matrix矩阵对象，包含了缩放比；7.如果真的需要过滤，仅适用于如果矩阵包含的不仅仅是转化。
		Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, true);
		return bitmap;
	}
}
