package com.librariy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

public class AdaptiveScrollView extends ScrollView {
    public AdaptiveScrollView(Context context) {
        super(context);
    }
    public AdaptiveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AdaptiveScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() < 1) {
            return;
        }
        int pWidthMode=MeasureSpec.getMode(widthMeasureSpec);
        int pWidthSize=MeasureSpec.getSize(widthMeasureSpec);
        final View child = getChildAt(0);
        final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
        if(pWidthMode!=MeasureSpec.UNSPECIFIED){
            widthMeasureSpec=MeasureSpec.makeMeasureSpec(pWidthSize, MeasureSpec.EXACTLY);
            int atMost=pWidthSize-lp.leftMargin-lp.rightMargin-getPaddingLeft()-getPaddingRight();
            if(lp.width==FrameLayout.LayoutParams.MATCH_PARENT){
                child.measure(MeasureSpec.makeMeasureSpec(atMost, MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED));
            }else if(lp.width==FrameLayout.LayoutParams.WRAP_CONTENT){
                child.measure(MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED));
            }else{
                child.measure(MeasureSpec.makeMeasureSpec(lp.width<atMost?lp.width:atMost, MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED));
            }
        }else{
            child.measure(MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(-1, MeasureSpec.UNSPECIFIED));
            int atLest=child.getMeasuredWidth()+lp.leftMargin-lp.rightMargin+getPaddingLeft()+getPaddingRight();
            widthMeasureSpec=MeasureSpec.makeMeasureSpec(atLest, MeasureSpec.EXACTLY);
        }
        //高度
        int childRealHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;        
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            int pHeightSizeAtMost=MeasureSpec.getSize(heightMeasureSpec);
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(childRealHeight<pHeightSizeAtMost?childRealHeight:pHeightSizeAtMost, MeasureSpec.EXACTLY);
        }else{
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(childRealHeight, MeasureSpec.EXACTLY);
        }
        super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
