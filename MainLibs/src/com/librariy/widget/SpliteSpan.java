package com.librariy.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ReplacementSpan;

public class SpliteSpan extends ReplacementSpan {
    private int mMargin = 0;
    private Paint mPaint = new Paint();

    public SpliteSpan(int mColor) {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mColor);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
        Rect bounds = new Rect();
        paint.getTextBounds(text.toString(), start, end, bounds);
        return bounds.width() + 2 * mMargin;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Rect targetRect = new Rect();
        paint.getTextBounds(text.toString(), start, end, targetRect);
        targetRect.offsetTo((int) (x + mMargin), (top + bottom) / 2 - targetRect.height() / 2);
        canvas.drawRect(targetRect.centerX()-2, targetRect.top, targetRect.centerX()+2, targetRect.bottom, mPaint);
    }

    /**
     * @param
     * */
    public static SpannableString newSpannableString(int mColor) {
        SpannableString styleString = new SpannableString("#");
        styleString.setSpan(new SpliteSpan(mColor), 0, styleString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styleString;
    }
}