package com.librariy.view;

import com.librariy.base.AppContextBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 放大图片View
 * 
 * @author 申
 * 
 */
public class EnlargeView extends ImageView {
	  protected Rect mFloatRect = new Rect();// 浮层的Rect
	public static enum EVENT {
		LT, RT, LB, RB, DRAG, SCALE, NONE
	}
	int screenWidth,screenHeight;
	private Paint textPaint = new Paint();
	// 在touch重要用到的点
	private PointF mPoint = new PointF(0, 0);
	// 两点触控最后一次两点之间的距离
	private float mDistance = 0;
	public EVENT mEevent = EVENT.NONE;
	protected float mGlobalRation = 0;
	protected float mGlobalScale = 1;
	protected boolean isInitialized = false;
	protected Context mContext;

	public EnlargeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public EnlargeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public EnlargeView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	  @SuppressLint("NewApi")
	    private void init(Context context) {
	        this.mContext = context;
	        try {
	            if (android.os.Build.VERSION.SDK_INT >= 11) {
	                this.setLayerType(LAYER_TYPE_SOFTWARE, null);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        DisplayMetrics dm = getResources().getDisplayMetrics();  
	        screenWidth = dm.widthPixels;  
	        screenHeight = dm.heightPixels - 50;  
	        textPaint.setColor(Color.RED);
	        setBackgroundColor(Color.BLACK);
	        setScaleType(ScaleType.FIT_CENTER);
	    }

	    private void checkMatrixInitValues() {
	        if (mGlobalScale == -1) {
	            Matrix newMatrix = getImageMatrix();
	            float[] values = new float[9];
	            newMatrix.getValues(values);
	            mGlobalScale = Math.min(values[Matrix.MSCALE_X], values[Matrix.MSCALE_Y]);
	        }
	    }

	    public void postRotate(float offsetDegrees) {
	        this.checkMatrixInitValues();
	        mGlobalRation = (mGlobalRation + offsetDegrees) % 360;
	        this.updateMatrix();
	    }

	    public void postScale(float offsetScale) {
	        this.checkMatrixInitValues();
	        mGlobalScale = mGlobalScale * offsetScale;
	        mGlobalScale = (mGlobalScale > 10) ? 10 : mGlobalScale;
	        mGlobalScale = (mGlobalScale < 0.25f) ? 0.25f : mGlobalScale;
	        this.updateMatrix();
	    }

	    private void updateMatrix() {
	        if (getDrawable() == null) {
	            return;
	        }
	        Matrix newMatrix = super.getImageMatrix();
	        newMatrix.setScale(mGlobalScale, mGlobalScale);
	        newMatrix.postRotate(mGlobalRation);
	        RectF r = new RectF(getDrawable().getBounds());
	        newMatrix.mapRect(r);
	        newMatrix.postTranslate(getWidth() / 2 - r.centerX(), getHeight() / 2 - r.centerY());
	        super.setImageMatrix(newMatrix);
	        invalidate();
	    }


	    @SuppressLint("ClickableViewAccessibility")
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        if (event.getPointerCount() == 1) {
	            this.onOnePointerMove(event);
	        } else if (event.getPointerCount() == 2) {
	            this.onTowPointerMove(event);
	        }
	        return true;
	    }

	    public void onOnePointerMove(MotionEvent event) {
	        int eventId = event.getAction();
	        if (eventId == MotionEvent.ACTION_DOWN) {
	            mPoint.set(event.getX(), event.getY());
	            mEevent = EVENT.NONE;
	            return; 
	        } else if (eventId == MotionEvent.ACTION_MOVE) {
	            mEevent = EVENT.NONE;
	            int dx = (int) ( event.getX() - mPoint.x);  
                int dy = (int) (event.getY() - mPoint.y);  
//             // 设置不能出界  
//                dx=dx>0?dx:0;
//                dy=dy>0?dy:0;
                int left = this.getLeft() + dx;  
                int top = this.getTop() + dy;  
                int right = left+this.getWidth();  
                int bottom = top + this.getHeight();  
//                // 设置不能出界  
//                if (left < this.getLeft()) {  
//                    left = 0;  
//                    right = left + this.getWidth();  
//                }  
//  
//                if (right > screenWidth) {  
//                    right = screenWidth;  
//                    left = right - this.getWidth();  
//                }  
//  
//                if (top < 0) {  
//                    top = 0;  
//                    bottom = top + this.getHeight();  
//                }  
//  
//                if (bottom > screenHeight) {  
//                    bottom = screenHeight;  
//                    top = bottom - this.getHeight();  
//                }  
                AppContextBase.log("layout1", left+"，"+top+"，"+right+"，"+bottom+"，");
                Matrix m= super.getImageMatrix();
               m.postTranslate(dx, dy);
               setImageMatrix(m);
               super.invalidate();
//                this.layout(left, top,right, bottom); 
                
 	           AppContextBase.log("layout2", left+"，"+top+"，"+right+"，"+bottom+"，");
                mPoint.set(event.getX(), event.getY());
	        }

	    }

	    public void onTowPointerMove(MotionEvent event) {
	        int eventId = event.getAction();
	        if (eventId == MotionEvent.ACTION_POINTER_DOWN) {
	            mEevent = EVENT.SCALE;
	            this.mDistance = -1;
	            return;
	        } else if (eventId != MotionEvent.ACTION_MOVE) {
	            mEevent = EVENT.NONE;
	            this.mDistance = -1;
	            return;
	        }
	        float tempDistance = (float) Math.hypot(event.getX(1) - event.getX(0), event.getY(1) - event.getY(0));
	        if (mDistance < 1) {
	            mDistance = tempDistance;
	            return;
	        }
	        // 整个控件对角线长度分成50份，每份为一个长度单位
	        float mUnit = (float) Math.hypot(getWidth(), getHeight()) / 50;
	        float mScale = (tempDistance - mDistance) / mUnit;
	        if (mScale >= -1 && mScale <= 1) {
	            return;
	        }
	        mDistance = tempDistance;
	        mScale = (mScale < 0) ? (-1 / mScale) : (mScale);
	        this.postScale(mScale);
	    }

//	    // 根据初触摸点判断是触摸的Rect哪一个角
//	    public EVENT getTouchEvent(PointF p) { 
//	        if (mFloatDrawable.getBounds().left <= p.x && p.x < (mFloatDrawable.getBounds().left + mFloatDrawable.getBorderWidth()) && mFloatDrawable.getBounds().top <= p.y && p.y < (mFloatDrawable.getBounds().top + mFloatDrawable.getBorderHeight())) {
//	            return EVENT.LT;
//	        } else if ((mFloatDrawable.getBounds().right - mFloatDrawable.getBorderWidth()) <= p.x && p.x < mFloatDrawable.getBounds().right && mFloatDrawable.getBounds().top <= p.y && p.y < (mFloatDrawable.getBounds().top + mFloatDrawable.getBorderHeight())) {
//	            return EVENT.RT;
//	        } else if (mFloatDrawable.getBounds().left <= p.x && p.x < (mFloatDrawable.getBounds().left + mFloatDrawable.getBorderWidth()) && (mFloatDrawable.getBounds().bottom - mFloatDrawable.getBorderHeight()) <= p.y && p.y < mFloatDrawable.getBounds().bottom) {
//	            return EVENT.LB;
//	        } else if ((mFloatDrawable.getBounds().right - mFloatDrawable.getBorderWidth()) <= p.x && p.x < mFloatDrawable.getBounds().right && (mFloatDrawable.getBounds().bottom - mFloatDrawable.getBorderHeight()) <= p.y && p.y < mFloatDrawable.getBounds().bottom) {
//	            return EVENT.RB;
//	        } else if (mFloatDrawable.getBounds().contains((int) p.x, (int) p.y)) {
//	            return EVENT.DRAG;
//	        }
//	        return EVENT.NONE;
//	    }

	    @Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        if (!this.isInitialized) {
	            //
	            this.mGlobalRation = 0;
	            this.mGlobalScale = -1;
	            //
//	            mFloatRect.set(0, 0, cropWidth, cropHeight);
//	            mFloatRect=getDrawable().getBounds();
	            while (mFloatRect.width() > this.getWidth() || mFloatRect.height() > this.getHeight()) {
	                mFloatRect.right = mFloatRect.centerX();
	                mFloatRect.bottom = mFloatRect.centerY();
	            }
	            mFloatRect.offsetTo(getWidth() / 2 - mFloatRect.width() / 2, getHeight() / 2 - mFloatRect.height() / 2);
	            isInitialized = true;
	        }
	        // 在画布上花图片
	        canvas.save();
	        // 在画布上画浮层FloatDrawable,Region.Op.DIFFERENCE是表示Rect交集的补集
//	        canvas.clipRect(mFloatRect);
//	        canvas.clipRect(getDrawable().getBounds(), Region.Op.DIFFERENCE);
	        // 在交集的补集上画上灰色用来区分
//	        canvas.drawColor(Color.parseColor("#A0000000"));
	        canvas.restore();
	        // 画浮层
//	        mFloatDrawable.setBounds(mFloatRect);
//	        mFloatDrawable.draw(canvas);
	        AppContextBase.log("draw", mFloatRect.toString()+"");
	    }

	    private void configureBounds(boolean translateCanvas) {
	        // 校验是否把浮层拖出了可见视图
	        Matrix mMatrix = this.getImageMatrix();
	        if (translateCanvas) {
	            if (mFloatRect.left < 0) {
	                mMatrix.postTranslate(-mFloatRect.left, 0);
	                mFloatRect.offset(-mFloatRect.left, 0);
	            }
	            if (mFloatRect.top < 0) {
	                mMatrix.postTranslate(0, -mFloatRect.top);
	                mFloatRect.offset(0, -mFloatRect.top);
	            }
	            if (mFloatRect.right > getWidth()) {
	                mMatrix.postTranslate(-(mFloatRect.right - getWidth()), 0);
	                mFloatRect.offset(-(mFloatRect.right - getWidth()), 0);
	            }
	            if (mFloatRect.bottom > getHeight()) {
	                mMatrix.postTranslate(0, -(mFloatRect.bottom - getHeight()));
	                mFloatRect.offset(0, -(mFloatRect.bottom - getHeight()));
	            }
	        } else {
	            int maxWidth = getWidth();
	            int maxHeight = getHeight();
	            if (mFloatRect.left < 0) {
	                mFloatRect.left = 0;
	                int newHeight = mFloatRect.width() ;
	                mFloatRect.top = mFloatRect.bottom - newHeight;
	            }
	            if (mFloatRect.top < 0) {
	                mFloatRect.top = 0;
	                int newWidth = mFloatRect.height() ;
	                mFloatRect.left = mFloatRect.right - newWidth;
	            }
	            if (mFloatRect.right > maxWidth) {
	                mFloatRect.right = maxWidth;
	                int newHeight = mFloatRect.width() ;
	                mFloatRect.bottom = mFloatRect.top + newHeight;
	            }
	            if (mFloatRect.bottom > maxHeight) {
	                mFloatRect.bottom = maxHeight;
	                int newWidth = mFloatRect.height() ;
	                mFloatRect.right = mFloatRect.left + newWidth;
	            }
	        }
	    }

//	    // 进行图片的裁剪，所谓的裁剪就是根据Drawable的新的坐标在画布上创建一张新的图片
//	    public Bitmap getCropImage() {
//	        Bitmap tmpBitmap = Bitmap.createBitmap(mFloatRect.width(), mFloatRect.height(), Config.RGB_565);
//	        Canvas canvas = new Canvas(tmpBitmap);
//	        Matrix matrix = new Matrix(super.getImageMatrix());
//	        matrix.postTranslate(-mFloatRect.left, -mFloatRect.top);
//	        canvas.setMatrix(matrix);
//	        canvas.clipRect(0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), Region.Op.UNION);
//	        Drawable mDrawable = super.getDrawable();
//	        if (mDrawable != null) {
//	            mDrawable.draw(canvas);
//	        }
//	        Bitmap ret = Bitmap.createScaledBitmap(tmpBitmap, 200, 200, false);
//	        tmpBitmap.recycle();
//	        tmpBitmap = null;
//	        return ret;
//	    }

	    public int dipTopx(Context context, float dpValue) {
	        final float scale = context.getResources().getDisplayMetrics().density;
	        return (int) (dpValue * scale + 0.5f);
	    }

//	    /**
//	     * FloatDrawable 继承自Drawable 功能：图片上面的浮动框，通过拖动确定位置
//	     */
//	    public class FloatDrawable extends Drawable {
//
//	        private Context mContext;
//	        private int offset = 50;
//	        private Paint mLinePaint = new Paint();
//	        private Paint mLinePaint2 = new Paint();
//	        {
//	            // mLinePaint.setARGB(0, 50, 50, 50);
//	            mLinePaint.setStrokeWidth(1F);
//	            mLinePaint.setStyle(Paint.Style.STROKE);
//	            mLinePaint.setAntiAlias(true);
//	            mLinePaint.setColor(Color.RED);
//	            // mLinePaint2.setARGB(200, 50, 50, 50);
//	            mLinePaint2.setStrokeWidth(7F);
//	            mLinePaint2.setStyle(Paint.Style.STROKE);
//	            mLinePaint2.setAntiAlias(true);
//	            mLinePaint2.setColor(Color.GREEN);
//	        }
//
//	        public FloatDrawable(Context context) {
//	            super();
//	            this.mContext = context;
//	        }
//
//	        public int getBorderWidth() {
//	            return dipTopx(mContext, offset);// 根据dip计算的像素值，做适配用的
//	        }
//
//	        public int getBorderHeight() {
//	            return dipTopx(mContext, offset);
//	        }
//
//	        @Override
//	        public void draw(Canvas canvas) {
//
//	            int left = getBounds().left;
//	            int top = getBounds().top;
//	            int right = getBounds().right;
//	            int bottom = getBounds().bottom;
//
//	            Rect mRect = new Rect(left + dipTopx(mContext, offset) / 2, top + dipTopx(mContext, offset) / 2, right - dipTopx(mContext, offset) / 2, bottom - dipTopx(mContext, offset) / 2);
//	            // 画默认的选择框
//	            canvas.drawRect(mRect, mLinePaint);
//	            // 画四个角的四个粗拐角、也就是八条粗线
//	            canvas.drawLine((left + dipTopx(mContext, offset) / 2 - 3.5f), top + dipTopx(mContext, offset) / 2, left + dipTopx(mContext, offset) - 8f, top + dipTopx(mContext, offset) / 2, mLinePaint2);
//	            canvas.drawLine(left + dipTopx(mContext, offset) / 2, top + dipTopx(mContext, offset) / 2, left + dipTopx(mContext, offset) / 2, top + dipTopx(mContext, offset) / 2 + 30, mLinePaint2);
//	            canvas.drawLine(right - dipTopx(mContext, offset) + 8f, top + dipTopx(mContext, offset) / 2, right - dipTopx(mContext, offset) / 2, top + dipTopx(mContext, offset) / 2, mLinePaint2);
//	            canvas.drawLine(right - dipTopx(mContext, offset) / 2, top + dipTopx(mContext, offset) / 2 - 3.5f, right - dipTopx(mContext, offset) / 2, top + dipTopx(mContext, offset) / 2 + 30, mLinePaint2);
//	            canvas.drawLine((left + dipTopx(mContext, offset) / 2 - 3.5f), bottom - dipTopx(mContext, offset) / 2, left + dipTopx(mContext, offset) - 8f, bottom - dipTopx(mContext, offset) / 2, mLinePaint2);
//	            canvas.drawLine((left + dipTopx(mContext, offset) / 2), bottom - dipTopx(mContext, offset) / 2, (left + dipTopx(mContext, offset) / 2), bottom - dipTopx(mContext, offset) / 2 - 30f, mLinePaint2);
//	            canvas.drawLine((right - dipTopx(mContext, offset) + 8f), bottom - dipTopx(mContext, offset) / 2, right - dipTopx(mContext, offset) / 2, bottom - dipTopx(mContext, offset) / 2, mLinePaint2);
//	            canvas.drawLine((right - dipTopx(mContext, offset) / 2), bottom - dipTopx(mContext, offset) / 2 - 30f, right - dipTopx(mContext, offset) / 2, bottom - dipTopx(mContext, offset) / 2 + 3.5f, mLinePaint2);
//
//	        }
//
//	        @Override
//	        public void setBounds(Rect bounds) {
//	            super.setBounds(new Rect(bounds.left - dipTopx(mContext, offset) / 2, bounds.top - dipTopx(mContext, offset) / 2, bounds.right + dipTopx(mContext, offset) / 2, bounds.bottom + dipTopx(mContext, offset) / 2));
//	        }
//
//	        @Override
//	        public void setAlpha(int alpha) {
//
//	        }
//
//	        @Override
//	        public void setColorFilter(ColorFilter cf) {
//
//	        }
//
//	        @Override
//	        public int getOpacity() {
//	            return 0;
//	        }
//
//	        public int dipTopx(Context context, float dpValue) {
//	            final float scale = context.getResources().getDisplayMetrics().density;
//	            return (int) (dpValue * scale + 0.5f);
//	        }
//	    }
}
