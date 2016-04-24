package com.librariy.drawable;

import com.librariy.utils.SystemDisplay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class RotateRingDrawable extends Drawable {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float UNIT = 1.0f;
    private RectF mBgRect,mRingRect;
    private Path path=new Path();
    private Bitmap mBgBitmap = null;
    private int mAngle = 0;

    public RotateRingDrawable() {
        float radius=SystemDisplay.getInstance(null).heightPixels/14;
        this.UNIT=radius/50;
        this.mBgRect = new RectF(0, 0, radius, radius);       
        this.mRingRect = new RectF(mBgRect.left + 12 * UNIT, mBgRect.top + 12 * UNIT, mBgRect.right - 12 * UNIT, mBgRect.bottom - 12 * UNIT);
        this.initialise();
    }

    public void setAngle(int mAngle) {
        this.mAngle = mAngle;
    }
    public void resetAngle(int mAngle) {
        this.mAngle=Integer.MAX_VALUE;
    }
    public int getIntrinsicWidth() {
        return (int)mBgRect.width();
    }
    public int getIntrinsicHeight() {
        return (int)mBgRect.height();
    }
    public void initialise() {
        super.setBounds(0,0,getMinimumWidth(),getMinimumHeight());
        // 1.背景圆形
        if (mBgBitmap != null && !mBgBitmap.isRecycled()) {
            mBgBitmap.recycle();
        }
        mBgBitmap = Bitmap.createBitmap((int) mBgRect.width(), (int) mBgRect.height(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(mBgBitmap);
        Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(0xFFFFFFFF);
        mBgPaint.setStyle(Style.FILL_AND_STROKE);
        mBgPaint.setShadowLayer(3 * UNIT, 0, 0, 0xFFCCCCCC);
        c.drawCircle(mBgRect.centerX(), mBgRect.centerY(), mBgRect.height() / 2 - 3 * UNIT, mBgPaint);
        // 2.圆环动画
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFF333333);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(3 * UNIT);
    }
    
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mAngle=Integer.MAX_VALUE;
    }
    
    @Override
    public void draw(Canvas canvas) {
        if(mAngle==Integer.MAX_VALUE){
            drawArcAndArrow(canvas);
        }else{
            drawArc(canvas);
        }
    }    
    public void drawArc(Canvas canvas) {
        canvas.save();
        int startAngle=mAngle;
        //canvas.drawColor(0x000000);
        Rect bounds = getBounds();
        //1.画圆弧
        canvas.translate(bounds.centerX() - mBgRect.width() / 2, bounds.bottom - mBgRect.height());
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
         int sweepAngle=startAngle > 300 ? 300 : startAngle;
        canvas.drawArc(mRingRect, startAngle, sweepAngle, false, mPaint);
        canvas.restore();
    }
    public void drawArcAndArrow(Canvas canvas) {
        canvas.save();
        canvas.drawColor(0xFFFFFFFF);
        Rect bounds = getBounds();
        int startAngle=(int)(bounds.height()*1.5);
        //1.画圆弧
        canvas.translate(bounds.centerX() - mBgRect.width() / 2, bounds.bottom - mBgRect.height());
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
         int sweepAngle=startAngle > 300 ? 300 : startAngle;
        canvas.drawArc(mRingRect, startAngle, sweepAngle, false, mPaint);
        //2.画箭头
        int endAngle = startAngle+sweepAngle;
        float radius = mRingRect.height() / 2;
        double x1 = mRingRect.centerX() + radius * Math.cos(endAngle * Math.PI / 180);
        double y1 = mRingRect.centerY() + radius * Math.sin((endAngle * Math.PI / 180));
        //点(x1,y1)上的切线角度（3点钟方向为0°角）
        int tangentAngle=endAngle-90;
        //方向校正，保证箭头看起来更合理
        tangentAngle-=15;
        double x2 = x1+(6 * UNIT)*Math.cos((tangentAngle-30)* Math.PI / 180);
        double y2 = y1+(6 * UNIT)*Math.sin((tangentAngle-30) * Math.PI / 180);        
        double x3 = x1+(6 * UNIT)*Math.cos((tangentAngle+30) * Math.PI / 180);
        double y3 = y1+(6 * UNIT)*Math.sin((tangentAngle+30) * Math.PI / 180);

        path.rewind();
        path.setLastPoint((float)x1, (float)y1);
        path.lineTo((float)x2, (float)y2);
        path.lineTo((float)x3, (float)y3);
        path.lineTo((float)x1, (float)y1);
        path.close();
        canvas.drawPath(path, mPaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}