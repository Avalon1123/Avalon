package com.librariy.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;

public class IconTextView extends FrameLayout implements Checkable {
    private CheckedTextView mCheckedTextView;

    public IconTextView(Context context) {
        super(context);
        this.initialize(context, null, 0);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context, attrs, 0);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context, attrs, defStyleAttr);
    }

    protected void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        this.setClickable(true);
        this.mCheckedTextView = new CheckedTextView(context, attrs, defStyleAttr);
        this.mCheckedTextView.setPadding(0, 0, 0, 0);
        this.mCheckedTextView.setBackgroundDrawable(null);
        this.mCheckedTextView.setDuplicateParentStateEnabled(true);
        super.addView(mCheckedTextView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckedTextView.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return mCheckedTextView.isChecked();
    }

    @Override
    public void toggle() {
        mCheckedTextView.toggle();
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        mCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        mCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }
}
