package com.librariy.view;

import com.librariy.utils.Log;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
    private static final String TAG = FlowLayout.class.getSimpleName();
    private int mLines = Integer.MAX_VALUE;

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context, attrs);
    }

    public FlowLayout(Context context) {
        super(context);
        this.initialize(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, new int[] { android.R.attr.lines });
        this.mLines = mTypedArray.getInt(0, Integer.MAX_VALUE);
        mTypedArray.recycle();
        Log.d(TAG, "initialize(), mLines=" + mLines);
        // Gravity.applyDisplay(gravity, display, inoutObj);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        // super.generateLayoutParams(attrs)
        return new ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        // return super.generateDefaultLayoutParams();
        return new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        // return super.generateLayoutParams(p);
        return new ViewGroup.MarginLayoutParams(p);
    }

    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewGroup.MarginLayoutParams;
    }

    public void addView(View child, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams p = new ViewGroup.MarginLayoutParams(width, height);
        p.leftMargin = leftMargin;
        p.topMargin = topMargin;
        p.rightMargin = rightMargin;
        p.bottomMargin = bottomMargin;
        super.addView(child, p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mChildCount = getChildCount();
        Log.d(TAG, "onMeasure(),mChildCount=" + mChildCount + ", widthMeasureSpec=" + MeasureSpec.toString(widthMeasureSpec) + ", heightMeasureSpec=" + MeasureSpec.toString(heightMeasureSpec));
        if (mChildCount < 1) {
            super.setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY));
            return;
        }
        // 控件希望得到的高度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.UNSPECIFIED) {
            // 1.宽度有固定值（可能是“精确值/最大数值”，不需要计算）
            int pWidth = MeasureSpec.getSize(widthMeasureSpec);
            int pHeight = MeasureSpec.getSize(heightMeasureSpec);
            int right = pWidth - getPaddingRight();
            int x = getPaddingLeft(), y = getPaddingTop();
            int rowHeight = 0;
            int currLine = 1;
            for (int i = 0; i < mChildCount; i++) {
                final View child = getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                child.measure(p.width, p.height);
                int childW = child.getMeasuredWidth(), childH = child.getMeasuredHeight();
                if (rowHeight < (childH + p.topMargin + p.bottomMargin)) {
                    rowHeight = childH + p.topMargin + p.bottomMargin;
                }
                if ((x + p.leftMargin + childW + p.rightMargin) > right) {
                    currLine++;
                    if (currLine > mLines) {
                        break;
                    }
                    y += rowHeight;
                    x = getPaddingLeft();
                    rowHeight = childH + p.topMargin + p.bottomMargin;
                }
                x = x + p.leftMargin + childW + p.rightMargin;
            }
            y = y + rowHeight + getPaddingBottom();
            if (heightMode == MeasureSpec.UNSPECIFIED) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(y, MeasureSpec.EXACTLY);
            } else if (heightMode == MeasureSpec.AT_MOST) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(y < pHeight ? y : pHeight, MeasureSpec.EXACTLY);
            }
            super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            // 2.宽度自适应,需要根据子View计算控件宽度
            int totalWidth = 0;
            for (int i = 0; i < mChildCount; i++) {
                final View child = getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                child.measure(p.width, p.height);
                totalWidth = totalWidth + (p.leftMargin + child.getMeasuredWidth() + p.rightMargin);
            }
            super.setMeasuredDimension(MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), widthMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mChildCount = getChildCount();
        if (mChildCount < 1)
            return;
        int right = getMeasuredWidth() - getPaddingRight();
        int x = getPaddingLeft(), y = getPaddingTop();
        int rowHeight = 0;
        int currLine = 1;
        for (int i = 0; i < mChildCount; i++) {
            final View child = getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childW = child.getMeasuredWidth(), childH = child.getMeasuredHeight();
            if (rowHeight < (childH + p.topMargin + p.bottomMargin)) {
                rowHeight = childH + p.topMargin + p.bottomMargin;
            }
            if ((x + childW + p.leftMargin + p.rightMargin) > right) {
                currLine++;
                if (currLine > mLines) {
                    break;
                }
                y += rowHeight;
                x = getPaddingLeft();
                rowHeight = childH + p.topMargin + p.bottomMargin;
            }
            child.layout(x + p.leftMargin, y + p.topMargin, x + p.leftMargin + childW, y + p.topMargin + childH);
            x = x + p.leftMargin + childW + p.rightMargin;
        }
    }
}
