package com.librariy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.librariy.utils.Log;

public class FixedColumnsLayout extends ViewGroup {
    protected static final String TAG = FixedColumnsLayout.class.getSimpleName();
    protected int numColumns = 1;
    protected int stretchMode = GridView.NO_STRETCH;
    protected int horizontalSpacing = 0;
    protected int verticalSpacing = 0;
    protected int mGravity = Gravity.CENTER;
    protected int[] mRowHeight = null;
    protected double mAspectRatio = 0;

    public FixedColumnsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context, attrs);
    }

    public FixedColumnsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context, attrs);
    }

    public FixedColumnsLayout(Context context) {
        super(context);
        this.initialize(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, new int[] { android.R.attr.gravity, android.R.attr.horizontalSpacing, android.R.attr.verticalSpacing, android.R.attr.stretchMode, android.R.attr.numColumns });
        this.mGravity = mTypedArray.getInt(0, -1);
        this.horizontalSpacing = mTypedArray.getDimensionPixelSize(1, 0);
        this.verticalSpacing = mTypedArray.getDimensionPixelSize(2, 0);
        this.stretchMode = mTypedArray.getInt(3, GridView.NO_STRETCH);
        this.numColumns = mTypedArray.getInt(4, 1);
        mTypedArray.recycle();
        Log.d(TAG, "initialize(), numColumns=" + numColumns + "; horizontalSpacing=" + horizontalSpacing + "; verticalSpacing=" + verticalSpacing + "; mGravity=" + mGravity + ";stretchMode=" + stretchMode);
    }

    public void setAspectRatio(double mAspectRatio) {
        this.mAspectRatio = mAspectRatio;
        super.requestLayout();
    }
    public double getAspectRatio() {
        return this.mAspectRatio;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.d(TAG, "onMeasure(), widthMeasureSpec=" + MeasureSpec.toString(widthMeasureSpec) + ", heightMeasureSpec=" + MeasureSpec.toString(heightMeasureSpec));
        // 控件希望得到的宽度
        // 判断模式
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED);
        switch (widthMode) {
        // 如果是AT_MOST，不能超过父View的宽度
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((widthSize - getPaddingLeft() - getPaddingRight() - (horizontalSpacing * (numColumns - 1))) / numColumns, (stretchMode == GridView.STRETCH_COLUMN_WIDTH) ? MeasureSpec.EXACTLY : MeasureSpec.AT_MOST);
                break;
            case MeasureSpec.UNSPECIFIED:
                widthSize = -1;
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED);
                break;
        }
        int realHeight = 0;
        int mChildCount = getChildCount();
        int mRows = (mChildCount + numColumns - 1) / numColumns;
        mRowHeight = new int[mRows];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                int index = i * numColumns + j;
                if (index >= mChildCount) {
                    break;
                }
                final View child = getChildAt(index);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                int h = child.getMeasuredHeight();
                if (j == 0) {
                    mRowHeight[i] = h;
                } else if (h > mRowHeight[i]) {
                    mRowHeight[i] = h;
                }
            }
            realHeight += mRowHeight[i];
        }
        int minHeight = (int) (widthSize * mAspectRatio);
        if (minHeight > realHeight) {
            realHeight = minHeight;
        }
        super.setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(realHeight + getPaddingTop() + getPaddingBottom() + (verticalSpacing * (mRows - 1)), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mChildCount = getChildCount();
        if (mChildCount < 1)
            return;
        int mRows = (mChildCount + numColumns - 1) / numColumns;
        int mColumnWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - horizontalSpacing * (numColumns - 1)) / numColumns;
        int x = getPaddingLeft(), y = getPaddingTop();
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                int index = i * numColumns + j;
                if (index >= mChildCount) {
                    break;
                }
                final View child = getChildAt(index);
                int childW = child.getMeasuredWidth(), childH = child.getMeasuredHeight();
                childW = (childW > mColumnWidth) ? mColumnWidth : childW;
                int childX = x, childY = y;
                if ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
                    childY = childY + (mRowHeight[i] - childH);
                } else if ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.CENTER_VERTICAL) {
                    childY = childY + (mRowHeight[i] - childH) / 2;
                } else {
                    // mGravity&Gravity.VERTICAL_GRAVITY_MASK)==Gravity.TOP
                }
                if (stretchMode == GridView.STRETCH_COLUMN_WIDTH) {
                    childW = mColumnWidth;
                } else if ((mGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.RIGHT) {
                    childX = childX + (mColumnWidth - childW);
                } else if ((mGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL) {
                    childX = childX + (mColumnWidth - childW) / 2;
                } else {
                    // mGravity&Gravity.VERTICAL_GRAVITY_MASK)==Gravity.LEFT
                }
                child.layout(childX, childY, childX + childW, childY + childH);
                x = x + mColumnWidth + horizontalSpacing;
            }
            // 移动到父控件最左边，同时顶部移动到上一个区域底部
            y = y + mRowHeight[i] + verticalSpacing;
            x = getPaddingLeft();
        }
    }
}
