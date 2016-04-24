package com.librariy.view;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class PopAnimation extends PopupWindow {
    private static final String TAG = PopAnimation.class.getSimpleName();
    public PopAnimation(Context mContext) {
        super(ViewGroup.LayoutParams.MATCH_PARENT,(int)(50*mContext.getResources().getDisplayMetrics().density));
        super.setContentView(new RotateRingView(mContext));
        super.setBackgroundDrawable(new PaintDrawable(0));
        super.setOutsideTouchable(true);
        super.setFocusable(true);
    }
}