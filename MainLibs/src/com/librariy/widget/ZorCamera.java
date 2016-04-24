package com.librariy.widget;

import java.io.File;
import java.io.FileOutputStream;

import com.librariy.utils.Log;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ZorCamera extends Dialog implements View.OnClickListener {
    private static final String TAG = ZorCamera.class.getSimpleName();
    private CameraListener mListener;
    private SurfaceView surfaceView;
    private Camera camera;
    private SensorMonitor mSensorMonitor;

    public ZorCamera(Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        float DPI = getContext().getResources().getDisplayMetrics().density;
        LinearLayout mRootView = new LinearLayout(getContext());
        mRootView.setOrientation(LinearLayout.VERTICAL);
        super.setContentView(mRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.surfaceView = new SurfaceView(getContext());
        mRootView.addView(surfaceView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        // 4.BottomPanel
        LinearLayout mBottomPanel = new LinearLayout(getContext());
        mBottomPanel.setBackgroundColor(Color.BLACK);
        mBottomPanel.setOrientation(LinearLayout.HORIZONTAL);
        ImageView btn1 = new ImageView(getContext());
        btn1.setId(android.R.id.button1);
        btn1.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        btn1.setPadding((int) (20 * DPI), (int) (20 * DPI), (int) (20 * DPI), (int) (20 * DPI));
        btn1.setOnClickListener(this);
        mBottomPanel.addView(btn1, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        ImageView btn2 = new ImageView(getContext());
        btn2.setId(android.R.id.button2);
        btn2.setImageResource(android.R.drawable.ic_menu_camera);
        btn2.setPadding((int) (20 * DPI), (int) (20 * DPI), (int) (20 * DPI), (int) (20 * DPI));
        btn2.setOnClickListener(this);
        mBottomPanel.addView(btn2, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        mRootView.addView(mBottomPanel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mSensorMonitor = new SensorMonitor(this.getContext());
    }

    public ZorCamera setCameraListener(CameraListener mListener) {
        this.mListener = mListener;
        return this;
    }

    @Override
    public void show() {
        super.show();
        surfaceView.getHolder().setFixedSize(surfaceView.getMeasuredWidth(), surfaceView.getMeasuredHeight());
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new CameraCallback());
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == android.R.id.button1) {
            this.dismiss();
        } else if (v.getId() == android.R.id.button2) {
            this.autoFocusAndTakePicture();
            // this.takePicture();
        }
    }

    @SuppressWarnings("deprecation")
    private void autoFocusAndTakePicture() {
        if (camera == null) {
            Toast.makeText(getContext(), "相机正在初始化，请稍后!", Toast.LENGTH_LONG).show();
            return;
        }
        mSensorMonitor.tryPriorityLock();
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    takePicture();
                    mSensorMonitor.releasePriorityLock();
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void takePicture() {
        if (camera == null) {
            Toast.makeText(getContext(), "相机正在初始化，请稍后!", Toast.LENGTH_LONG).show();
            return;
        }
        Parameters p = camera.getParameters();
        p.setRotation(mSensorMonitor.getRotation());
        camera.setParameters(p);
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, final Camera camera) {
                try {
                    File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "xfd");
                    if (!localFile.isDirectory() && !localFile.mkdirs()) {
                        Toast.makeText(getContext(), "无法创建相册：" + localFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        camera.startPreview();
                        return;
                    }
                    localFile = new File(localFile, System.currentTimeMillis() + ".jpeg");
                    FileOutputStream out = new FileOutputStream(localFile);
                    out.write(data);
                    if (mListener == null) {
                        return;
                    }
                    // getContext().sendBroadcast(new
                    // Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    // Uri.fromFile(localFile)));
                    MediaScannerConnection.scanFile(getContext(), new String[] { localFile.getAbsolutePath() }, new String[] { "image/jpeg" }, null);
                    // Toast.makeText(getContext(), "照片已保存到：" +
                    // localFile+", Uri="+Uri.fromFile(localFile),
                    // Toast.LENGTH_LONG).show();
                    mListener.onPicSaved(localFile.getAbsolutePath());
                    camera.startPreview();
                    dismiss();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "保存文件失败：" + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 内部类：监听器，返回调用者所拍摄的图片
     * */
    public interface CameraListener {
        public void onPicSaved(String filePath);
    }

    /**
     * 内部类：方向感应监听器，监听手机放置方向
     * */
    public class SensorMonitor extends OrientationEventListener implements SensorEventListener {
        private float[] values = new float[] { 0f, 0f, 0f };
        private boolean mFocusLock = false;
        private int mOrientation = 0;
        private Context mContext;

        public SensorMonitor(Context mContext) {
            super(mContext);
            this.mContext = mContext;
        }

        @Override
        public void onOrientationChanged(int orientation) {
            //Log.d(TAG, "onOrientationChanged, orientation=" + orientation);
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                return;
            }
            if (Math.abs(mOrientation - orientation) > 2) {
                onDeviceStatusChanged();
            }
            this.mOrientation = orientation;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Math.abs(event.values[0] - values[0]) + Math.abs(event.values[1] - values[1]) + Math.abs(event.values[2] - values[2]) > 1.0) {
                onDeviceStatusChanged();
            }
            values[0] = event.values[0];
            values[1] = event.values[1];
            values[2] = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void tryPriorityLock() {
            if (mFocusLock&&camera!=null) {
                camera.cancelAutoFocus();
            }
            mFocusLock = true;
        }

        public void releasePriorityLock() {
            if (mFocusLock&&camera!=null) {
                camera.cancelAutoFocus();
            }
            mFocusLock = false;
        }

        public void onDeviceStatusChanged() {
            if (mFocusLock||camera==null) {
                return;
            }
            Log.e(TAG, "onDeviceStatusChanged! ;Start Auto Focus！");
            mFocusLock = true;
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        mFocusLock = false;
                    }
                }
            });
        }

        public void startMonitor() {
            super.enable();
            SensorManager mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometer != null) {
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        public void stopMonitor() {
            super.disable();
            SensorManager mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            mSensorManager.unregisterListener(this);
        }

        public int getRotation() {
            if ((mOrientation >= 45) && (mOrientation < 135)) {
                return 180;
            } else if ((mOrientation >= 135) && (mOrientation < 225)) {
                return 270;
            } else if ((mOrientation >= 225) && (mOrientation < 315)) {
                return 0;
            } else {
                return 90;
            }
        }
    }

    /**
     * 内部类：相机及图像预览初始化
     * */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private class CameraCallback implements SurfaceHolder.Callback {
        @SuppressWarnings("deprecation")
        private void initCameraParameters() {
            if(camera==null) return;
            int rotation = getWindow().getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == Surface.ROTATION_0) {
                Log.e(TAG, "initCameraParameters, rotation=Surface.ROTATION_0, Orientation=90");
                camera.setDisplayOrientation(90);
            } else if (rotation == Surface.ROTATION_90) {
                Log.e(TAG, "initCameraParameters(), Rotation=Surface.ROTATION_90, Orientation=0");
                camera.setDisplayOrientation(0);
            } else if (rotation == Surface.ROTATION_180) {
                Log.e(TAG, "initCameraParameters, rotation=Surface.ROTATION_180, Orientation=270");
                camera.setDisplayOrientation(270);
            } else if (rotation == Surface.ROTATION_270) {
                Log.e(TAG, "initCameraParameters, rotation=Surface.ROTATION_270, Orientation=180");
                camera.setDisplayOrientation(180);
            }
            // Parameters p=camera.getParameters();
            // p.setPreviewSize(800, 600);
            // p.setPictureSize(800, 600);
            // camera.setParameters(p);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                if(camera==null) return;
                camera.stopPreview();
                initCameraParameters();
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = null;
                try {
                    camera = Camera.open();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "无法打开摄像头", Toast.LENGTH_LONG).show();
                    dismiss();
                    return;
                }
                if (camera == null) {
                    Toast.makeText(getContext(), "摄像头为空", Toast.LENGTH_LONG).show();
                    dismiss();
                    return;
                }
                initCameraParameters();
                camera.setPreviewDisplay(holder);
                camera.startPreview();
                mSensorMonitor.startMonitor();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "摄像头初始化失败！", Toast.LENGTH_LONG).show();
                dismiss();
            }
        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                mSensorMonitor.stopMonitor();
                if(camera==null) return;
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}