package com.librariy.widget.camera;

import java.util.ArrayList;
import java.util.List;

import com.librariy.utils.Log;
import com.librariy.utils.ThreadPoolTask;
import com.librariy.utils.UIHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask.Status;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import cn.sharesdk.R;

public class ImgChoiceAdapter extends BaseAdapter {
    private static final String TAG = ImgChoiceAdapter.class.getSimpleName();

    public static interface SelectListChangeListener {
        public void onSelectListChanged(int newSelectedCnt);
    }
    private SelectListChangeListener mSelectListChangeListener = null;
    protected Context mContext;
    private int mMaxCheckedCnt = 1;
    protected List<CheckableFile> mData = new ArrayList<CheckableFile>();

    public ImgChoiceAdapter(Context mContext, int mMaxCheckedCnt) {
        this.mContext = mContext;
        this.mMaxCheckedCnt = mMaxCheckedCnt;
        mData.clear();
        mData.add(new CheckableFile(CheckableFile.CAMERA, false));
    }

    public void setMaxCheckedCnt(int mMaxCheckedCnt) {
        this.mMaxCheckedCnt = mMaxCheckedCnt;
    }

    public int getMaxCheckedCnt() {
        return this.mMaxCheckedCnt;
    }

    public void setSelectListChangeListener(@NonNull SelectListChangeListener mSelectListChangeListener) {
        this.mSelectListChangeListener = mSelectListChangeListener;
    }

    public void setData(List<String> mImgList) {
        mData.clear();
        mData.add(new CheckableFile(CheckableFile.CAMERA, false));
        for (String path : mImgList) {
            mData.add(new CheckableFile(path, false));
        }
        this.notifyDataSetChanged();
    }

    public void addData(String imagePath, boolean isChecked) {
        int realCheckedCnt = getSelectImages().size();
        if (isChecked && realCheckedCnt >= mMaxCheckedCnt) {
            mData.add(1, new CheckableFile(imagePath, false));
        } else {
            mData.add(1, new CheckableFile(imagePath, isChecked));
            realCheckedCnt++;
        }
        this.notifyDataSetChanged();
        if (mSelectListChangeListener != null) {
            mSelectListChangeListener.onSelectListChanged(realCheckedCnt);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CheckableFile getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CheckableCell getView(int position, View convertView, ViewGroup parent) {
        CheckableCell img;
        if (convertView instanceof ImageView) {
            img = (CheckableCell) convertView;
        } else {
            img = new CheckableCell(mContext);
        }
        CheckableFile mCellBean = getItem(position);
        img.setUiStyle(mMaxCheckedCnt > 1 ? CheckableCell.UiStyle.Checkbox : CheckableCell.UiStyle.None);
        img.setCellBean(mCellBean);
        return img;
    }

    public void toggleCheckedStatus(int position) {
        ArrayList<String> checkList = getSelectImages();
        int realCheckedCnt = checkList.size();
        if (position < 0 || position > mData.size() - 1) {
            Log.e(TAG, "position参数不合法!");
            // Toast.makeText(mContext, "非法选择调用", Toast.LENGTH_SHORT).show();
        } else if (mMaxCheckedCnt == 1) {
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).isChecked = (i == position);
            }
            realCheckedCnt = 1;
        } else {
            CheckableFile mCheckableFile = mData.get(position);
            if (mCheckableFile.isChecked) {
                mCheckableFile.isChecked = false;
                realCheckedCnt = realCheckedCnt - 1;
            } else {
                if (realCheckedCnt < mMaxCheckedCnt) {
                    mCheckableFile.isChecked = true;
                    realCheckedCnt = realCheckedCnt + 1;
                } else {
                    UIHelper.showToast(mContext, "您最多只能选择张" + realCheckedCnt + "图片");
                }
            }
        }
        super.notifyDataSetChanged();
        if (mSelectListChangeListener != null) {
            mSelectListChangeListener.onSelectListChanged(realCheckedCnt);
        }
    }

    public ArrayList<String> getSelectImages() {
        ArrayList<String> retList = new ArrayList<String>();
        for (CheckableFile file : mData) {
            if (!file.isChecked)
                continue;
            retList.add(file.imageFile);
        }
        return retList;
    }

    /**
     * 内部类，图片路径，选择状态实体
     * */
    public static class CheckableFile {
        public static final String CAMERA = "-1";
        public String imageFile;
        public boolean isChecked = true;

        public CheckableFile(String imageFile, boolean isChecked) {
            this.imageFile = imageFile;
            this.isChecked = isChecked;
        }
    }

    /**
     * 内部类，表格单元格显示视图
     * */
    public static class CheckableCell extends ImageView {
        public enum UiStyle {
            Checkbox, RadioButton, None
        };

        private float DP = 1.0f;
        private ThreadPoolTask<Void, Void, Bitmap> task = null;
        private CheckableFile mCheckableFile = null;
        private GradientDrawable mDark = null;
        private Bitmap checkOn, checkOff;

        public CheckableCell(Context context) {
            super(context);
            this.DP = getContext().getResources().getDisplayMetrics().density;
            // super.setPadding((int)(5*DP), (int)(5*DP), (int)(5*DP),
            // (int)(5*DP));
            super.setBackgroundColor(ZorCameraConfig.getColor(getContext(), R.color.zor_camera_gird_cell_bg));
            // super.setBackgroundResource(android.R.drawable.list_selector_background);
            super.setScaleType(ScaleType.FIT_XY);
            mDark = new GradientDrawable();
            // mDark.setStroke((int)(4*DP), Color.argb(0xFF, 0xC3, 0x76, 0x13));
            mDark.setShape(GradientDrawable.RECTANGLE);
            mDark.setColor(ZorCameraConfig.getColor(getContext(), R.color.zor_camera_gird_cell_dark));
        }

        public void setUiStyle(UiStyle mUiStyle) {
            if (mUiStyle == UiStyle.Checkbox || mUiStyle == UiStyle.RadioButton) {
                checkOn = BitmapFactory.decodeResource(getResources(), R.drawable.ic_checkbox_on);
                checkOff = BitmapFactory.decodeResource(getResources(), R.drawable.ic_checkbox_off);
            } else {
                checkOn = BitmapFactory.decodeResource(getResources(), R.drawable.ic_checkbox_on);
                checkOff = BitmapFactory.decodeResource(getResources(), R.drawable.ic_checkbox_off);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mCheckableFile == null || CheckableFile.CAMERA.equals(mCheckableFile.imageFile))
                return;
            if (isChecked()) {
                mDark.setBounds(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
                mDark.draw(canvas);
            }
            Bitmap b = isChecked() ? checkOn : checkOff;
            if (b != null) {
                canvas.drawBitmap(b, getMeasuredWidth() - getPaddingRight() - b.getWidth() - 5 * DP, getPaddingTop() + 5 * DP, null);
            }
        }

        private boolean isChecked() {
            if (mCheckableFile == null || CheckableFile.CAMERA.equals(mCheckableFile.imageFile))
                return false;
            return mCheckableFile.isChecked;
        }

        public void setCellBean(CheckableFile mCheckableFile) {
            if (mCheckableFile != null && mCheckableFile.imageFile != null && this.mCheckableFile != null && mCheckableFile.imageFile.equals(this.mCheckableFile.imageFile)) {
                this.mCheckableFile = mCheckableFile;
            } else {
                this.mCheckableFile = mCheckableFile;
                if (CheckableFile.CAMERA.equals(mCheckableFile.imageFile)) {
                    this.setImageResource(ZorCameraConfig.Rsid_Camera, Gravity.CENTER);
                } else {
                    this.loadImage();
                }
            }
        }
        /**
         * @param resId
         *            the Bitmap Resource Id
         * @param gravity
         *            the {@link android.view.Gravity}
         */
        public void setImageResource(int resId, int gravity) {
            Drawable d = getResources().getDrawable(resId);
            if (d instanceof BitmapDrawable) {
                BitmapDrawable tempD = (BitmapDrawable) d;
                tempD.setGravity(gravity);
            }
            super.setImageDrawable(d);
        }

        public void loadImage() {
            this.setImageResource(ZorCameraConfig.Rsid_Gallery, Gravity.CENTER);
            if (task != null && task.getStatus() != Status.FINISHED) {
                task.cancel(true);
            }
            task = new ThreadPoolTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    if (super.isCancelled())
                        return null;
                    return getBitMap(mCheckableFile.imageFile, 100, 100);
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result == null || super.isCancelled()) {
                        return;
                    }
                    setImageBitmap(result);
                }
            };
            task.executeOnThreadPool();
        }

        private Bitmap getBitMap(String path, int width, int height) {
            try {
                Log.w(TAG, "Start to get BitMap[" + Thread.currentThread() + "]: " + path);
                if (Thread.interrupted())
                    throw new Exception("getBitMap(" + path + ")，Thread interrupted！");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                if (Thread.interrupted())
                    throw new Exception("getBitMap(" + path + ")，Thread interrupted！");
                options.inJustDecodeBounds = false;
                int inSampleSizeWidth = (int) (options.outWidth / (float) width);
                int inSampleSizeHeight = (int) (options.outHeight / (float) height);
                options.inSampleSize = (inSampleSizeWidth > inSampleSizeHeight) ? inSampleSizeWidth : inSampleSizeHeight;
                if (options.inSampleSize < 1) {
                    options.inSampleSize = 1;
                }
                if (Thread.interrupted())
                    throw new Exception("getBitMap(" + path + ")，Thread interrupted！");
                return BitmapFactory.decodeFile(path, options);
            } catch (Exception e) {
                Log.w(TAG, "Error:" + e.getLocalizedMessage());
                return null;
            }
        }
    }

}