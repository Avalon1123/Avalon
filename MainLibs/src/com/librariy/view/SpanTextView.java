package com.librariy.view;

import java.util.ArrayList;
import java.util.Collection;

import com.librariy.utils.Log;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class SpanTextView extends View {
    private static final String TAG = SpanTextView.class.getSimpleName();
    private int spanPadding = 0;
    private int spanSpace = 0;
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private GradientDrawable mBorderDrawable = new GradientDrawable();   
    private ArrayList<String> spanTextList = new ArrayList<String>();

    public SpanTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context,attrs);
    }

    public SpanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context,attrs);
    }

    public SpanTextView(Context context) {
        super(context);
        this.initialize(context,null);
    }
    private void initialize(Context context,AttributeSet attrs) {
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mBorderDrawable.setColor(0);
        this.mBorderDrawable.setShape(GradientDrawable.RECTANGLE);
        this.mBorderDrawable.setCornerRadius(getDimension(TypedValue.COMPLEX_UNIT_DIP, 5));        
        this.setBorderStyle(2,Color.argb(0xFF, 0xE7, 0x00, 0x44));
        this.setSpanPadding((int)getDimension(TypedValue.COMPLEX_UNIT_DIP, 3));
        this.setSpanSpace((int)getDimension(TypedValue.COMPLEX_UNIT_DIP, 5));
        //
        TypedArray mTypedArray=context.obtainStyledAttributes(attrs, new int[]{android.R.attr.textSize,android.R.attr.textColor});
        float textSize=mTypedArray.getDimension(0, -1f);
        int textColor=mTypedArray.getColor(1, Color.BLACK);
        this.setTextColor(textColor);
        if(textSize>0){
            this.setTextSize(textSize);
        }else{
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        }
    }
    public void setTextColor(int mTextColor) {
        mTextPaint.setColor(mTextColor);
        super.postInvalidate();
    }
    public void setBorderStyle(int mBorderWidth,int mBorderColor) {
        this.mBorderDrawable.setStroke(mBorderWidth, mBorderColor);
        super.requestLayout();
    }
    public void setSpanSpace(int spanSpace) {
        this.spanSpace = spanSpace;
        super.requestLayout();
    }
    public void setSpanPadding(int spanPadding) {
        this.spanPadding = spanPadding;
        super.requestLayout();
    }
    public void setSpanText(String ... mList) {
        spanTextList.clear();
        for(String spanText:mList){
            spanTextList.add(spanText);
        }
        Log.d(TAG, "setSpanText(String ... mList), spanTextList="+spanTextList);
        super.requestLayout();
    }
    public void setSpanText(Collection<String> mList) {
        spanTextList.clear();
        for(String spanText:mList){
            spanTextList.add(spanText);
        }
        Log.d(TAG, "setSpanText(Collection<String> mList), spanTextList="+spanTextList);
        super.requestLayout();
    }
    /**
     * Set the default text size to a given unit and value.  See {@link
     * TypedValue} for the possible dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     *
     * @attr ref android.R.styleable#TextView_textSize
     */
    public void setTextSize(int unit, float size) {
        this.setTextSize(getDimension(unit,size));
    }
    public void setTextSize(float realSize) {       
        if (realSize != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(realSize);
            super.requestLayout();
        }
    }
    public float getDimension(int unit, float size) {
        Context c = getContext();
        Resources r=((c==null)?Resources.getSystem():c.getResources());       
        float realSize=TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
        return realSize;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 控件希望得到的宽度
        int desiredWidth = getPaddingLeft() + getPaddingRight() + (int) getTextWidth();
        int realWidth = -1;
        // 判断模式
        switch (widthMode) {
            // 如果是AT_MOST，不能超过父View的宽度
            case MeasureSpec.AT_MOST:
                realWidth = Math.min(widthSize, desiredWidth);
                break;
            // 如果是精确的，是多少，就给多少；
            case MeasureSpec.EXACTLY:
                realWidth = widthSize;
                break;
            // 这种情况，可以不考虑
            case MeasureSpec.UNSPECIFIED:
                realWidth = -1;
                break;
        }
        int desiredHeight = getPaddingTop() + getPaddingBottom() + (int) getTextHeight();
        int realHeight = -1;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                realHeight = Math.min(heightSize, desiredHeight);
                break;
            case MeasureSpec.EXACTLY:
                realHeight = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                realHeight = -1;
                break;
        }
        //调用父类的测量方法
        setMeasuredDimension(realWidth, realHeight);
    }

    private float getTextWidth() {
        float mRealWidth = 0;
        for (String spanText : spanTextList) {
            mRealWidth = mRealWidth + spanSpace + mTextPaint.measureText(spanText)+spanPadding*2;
        }
        mRealWidth -= spanSpace;
        return mRealWidth;
    }

    private float getTextHeight() {
        return mTextPaint.getFontMetrics().descent - mTextPaint.getFontMetrics().ascent+spanPadding*2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);        
        float drawVerticalMargin=(getHeight()-getTextHeight())/2;
        float drawTop=drawVerticalMargin;
        float drawBottom=getHeight()-drawVerticalMargin;
        float drawRight=getWidth()-getPaddingRight();
        FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        float baseline = drawTop + (drawBottom - drawTop - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        float x=getPaddingLeft();
        for(int i = 0; i < spanTextList.size(); i++) {            
            String drawText = spanTextList.get(i);
            float drawWidth = mTextPaint.measureText(drawText)+spanPadding * 2;
            if(x + drawWidth>drawRight){
                break;
            }
            canvas.save();
            canvas.translate(x, 0);
            mBorderDrawable.setBounds(0, (int)drawTop , (int)drawWidth, (int)drawBottom);
            mBorderDrawable.draw(canvas);
            canvas.drawText(drawText, drawWidth/2, baseline, mTextPaint);
            x += drawWidth + spanSpace;
            canvas.restore();
        }
    }
}
