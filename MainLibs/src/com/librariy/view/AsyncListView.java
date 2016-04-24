package com.librariy.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import cn.sharesdk.R;

import com.librariy.utils.Log;

public class AsyncListView extends ListView {
    private static final String TAG = AsyncListView.class.getSimpleName();
    private enum Status {
        None, Loading, Pressed
    }
    private Scroller mScroller = new Scroller(getContext(), new DecelerateInterpolator());
    private Drawable mEmptyImage;
    private PointF mPoint = new PointF();
    private int mPageSize = 20;
    private Status mStatus = Status.None;
    private boolean hasMore = true;

    private RingDrawable mRingDrawable = new RingDrawable();

    public AsyncListView(Context context) {
        super(context);
        this.initialise();
    }

    public AsyncListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialise();
    }
    public void setEmptyImage(int imgResId) {
        this.mEmptyImage=getResources().getDrawable(imgResId);
        this.mEmptyImage.setBounds(0, 0,mEmptyImage.getIntrinsicWidth() , mEmptyImage.getIntrinsicWidth());
    }
    public AsyncListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initialise();
    }

    public void setAdapter(AsyncListAdapter<?> adapter) {
        if (adapter instanceof AsyncListAdapter<?>) {
            ((AsyncListAdapter<?>) adapter).setListView(this);
        }
        super.setAdapter(adapter);
    }

    public AsyncListAdapter<?> getAsynAdapter() {
        if (super.getAdapter() instanceof AsyncListAdapter<?>) {
            return (AsyncListAdapter<?>) super.getAdapter();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void initialise() {
        this.setEmptyImage(R.drawable.bg_listview_empty);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            return;
        }
        super.setOverScrollMode(AbsListView.OVER_SCROLL_NEVER);
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            super.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            super.postInvalidate();
            return;
        }
    }

    public void smoothScrollTo(int x, int y, boolean isSmooth) {
        if (isSmooth) {
            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), x - mScroller.getCurrX(), y - mScroller.getCurrY());
        } else {
            mScroller.setFinalY(x);
            mScroller.setFinalY(y);
        }
        super.postInvalidate();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        // super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if ((super.getLastVisiblePosition() == super.getCount() - 1) && hasMore && mStatus == Status.None) {
            if (super.getChildCount() < 1) {
                return;
            }
            View v = super.getChildAt(super.getChildCount() - 1);
            Rect mRect = new Rect();
            if (!v.getLocalVisibleRect(mRect)) {
                return;
            }
            if (mRect.height() == v.getHeight()) {
                loadMore(getCount());
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
        return result;
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPoint.set(event.getX(), event.getY());
                mStatus = Status.Pressed;
                break;
            case MotionEvent.ACTION_MOVE:
                mStatus = Status.Pressed;
                if (mStatus == Status.Pressed && event.getAction() == MotionEvent.ACTION_MOVE) {
                    int distanceY = (int) ((mPoint.y - event.getY()));
                    mPoint.set(event.getX(), event.getY());
                    overScrollBy(distanceY);
                }
                break;
            case MotionEvent.ACTION_UP:
                mPoint.set(event.getX(), event.getY());
                mStatus = Status.None;
                int mScrollY = mScroller.getFinalY();
                if (Math.abs(mScrollY) >= RingDrawable.LiminalHeight) {
                    loadMore((mScrollY > 0) ? getCount() : 0);
                }
                if (Math.abs(mScrollY) > 0) {
                    this.smoothScrollTo(0, 0, true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mPoint.set(event.getX(), event.getY());
                mStatus = Status.None;
                this.smoothScrollTo(0, 0, true);
                break;
        }
        return super.onTouchEvent(event);
    }

    protected boolean overScrollBy(int distanceY) {
        int mChildCount = getChildCount();
        int newScrollY = mScroller.getFinalY() + distanceY;
        if (newScrollY > 0 && !hasMore)
            newScrollY = 1;// 禁止尾部滑动
        if (mChildCount < 1) {
            this.smoothScrollTo(0, newScrollY < -RingDrawable.MaxHeight ? -RingDrawable.MaxHeight : newScrollY, false);
            return true;
        }
        Rect mRect = new Rect();
        if ((super.getFirstVisiblePosition() == 0) && newScrollY < 0) {
            View v = super.getChildAt(0);
            if (!v.getLocalVisibleRect(mRect)) {
                return false;
            }
            if (mRect.height() == v.getHeight()) {
                this.smoothScrollTo(0, newScrollY < -RingDrawable.MaxHeight ? -RingDrawable.MaxHeight : newScrollY, false);
                return true;
            }
        } else if ((super.getLastVisiblePosition() == super.getCount() - 1) && newScrollY > 0) {
            View v = super.getChildAt(mChildCount - 1);
            if (!v.getLocalVisibleRect(mRect)) {
                return false;
            }
            if (mRect.height() == v.getHeight()) {
                this.smoothScrollTo(0, newScrollY > RingDrawable.MaxHeight ? RingDrawable.MaxHeight : newScrollY, false);
                return true;
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(super.getChildCount()<1&&mEmptyImage!=null){
            int width=mEmptyImage.getIntrinsicWidth();
            int height=mEmptyImage.getIntrinsicHeight();
            if(width>getMeasuredWidth()){
                width=getMeasuredWidth();
            }
            if(height>getMeasuredHeight()){
                height=getMeasuredHeight();
            }
            int left=getScrollX()+(getMeasuredWidth()-width)/2;
            int top=getScrollY()+(getMeasuredHeight()-height)/2;
            mEmptyImage.setBounds(left, top, left+width, top+height);
            mEmptyImage.draw(canvas);
        }
        Rect mRect = canvas.getClipBounds();
        if (mRect.top < 0) {
            mRect.bottom = 0;
        } else if (mRect.top > 0) {
            mRect.top = canvas.getHeight();
        } else {
            mRect.setEmpty();
        }
        if (mStatus == Status.Loading) {
            mRingDrawable.drawDynamicImage(canvas, mRect);
            super.postInvalidateDelayed(20, mRect.left, mRect.top, mRect.right, mRect.bottom);
        } else if (mStatus == Status.Pressed) {
            mRingDrawable.drawStaticImage(canvas, mRect);
        } else if (mStatus == Status.None) {
            mRingDrawable.drawDynamicImage(canvas, mRect);
        }
    }

    public void loadMore(final int position) {
        if (mStatus == Status.Loading) {
            return;
        }
        Log.d(TAG, "loadMore，postion=" + position);
        this.hasMore = true;
        if (!(getAsynAdapter() instanceof AsyncListAdapter)) {
            this.mStatus = Status.None;
            this.smoothScrollTo(0, 0, true);
        } else {
            mStatus = Status.Loading;
            if (position > 0) {
                this.smoothScrollTo(0, RingDrawable.MinHeight, true);
            } else {
                this.smoothScrollTo(0, -RingDrawable.MinHeight, true);
            }
            getAsynAdapter().loadData(position, (position + mPageSize) / mPageSize, mPageSize);
        }
    }

    private void dispatchLoadSuccess(int position, boolean hasMore) {
        this.hasMore = hasMore;
        this.mStatus = Status.None;
        Log.d(TAG, "dispatchLoadSuccess，postion=" + position + "; hasMore=" + hasMore);
        this.smoothScrollTo(0, 0, true);
    }

    public static abstract class AsyncListAdapter<T> extends BaseAdapter {
        protected static final String TAG = AsyncListAdapter.class.getSimpleName();
        protected AsyncListView listView;
        protected ArrayList<T> mData = new ArrayList<T>();

        public AsyncListAdapter() {
        }

        private void setListView(AsyncListView listView) {
            this.listView = listView;
        }

        protected Context getContext() {
            return listView.getContext();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        protected void setData(int position, List<T> mList) {
            // eg: children.size()=9, startIndex=10;
            while (mData.size() < position) {
                mData.add(null);
            }
            // eg: children.size()=11, startIndex=10;
            while (position < mData.size() && !mData.isEmpty()) {
                mData.remove(mData.size() - 1);
            }
            for (T item : mList) {
                mData.add(item);
            }
            if (position == 0) {
                super.notifyDataSetInvalidated();
            } else {
                super.notifyDataSetChanged();
            }
        }

        @Override
        public T getItem(int position) {
            if (position < 0 || position > mData.size() - 1) {
                Log.e(TAG, "Error: getItem(" + position + "), mData.size()=" + mData.size() + ",  Illegal item position[" + position + "]");
                return null;
            }
            return mData.get(position);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        protected void dispatchLoadSuccess(int position, List<T> result, boolean hasMore) {
            this.setData(position, result);
            listView.dispatchLoadSuccess(position, hasMore);
        }

        protected abstract void loadData(final int position, final int pageNum, final int pageSize);
    }

    private static class RingDrawable {
        private static final String TAG = RingDrawable.class.getSimpleName();
        // 全局参数
        private final static float DP = Resources.getSystem().getDisplayMetrics().widthPixels * 1.0f / 360;
        private final static RectF mBgRect = new RectF(0, 0, 60 * DP, 60 * DP);
        private final static RectF mRingRect = new RectF(mBgRect.left + 18 * DP, mBgRect.top + 18 * DP, mBgRect.right - 18 * DP, mBgRect.bottom - 18 * DP);
        public final static int MinHeight = (int) mBgRect.height();
        public final static int MaxHeight = (int) (mBgRect.height() * 2.5);
        public final static int LiminalHeight = (int) (mBgRect.height() * 1.5);

        // 成员变量
        private Paint mShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Path path = new Path();
        private Bitmap mBgBitmap = null;
        private int mAngle = 0;
        private long mLastTime = SystemClock.elapsedRealtime();

        public RingDrawable() {
            mBgBitmap = Bitmap.createBitmap((int) mBgRect.width(), (int) mBgRect.height(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(mBgBitmap);
            Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBgPaint.setColor(0xFFFFFFFF);
            mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mBgPaint.setShadowLayer(3 * DP, 0, 0, 0xFFCCCCCC);
            c.drawCircle(mBgRect.centerX(), mBgRect.centerY(), mBgRect.height() / 2 - 12 * DP, mBgPaint);
            // 2.圆环动画
            mShapePaint.setAntiAlias(true);
            mShapePaint.setColor(0xFF333333);
            mShapePaint.setStyle(Paint.Style.STROKE);
            mShapePaint.setStrokeWidth((float) (2.5 * DP));
            // 3.文字
            mTextPaint.setTextSize(12 * DP);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
        }

        private void postNextAngle() {
            float offsetAngle = (SystemClock.elapsedRealtime() - mLastTime) * 360 / 2000;
            if (offsetAngle < 1) {
                return;
            }
            this.mLastTime = SystemClock.elapsedRealtime();
            this.mAngle = (int) (mAngle + offsetAngle) % 360;
            Log.d(TAG, "setAngle(" + mAngle + ")");
        }

        public void drawDynamicImage(Canvas canvas, Rect bounds) {
            postNextAngle();
            canvas.save();
            // 设置透明度
            int mAlpha = (bounds.height() * 255 / RingDrawable.MinHeight);
            mAlpha = mAlpha > 255 ? 255 : mAlpha;
            mShapePaint.setAlpha(mAlpha);
            //
            canvas.clipRect(bounds);
            int startAngle = mAngle;
            canvas.drawColor(0x00FFFFFF);
            // canvas.drawColor(0xFFFF0000);
            // 1.画圆弧
            canvas.translate(bounds.centerX() - mBgRect.width() / 2, bounds.top);
            float mScale = (bounds.height() * 1.0f / RingDrawable.MinHeight);
            if (mScale < 1) {
                canvas.scale(mScale, mScale, mBgRect.centerX(), mBgRect.top);
            }
            canvas.drawBitmap(mBgBitmap, 0, 0, mShapePaint);
            int sweepAngle = startAngle > 300 ? 300 : startAngle;
            canvas.drawArc(mRingRect, startAngle, sweepAngle, false, mShapePaint);
            canvas.restore();
        }

        public void drawStaticImage(Canvas canvas, Rect bounds) {
            Log.d(TAG, "draw, this.getBounds()=" + bounds + "; ClipBounds=" + canvas.getClipBounds() + "; Union_Bounds=" + 1 + "; canvas.getHeight()=" + canvas.getHeight());
            canvas.save();
            mShapePaint.setAlpha(255);
            canvas.clipRect(bounds);
            canvas.drawColor(0x00FFFFFF);
            int startAngle = mAngle = (int) (bounds.height());
            // 1.画圆弧
            canvas.translate(bounds.centerX() - mBgRect.width() / 2, bounds.bottom - mBgRect.height());
            float mScale = (bounds.height() * 1.0f / RingDrawable.MinHeight);
            if (mScale < 1) {
                canvas.scale(mScale, mScale, mBgRect.centerX(), mBgRect.bottom);
            }
            canvas.drawBitmap(mBgBitmap, 0, 0, null);
            int sweepAngle = startAngle > 300 ? 300 : startAngle;
            canvas.drawArc(mRingRect, startAngle, sweepAngle, false, mShapePaint);
            if (bounds.height() >= RingDrawable.LiminalHeight) {
                int mAlpha = (bounds.height() - RingDrawable.LiminalHeight) * 255 / (RingDrawable.MaxHeight - RingDrawable.LiminalHeight);
                mTextPaint.setAlpha(mAlpha > 255 ? 255 : mAlpha);
                canvas.drawText("释放刷新数据", (int) mBgRect.centerX(), -2 * DP, mTextPaint);
            }
            // // 2.画箭头
            // int endAngle = startAngle + sweepAngle;
            // float radius = mRingRect.height() / 2;
            // double x1 = mRingRect.centerX() + radius * Math.cos(endAngle *
            // Math.PI / 180);
            // double y1 = mRingRect.centerY() + radius * Math.sin((endAngle *
            // Math.PI / 180));
            // // 点(x1,y1)上的切线角度（3点钟方向为0°角）
            // int tangentAngle = endAngle - 90;
            // // 方向校正，保证箭头看起来更合理
            // tangentAngle -= 15;
            // double x2 = x1 + (6 * DP) * Math.cos((tangentAngle - 30) *
            // Math.PI / 180);
            // double y2 = y1 + (6 * DP) * Math.sin((tangentAngle - 30) *
            // Math.PI / 180);
            // double x3 = x1 + (6 * DP) * Math.cos((tangentAngle + 30) *
            // Math.PI / 180);
            // double y3 = y1 + (6 * DP) * Math.sin((tangentAngle + 30) *
            // Math.PI / 180);
            //
            // path.rewind();
            // path.setLastPoint((float) x1, (float) y1);
            // path.lineTo((float) x2, (float) y2);
            // path.lineTo((float) x3, (float) y3);
            // path.lineTo((float) x1, (float) y1);
            // path.close();
            // canvas.drawPath(path, mShapePaint);
            canvas.restore();
        }
    }

}