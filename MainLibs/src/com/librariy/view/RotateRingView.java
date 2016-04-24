package com.librariy.view;

import com.librariy.drawable.RotateRingDrawable;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class RotateRingView extends View {
    private int startAngle = 0, endAngle = 0;
    private RotateRingDrawable mDrawable=new RotateRingDrawable();
    public RotateRingView(Context context) {
        super(context);
    }
    public RotateRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RotateRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onAttachedToWindow() {
        super.postDelayed(task, 100);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        super.removeCallbacks(task);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultValue = mDrawable.getIntrinsicHeight();
        if(MeasureSpec.getMode(widthMeasureSpec)!=MeasureSpec.EXACTLY){
            widthMeasureSpec=MeasureSpec.makeMeasureSpec(defaultValue, defaultValue);
        }
        if(MeasureSpec.getMode(heightMeasureSpec)!=MeasureSpec.EXACTLY){
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(defaultValue, defaultValue);
        }
        super.setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable.setAngle(startAngle);
        mDrawable.draw(canvas);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            startAngle = (startAngle + 5) % 360;
            endAngle = (endAngle + 10) % 360;
            invalidate();
            postDelayed(this, 40);
        }
    };
}