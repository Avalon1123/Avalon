package com.librariy.widget.camera;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sharesdk.R;

public class AblumChoiceView extends TextView implements View.OnClickListener{
    private static final String TAG = AblumChoiceView.class.getSimpleName();
    public static String defaultAblumName="所有图片";    
    private OnAblumListener mOnAblumListener;
    private PopupWindow popWin;
    private LinearLayout listView;
    private float SP=1f,DP=1f;
    public AblumChoiceView(Context context) {
        super(context);
        this.init();
    }
    public AblumChoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }
    public AblumChoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }
    public void init() {
        this.SP = getContext().getResources().getDisplayMetrics().scaledDensity;
        this.DP= getContext().getResources().getDisplayMetrics().density;
        super.setGravity(Gravity.CENTER);
        super.setClickable(true);
        super.setText(defaultAblumName);
        super.setOnClickListener(this);        
        //1.初始化相册列表容器;
        ScrollView mScrollView=new ScrollView(getContext());
        //mScrollView.setPadding((int)(5*DP), (int)(5*DP), (int)(5*DP), (int)(5*DP));
        listView=new LinearLayout(getContext());  
        listView.setOrientation(LinearLayout.VERTICAL);
        mScrollView.addView(listView);
        //2.初始化PopWindow
        popWin=new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popWin.setBackgroundDrawable(new ColorDrawable(ZorCameraConfig.getColor(getContext(), R.color.zor_camera_title)));
        popWin.setOutsideTouchable(true);
        popWin.setFocusable(true); 
        popWin.setContentView(mScrollView);
    }
    public void setOnAblumListener(OnAblumListener mOnAblumListener) {
        this.mOnAblumListener = mOnAblumListener;
    }
    public void showSelector() {        
        popWin.showAsDropDown(this,0,5);
        reload();
    }
    
    public void reload() {
        if(listView.getChildCount()>0) return;
        listView.removeAllViews();
        String where ="0==0) GROUP BY  (" + MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME ;
        Cursor mCursor=MediaStore.Images.Media.query(getContext().getContentResolver(),Images.Media.EXTERNAL_CONTENT_URI, new String[]{ImageColumns.BUCKET_ID,ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.DATA,"count(*)"}, where, Images.Media.BUCKET_ID);
        this.addListItem(-1,defaultAblumName,"",-1);
        this.addItemDivider();
        if (mCursor != null) {
            while (mCursor.moveToNext()) {                
                this.addListItem(mCursor.getInt(0),mCursor.getString(1),mCursor.getString(2),mCursor.getInt(3));
                this.addItemDivider();
            }
            mCursor.close();
        }
   }    
    public void addItemDivider() {
        View v=new View(getContext());
        v.setBackgroundResource(R.color.zor_camera_divide);
        listView.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
    }
    public void addListItem(final int id,final String title,final String iconPath,final int imgCnt) {
        final Button tv=new Button(getContext());
        tv.setId(id);
        tv.setTag(title);
        tv.setPadding((int)(5*DP), (int)(5*DP), (int)(5*DP), (int)(5*DP));
        tv.setCompoundDrawablePadding((int)(10*DP));
        //tv.setLines(2);
        tv.setClickable(true);
        tv.setLineSpacing(0, 1.3f);
        tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        tv.setBackgroundResource(android.R.drawable.list_selector_background);
        Drawable mDrawable=getContext().getResources().getDrawable(ZorCameraConfig.Rsid_Ablum);
        mDrawable.setBounds(0, 0, (int)(60 * DP), (int)(60 * DP));
        tv.setCompoundDrawables(mDrawable, null, null, null);
        tv.setOnClickListener(this);
        SpannableStringBuilder builder=new SpannableStringBuilder("");
        builder.append(newStyleText(title, Typeface.BOLD, 18 * SP, ZorCameraConfig.getColor(getContext(), R.color.zor_camera_text_color)));
        if(imgCnt>=0){
            builder.append("\n");
            builder.append(newStyleText(imgCnt+"张", Typeface.NORMAL, 14 * SP, ZorCameraConfig.getColor(getContext(), R.color.zor_camera_text_color)));
        }
        tv.setText(builder);
        AsyncTask<Void, Void, Drawable> task = new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... params) {
                return ZorCameraConfig.getDrawable(getContext().getResources(), iconPath, 60 * DP, 60 * DP);
            }
            @Override
            protected void onPostExecute(Drawable result) {
                super.onPostExecute(result);
                tv.setCompoundDrawables(result, null, null, null);
            }
        };
        task.execute();
        listView.addView(tv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }    
    public SpannableString newStyleText(String text, int mTextStyle, float mTextSize, int mTextColor) {
        if(TextUtils.isEmpty(text)) return new SpannableString("");
        SpannableString styleString = new SpannableString(text);
        styleString.setSpan(new AbsoluteSizeSpan((int) mTextSize), 0, styleString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleString.setSpan(new ForegroundColorSpan(mTextColor), 0, styleString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleString.setSpan(new StyleSpan(mTextStyle), 0, styleString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styleString;
    }
    public interface OnAblumListener{
        public void onAblumChanged(int ablumId);
    }
    @Override
    public void onClick(View v) {
        if(v==this){
            showSelector();
        }else{
            popWin.dismiss();
            this.setText((String)v.getTag());
            if(mOnAblumListener!=null){
                mOnAblumListener.onAblumChanged(v.getId());
            }
        }
    }
}

