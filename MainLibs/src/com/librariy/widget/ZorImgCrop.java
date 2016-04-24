package com.librariy.widget;

import java.util.List;

import com.librariy.view.CropImageView;
import com.librariy.widget.camera.ZorCameraConfig;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import cn.sharesdk.R;

/**
 * <pre>
 * new ZorImgCrop(this).showCropDialog(856, 540, new ZorImgCrop.CropListener() {
 *     &#64;Override
 *     public void onImgCroped(String path) {
 *         imgIDCardPositive.setImageBitmap(BitmapFactory.decodeFile(path));
 *         imgIDCardPositive.setTag(path);
 *     }
 * });
 * </pre>
 */
public class ZorImgCrop extends Dialog implements View.OnClickListener {
    private static final String TAG = ZorImgCrop.class.getSimpleName();
    private CropListener mCropListener;
    private CropImageView mCropImageView;

    public ZorImgCrop(Context mContext) {
        super(mContext, android.R.style.Theme_Black_NoTitleBar);
        ZorCameraConfig.initialize(mContext);
        initialize();
    }

    protected void initialize() {
        super.setContentView(R.layout.widget_img_crop);
        // 1.标题栏
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.clockwise).setOnClickListener(this);
        findViewById(R.id.anticlockwise).setOnClickListener(this);
        this.mCropImageView = (CropImageView) findViewById(R.id.mCropImageView);
    }

    /**
     * 已失效，请使用 {@link #showCropDialog(int, int, CropListener)} 代替
     */
    @Override
    @Deprecated
    public void show() {
        // super.show();
    }

    public void showCropDialog(final int cropWidth, final int cropHeight, CropListener mCropListener) {
        super.show();
        this.mCropListener = mCropListener;
        ZorImgPicker picker = new ZorImgPicker(getContext());
        picker.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });
        picker.setImageConfig(800, 800, false).setPickListener(new ZorImgPicker.PickListener() {
            @Override
            public void onImgPicked(List<String> images) {
                if (images.isEmpty()) {
                    dismiss();
                    return;
                }
                mCropImageView.setImage(images.get(0), cropWidth, cropHeight);
            }
        }).showPicker(1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok) {
            if (mCropListener != null) {
                String path = ZorCameraConfig.saveToCacheDir(mCropImageView.getCropImage());
                mCropListener.onImgCroped(path);
            }
            this.dismiss();
        } else if (v.getId() == R.id.back) {
            this.dismiss();
        }else if (v.getId() == R.id.back) {
            this.dismiss();
        }else if (v.getId() == R.id.anticlockwise) {
            mCropImageView.postRotate(-90);
        }else if (v.getId() == R.id.clockwise) {
            mCropImageView.postRotate(90);
        }
    }

    /**
     * 设置监听器，通知调用者所选取的图片
     */
    public ZorImgCrop setCropListener(CropListener mCropListener) {
        this.mCropListener = mCropListener;
        return this;
    }

    /**
     * 内部类：监听器，通知调用者所选取的图片
     */
    public interface CropListener {
        public void onImgCroped(String path);
    }

}
