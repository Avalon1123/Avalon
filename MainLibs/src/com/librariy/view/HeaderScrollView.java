package com.librariy.view;

import com.librariy.base.AppContextBase;
import com.librariy.utils.Log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import cn.sharesdk.R;
public class HeaderScrollView extends LinearLayout {
    private static final String TAG = HeaderScrollView.class.getSimpleName();
    private Scroller mScroller = new Scroller(getContext(), new DecelerateInterpolator());
    private PointF mPoint = new PointF();
    private int mMaxLen = 0;

    public HeaderScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public HeaderScrollView(Context mContext) {
        super(mContext);
        this.initialize();
    }

    private void initialize() {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setTag(android.R.id.custom, v.getVisibility() != View.VISIBLE);
            v.setVisibility(View.VISIBLE);
        }
    }
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            super.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            super.postInvalidate();
            return;
        }
    }
    public void smoothScrollTo(int x,int y,boolean isSmooth) {
        if(isSmooth){
            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), x-mScroller.getCurrX(), y-mScroller.getCurrY());
        }else{
            mScroller.setFinalY(x);
            mScroller.setFinalY(y);
        }
        super.postInvalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.VISIBLE) {
                v.setTag(R.id.layout, true);
                v.setVisibility(View.VISIBLE);
            }
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.d(TAG, "onMeasure(), widthMeasureSpec=" + MeasureSpec.toString(widthMeasureSpec) + ", heightMeasureSpec=" + MeasureSpec.toString(heightMeasureSpec));
        // 控件希望得到的宽度
        // 判断模式
        switch (heightMode) {
        // 如果是AT_MOST，不能超过父View的宽度
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                int mTotalHeight = MeasureSpec.getSize(heightMeasureSpec);
                super.setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mTotalHeight, MeasureSpec.EXACTLY));
                float mWeightSum = 0.0f;
                for (int i = 0; i < getChildCount(); i++) {
                    View v = getChildAt(i);
                    if (v.getVisibility() == View.VISIBLE) {
                        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
                        mTotalHeight -= (p.topMargin + p.bottomMargin);
                        if ((p.weight > 0) && v.getTag(R.id.layout) != Boolean.TRUE) {
                            mWeightSum += p.weight;
                        } else {
                            v.measure(widthMeasureSpec, (p.height > 0) ? MeasureSpec.makeMeasureSpec(p.height, MeasureSpec.EXACTLY) : MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED));
                            if (v.getTag(R.id.layout) != Boolean.TRUE) {
                                mTotalHeight -= v.getMeasuredHeight();
                            }
                        }
                    }
                }
                for (int i = 0; i < getChildCount(); i++) {
                    View v = getChildAt(i);
                    LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
                    if (v.getVisibility() == View.VISIBLE && p.weight > 0) {
                        float cHeight = (mTotalHeight * p.weight) / mWeightSum;
                        v.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) cHeight, MeasureSpec.EXACTLY));
                    }
                }
                mMaxLen = 0;
                for (int i = 0; i < getChildCount(); i++) {
                    View v = getChildAt(i);
                    LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
                    if (v.getTag(R.id.layout) == Boolean.TRUE) {
                        mMaxLen += (v.getMeasuredHeight() + p.topMargin + p.bottomMargin);
                    }
                }
                break;
            case MeasureSpec.UNSPECIFIED:
                mTotalHeight = 0;
                for (int i = 0; i < getChildCount(); i++) {
                    View v = getChildAt(i);
                    LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
                    if (v.getVisibility() == View.VISIBLE) {
                        v.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED));
                        mTotalHeight += (v.getMeasuredHeight() + p.topMargin + p.bottomMargin);
                    }
                }
                super.setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) mTotalHeight, MeasureSpec.EXACTLY));
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topY = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v.getVisibility() == View.VISIBLE) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
                v.layout(getPaddingLeft(), topY + p.topMargin, getMeasuredWidth() - getPaddingRight(), topY + v.getMeasuredHeight() + p.topMargin);
                topY += (v.getMeasuredHeight() + p.topMargin + p.bottomMargin);
            }
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int distanceX = (int) (mPoint.x - event.getX());        
        int distanceY = (int) (mPoint.y - event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPoint.set(event.getX(), event.getY());
                if(Math.abs(distanceX)>Math.abs(distanceY)) break;
                int mScrollY = mScroller.getFinalY() + distanceY;
                mScrollY = mScrollY < 0 ? 0 : mScrollY;
                mScrollY = mScrollY > mMaxLen ? mMaxLen : mScrollY;
                if (mScrollY != mScroller.getFinalY()) {
                    this.smoothScrollTo(0, mScrollY,false);
                    return true;
                }
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
    @Override
    @SuppressLint("ClickableViewAccessibility")
   public boolean onTouchEvent(MotionEvent event) {
        int distanceY = (int) (mPoint.y - event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mPoint.set(event.getX(), event.getY());
                int mScrollY = getScrollY() + distanceY;
                mScrollY = mScrollY < 0 ? 0 : mScrollY;
                mScrollY = mScrollY > mMaxLen ? mMaxLen : mScrollY;
                if (mScrollY != getScrollY()) {
                    this.smoothScrollTo(0, mScrollY,false);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mPoint.set(event.getX(), event.getY());
                mScrollY = getScrollY();
                mScrollY = (mScrollY > mMaxLen / 2) ? mMaxLen : 0;
                if (mScrollY != getScrollY()) {
                    this.smoothScrollTo(0, mScrollY,true);
                    return true;
                }
            default:
                mPoint.set(event.getX(), event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	// TODO Auto-generated method stub
    	AppContextBase.log(TAG, ev.toString());
    	return super.dispatchTouchEvent(ev);
    }
}