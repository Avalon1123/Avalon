package com.librariy.widget;

import java.util.ArrayList;
import java.util.List;

import com.librariy.utils.UIHelper;
import com.librariy.widget.camera.AblumChoiceView;
import com.librariy.widget.camera.ImgChoiceAdapter;
import com.librariy.widget.camera.ZorCameraConfig;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.R;

/**
 * <pre>
  new ZorImgPicker(Context).setImageConfig(800, 600, true).setPickListener(new ZorImgPicker.PickListener() {{
        public void onImgPicked(List<String> images) {
            if(images.isEmpty()) return;
            String path=images.get(0);
            imgIDCardBack.setImageBitmap(BitmapFactory.decodeFile(path));
            imgIDCardBack.setTag(path);
        }
    }).showPicker(1);
 * </pre>
 */
public class ZorImgPicker extends Dialog implements View.OnClickListener {
    private static final String TAG = ZorImgPicker.class.getSimpleName();
    private PickListener mPickListener;

    private AblumChoiceView mAblumsView;
    private ImgChoiceAdapter mChoiceAdapter;
    private Bundle mConfig = new Bundle();

    public ZorImgPicker(Context mContext) {
        super(mContext, android.R.style.Theme_Black_NoTitleBar);
        ZorCameraConfig.initialize(mContext);
        initialize();
    }

    protected void initialize() {
        super.setContentView(R.layout.widget_img_picker);
        // 1.标题栏
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        mAblumsView = ((AblumChoiceView) findViewById(R.id.ablums));
        mAblumsView.setOnAblumListener(new AblumChoiceView.OnAblumListener() {
            @Override
            public void onAblumChanged(int ablumId) {
                // 用户选择不同的相册，重新载入该相册中的所有照片
                reloadImages();
            }
        });
        // 2.图片九宫格
        mChoiceAdapter = new ImgChoiceAdapter(getContext(), 1);
        mChoiceAdapter.setSelectListChangeListener(new ImgChoiceAdapter.SelectListChangeListener() {
            @Override
            public void onSelectListChanged(int newSelectedCnt) {
                ((TextView) findViewById(R.id.ok)).setText(String.format("确定(%s/%s)", newSelectedCnt, mChoiceAdapter.getMaxCheckedCnt()));
            }
        });
        GridView mGridView = (GridView) findViewById(R.id.mGridView);
        mGridView.setAdapter(mChoiceAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    showLocalCamera();
                } else {
                    mChoiceAdapter.toggleCheckedStatus(position);
                }
            }
        });
    }

    /**
     * 已失效，请使用 {@link ZorImgPicker#showPicker(int)} 代替
     */
    @Override
    @Deprecated
    public void show() {
        // super.show();
    }

    public void showPicker(int mMaxCheckedCnt) {
        mChoiceAdapter.setMaxCheckedCnt(mMaxCheckedCnt);
        super.show();
        reloadImages();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok) {
            finishSelected(mChoiceAdapter.getSelectImages());
        } else if (v.getId() == R.id.back) {
            if (mPickListener != null) {
                mPickListener.onImgPicked(new ArrayList<String>(0));
            }
            dismiss();
        }
    }
    @Override
    public void dismiss() {
        UIHelper.dismissToast();
        super.dismiss();
    }

    /**
     * 设置监听器，通知调用者所选取的图片
     */
    public ZorImgPicker setPickListener(PickListener mPickListener) {
        this.mPickListener = mPickListener;
        return this;
    }

    /**
     * 设置返回图片的宽和高，及（宽高比与原图不匹配时）是否自动填充到设定的大小
     */
    public ZorImgPicker setImageConfig(int mConfigWidth, int mConfigHeight, boolean mAutoFill) {
        mConfig.putInt("ConfigWidth", mConfigWidth);
        mConfig.putInt("ConfigHeight", mConfigHeight);
        mConfig.putBoolean("AutoFill", mAutoFill);
        return this;
    }

    /**
     * 内部类：监听器，通知调用者所选取的图片
     */
    public interface PickListener {
        public void onImgPicked(List<String> images);
    }

    /**
     * 重新读取相册中的图片
     */
    private void reloadImages() {
        String where = null;
        String ablumName = mAblumsView.getText().toString();
        if (!AblumChoiceView.defaultAblumName.endsWith(ablumName)) {
            where = Images.Media.BUCKET_DISPLAY_NAME + "='" + ablumName + "'";
        }
        Cursor mCursor = MediaStore.Images.Media.query(getContext().getContentResolver(), Images.Media.EXTERNAL_CONTENT_URI, null, where, Images.Media.DATE_ADDED + " DESC");
        List<String> mImgList = new ArrayList<String>();
        if (mCursor == null) {
            return;
        }
        while (mCursor.moveToNext()) {
            mImgList.add(mCursor.getString(mCursor.getColumnIndex(ImageColumns.DATA)));
        }
        mCursor.close();
        mChoiceAdapter.setData(mImgList);
        ((TextView) findViewById(R.id.ok)).setText(String.format("完成(%s/%s)", 0, mChoiceAdapter.getMaxCheckedCnt()));
    }

    /**
     * 打开相机拍摄照片
     */
    private void showLocalCamera() {
        new ZorCamera(getContext()).setCameraListener(new ZorCamera.CameraListener() {
            @Override
            public void onPicSaved(String filePath) {
                mChoiceAdapter.addData(filePath, true);
            }
        }).show();
    }

    /**
     * 选择完成，处理图片并返回
     */
    private void finishSelected(final ArrayList<String> images) {
        if (images.isEmpty()) {
            Toast.makeText(getContext(), "请至少请选择一张图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPickListener == null)
            return;
        AsyncTask<Void, Void, ArrayList<String>> task = new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                int mConfigWidth = mConfig.getInt("ConfigWidth", 800);
                int mConfigHeight = mConfig.getInt("ConfigHeight", 600);
                boolean mAutoFill = mConfig.getBoolean("AutoFill", true);
                for (int i = 0; i < images.size(); i++) {
                    images.set(i, ZorCameraConfig.saveToCacheDir(images.get(i), mConfigWidth, mConfigHeight, mAutoFill));
                }
                return images;
            }

            @Override
            protected void onPostExecute(ArrayList<String> result) {
                super.onPostExecute(result);
                mPickListener.onImgPicked(result);
                dismiss();
            }
        };
        task.execute();
    }
}
