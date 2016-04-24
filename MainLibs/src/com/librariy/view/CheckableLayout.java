package com.librariy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

public class CheckableLayout extends FrameLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
    private boolean mChecked;
    private Drawable dT, dB, dL, dR;
    private int paddingT = 0, paddingB = 0, paddingL = 0, paddingR = 0;
    private int drawablePadding = 10;

    public CheckableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context, attrs);
    }

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context, attrs);
    }

    public CheckableLayout(Context context) {
        super(context);
        this.initialize(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        //super.setClickable(false);
        this.paddingT = getPaddingTop();
        this.paddingB = getPaddingBottom();
        this.paddingL = getPaddingLeft();
        this.paddingR = getPaddingRight();
        if (attrs == null)
            return;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, new int[] { android.R.attr.drawableTop, android.R.attr.drawableBottom, android.R.attr.drawableLeft, android.R.attr.drawableRight, android.R.attr.drawablePadding });
        dT = mTypedArray.getDrawable(0);
        dB = mTypedArray.getDrawable(1);
        dL = mTypedArray.getDrawable(2);
        dR = mTypedArray.getDrawable(3);
        drawablePadding = mTypedArray.getDimensionPixelSize(4, 5);
        mTypedArray.recycle();
        updateCompoundDrawables();
    }

    private void updateCompoundDrawables() {
        int left = paddingL, top = paddingT, right = paddingR, bottom = paddingB;
        if (dL != null) {
            left = paddingL + dL.getIntrinsicWidth() + drawablePadding;
            dL.setBounds(0, 0, dL.getMinimumWidth(), dL.getIntrinsicHeight());
        }
        if (dT != null) {
            top = paddingT + dT.getIntrinsicHeight() + drawablePadding;
            dT.setBounds(0, 0, dT.getMinimumWidth(), dT.getIntrinsicHeight());
        }
        if (dR != null) {
            right = paddingR + dR.getIntrinsicWidth() + drawablePadding;
            dR.setBounds(0, 0, dR.getMinimumWidth(), dR.getIntrinsicHeight());
        }
        if (dB != null) {
            bottom = paddingB + dB.getIntrinsicHeight() + drawablePadding;
            dB.setBounds(0, 0, dB.getMinimumWidth(), dB.getIntrinsicHeight());
        }
        this.setPadding(left, top, right, bottom);
    }

    public void setCompoundDrawables(int left, int top, int right, int bottom) {
        this.dL = (left > 0) ? getResources().getDrawable(left) : null;
        this.dT = (top > 0) ? getResources().getDrawable(top) : null;
        this.dR = (right > 0) ? getResources().getDrawable(right) : null;
        this.dB = (bottom > 0) ? getResources().getDrawable(bottom) : null;
        updateCompoundDrawables();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // Left
        if (dL != null) {
            canvas.save();
            canvas.translate(paddingL, getHeight() / 2 - dL.getIntrinsicHeight() / 2);
            dL.draw(canvas);
            canvas.restore();
        }
        // Top
        if (dT != null) {
            canvas.save();
            canvas.translate(getWidth() / 2 - dT.getIntrinsicWidth() / 2, paddingT);
            dT.draw(canvas);
            canvas.restore();
        }
        // Right
        if (dR != null) {
            canvas.save();
            canvas.translate(getWidth() - paddingR - dR.getIntrinsicHeight(), getHeight() / 2 - dR.getIntrinsicHeight() / 2);
            dR.draw(canvas);
            canvas.restore();
        }
        // Bottom
        if (dB != null) {
            canvas.save();
            canvas.translate(getWidth() / 2 - dB.getIntrinsicWidth() / 2, getHeight() - paddingB - dB.getIntrinsicHeight());
            dB.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int status[] = getDrawableState();
        boolean changed = false;
        if (dL != null && dL.isStateful()) {
            dL.setState(status);
            changed = true;
        }
        if (dT != null && dT.isStateful()) {
            dT.setState(status);
            changed = true;
        }
        if (dR != null && dR.isStateful()) {
            dR.setState(status);
            changed = true;
        }
        if (dB != null && dB.isStateful()) {
            dB.setState(status);
            changed = true;
        }
        if (changed) {
            postInvalidate();
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
        }
        refreshDrawableState();
        postInvalidate();
    }
    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!this.isChecked());
    }
    @Override
    public boolean performClick() {
        this.toggle();
        return super.performClick();
    }
}