package com.librariy.utils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.librariy.view.SpanTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewBinder {
    private final View mRootView;
    private SparseArray<Object> mViewMap = new SparseArray<Object>();

    public ViewBinder(View mContentView) {
        this.mRootView = mContentView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getRootView() {
        return (T) mRootView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        Object tagetView = mViewMap.get(viewId);
        if (tagetView == null) {
            tagetView = mRootView.findViewById(viewId);
            mViewMap.put(viewId, tagetView);
        }
        return tagetView == null ? null : (T) tagetView;
    }
    public int getColor(int colorResid) {
        Object tagetColor = mViewMap.get(colorResid);
        if (tagetColor==null) {
            tagetColor=mRootView.getResources().getColor(colorResid);
            mViewMap.put(colorResid, tagetColor);
        }
        return tagetColor == null ? 0 : (Integer) tagetColor;
    }

    public void setText(int viewId, CharSequence text) {
        View tagetView = getView(viewId);
        if (tagetView instanceof TextView) {
            ((TextView) tagetView).setText(text);
        }
    }

    public void setSpanText(int viewId, String[] optSplitArray) {
        SpanTextView tagetView = getView(viewId);
        tagetView.setSpanText(optSplitArray);
    }

    public void displayImage(int viewId, String url, int defaultImg) {
        ImageView tagetView = getView(viewId);
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.considerExifParams(true);
        builder.showImageOnFail(defaultImg);
        builder.showImageOnLoading(defaultImg);
        builder.showImageForEmptyUri(defaultImg);
        ImageLoader.getInstance().displayImage(url, tagetView, builder.build());
    }
}