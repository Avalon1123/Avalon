package com.librariy.dialog;


import com.librariy.utils.SystemDisplay;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.R;

public class CustomDialogBase extends Dialog implements View.OnClickListener {
    private static final String TAG = CustomDialogBase.class.getSimpleName();
    public static interface Btn{
        public final static int closeButton=android.R.id.closeButton;
        public final static int button1=android.R.id.button1;
        public final static int button2=android.R.id.button2;
        public final static int button3=android.R.id.button3;
    }
    protected LinearLayout mRootView;
    protected LinearLayout mTitlePanel;
    protected FrameLayout mContentPanel;
    protected LinearLayout mBottomPanel;
    protected SystemDisplay sd;

    public CustomDialogBase(Context context) {
        super(context);
        this.sd=SystemDisplay.getInstance(context);
        super.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // 1.RootView
        this.mRootView = new LinearLayout(getContext());
        mRootView.setOrientation(LinearLayout.VERTICAL);
        super.setContentView(mRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 2.TitlePanel
        this.mTitlePanel = new LinearLayout(getContext());
        mTitlePanel.setGravity(Gravity.CENTER_VERTICAL);
        mTitlePanel.setOrientation(LinearLayout.HORIZONTAL);
        TextView title = new TextView(getContext());
        title.setId(android.R.id.title);
        title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        title.setText("--");
        mTitlePanel.addView(title, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        ImageView closeImg=new ImageView(getContext());
        closeImg.setId(android.R.id.closeButton);
        closeImg.setVisibility(View.GONE);
        closeImg.setOnClickListener(this);
        mTitlePanel.addView(closeImg,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mRootView.addView(mTitlePanel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        // 3.ContentPanel
        this.mContentPanel = new FrameLayout(getContext());
        mRootView.addView(mContentPanel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        // 4.BottomPanel
        this.mBottomPanel = new LinearLayout(getContext());
        mBottomPanel.setOrientation(LinearLayout.HORIZONTAL);
        mBottomPanel.setVisibility(View.VISIBLE);
        mRootView.addView(mBottomPanel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * Get the default text size to a given unit and value. See
     * {@link TypedValue} for the possible dimension units.
     *
     * @param unit
     *            The desired dimension unit.
     * @param size
     *            The desired size in the given units.
     *
     * @return the real Pixel Dimension
     */
    protected int getDimensionSize(int unit, int value) {
        Context c = getContext();
        Resources r = ((c == null) ? Resources.getSystem() : c.getResources());
        float f = TypedValue.applyDimension(unit, value, r.getDisplayMetrics());
        return (f >= 0) ? (int) (f + 0.5) : value;
    }
    protected int getDevicesHeightPixels() {
        Context c = getContext();
        Resources r = ((c == null) ? Resources.getSystem() : c.getResources());
        return (r==null||r.getDisplayMetrics()==null)?-1:(r.getDisplayMetrics().heightPixels);
    }
    protected int getDevicesWidthPixels() {
        Context c = getContext();
        Resources r = ((c == null) ? Resources.getSystem() : c.getResources());
        return (r==null||r.getDisplayMetrics()==null)?-1:(r.getDisplayMetrics().widthPixels);
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView) mTitlePanel.findViewById(android.R.id.title)).setText(title);
    }
    public TextView getTitleView() {
        return ((TextView) mTitlePanel.findViewById(android.R.id.title));
    }
    protected void setCloseImage(int resId) {
        ImageView closeImg=((ImageView)mTitlePanel.findViewById(android.R.id.closeButton));
        closeImg.setVisibility(View.VISIBLE);
        closeImg.setImageResource(resId);
    }
    public void setContentView(int layoutResID) {
        mContentPanel.removeAllViews();
        LayoutInflater.from(getContext()).inflate(layoutResID, mContentPanel);
    }

    public void setMessage(String msg) {
        mContentPanel.removeAllViews();
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tv.setPadding(20, 20, 20, 20);
        tv.setText(msg);
        mContentPanel.addView(tv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public void setItems(CharSequence[] items, final AdapterView.OnItemClickListener listener) {
        mContentPanel.removeAllViews();
        ListView view = new ListView(getContext());
        view.setDivider(new ColorDrawable(Color.LTGRAY));
        view.setDividerHeight(1);
        view.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        view.setAdapter(new ArrayAdapter<CharSequence>(getContext(), R.layout.libs_list_item_checked_1, items));
        view.setOnItemClickListener(listener);
        mContentPanel.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public void setSingleChoiceItems(CharSequence[] items, int checkedItem, final AdapterView.OnItemClickListener listener) {
        mContentPanel.removeAllViews();
        ListView view = new ListView(getContext());
        view.setDivider(new ColorDrawable(Color.LTGRAY));
        view.setDividerHeight(1);
        view.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        view.setAdapter(new ArrayAdapter<CharSequence>(getContext(), R.layout.libs_list_item_checked_1, items));
        view.setItemChecked(checkedItem, true);
        view.setOnItemClickListener(listener);
        mContentPanel.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }    
    public EditText setSingleEdit(String hint) {
        mContentPanel.removeAllViews();
        EditText edit=new EditText(getContext());
        edit.setHint(hint);
        edit.setPadding(10, 10, 10, 10);
        edit.setGravity(Gravity.LEFT|Gravity.TOP);
        edit.setTextAppearance(this.getContext(), android.R.style.TextAppearance_Small_Inverse);
        int mDevicesWidthPixels=getDevicesWidthPixels();
        edit.setWidth((int)(mDevicesWidthPixels*0.6));
        edit.setHeight((int)(mDevicesWidthPixels*0.3));
        mContentPanel.addView(edit, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return edit;
    }
    public ListView getListView() {
        View v=mContentPanel.getChildAt(0);
        if(v instanceof ListView){
            return  (ListView)v;
        }else{
            return null;
        }
    }
    public EditText getEditText() {
        View v=mContentPanel.getChildAt(0);
        if(v instanceof EditText){
            return  (EditText)v;
        }else{
            return null;
        }
    }
    public void addButton(int BtnID,String btnText, View.OnClickListener mOnClickListener) {
        mBottomPanel.setVisibility(View.VISIBLE);
        Button mBtn = new Button(getContext());
        mBtn.setId(BtnID);
        mBtn.setText(btnText);
        mBtn.setTag(mBottomPanel.getChildCount());
        mBtn.setOnClickListener(mOnClickListener != null ? mOnClickListener : this);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        if(mBottomPanel.getChildCount()>0){
            p.leftMargin=(int)(1*sd.density+0.5);
        }
        mBottomPanel.addView(mBtn,p);
    }
    public void addButton(String btnText, View.OnClickListener mOnClickListener) {
       this.addButton(-1, btnText, mOnClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.closeButton:
                this.dismiss();
                break;
            default:
                break;
        }
    }
    protected void onStyleInitialize(){
        int paddingRoot=getDimensionSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        ViewGroup parent=(ViewGroup)mRootView.getParent();
        parent.setPadding(paddingRoot, paddingRoot, paddingRoot, paddingRoot);
        //Window Style
        mRootView.setBackgroundResource(android.R.color.background_light);
        //Title Style
        mTitlePanel.setBackgroundResource(android.R.color.background_light);
        int padding=getDimensionSize(TypedValue.COMPLEX_UNIT_DIP, 5);
        mTitlePanel.setPadding(padding,padding,padding,padding);
        TextView mTitleView =this.getTitleView();
        mTitleView.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium_Inverse);
        //
        int mContentPanelPadding=getDimensionSize(TypedValue.COMPLEX_UNIT_DIP, 5);
        this.mContentPanel.setPadding(mContentPanelPadding, mContentPanelPadding, mContentPanelPadding, mContentPanelPadding);
    }
    public void show() {
        if(mRootView.getTag()!=Boolean.TRUE){
            onStyleInitialize();
            mRootView.setTag(true);
        }
        super.show();
    }
}
