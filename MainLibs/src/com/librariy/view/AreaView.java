package com.librariy.view;

import java.util.ArrayList;
import java.util.List;

import com.librariy.utils.Log;
import com.librariy.utils.Metadata;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.R;

public class AreaView extends FrameLayout implements AdapterView.OnItemClickListener {
    private static final String TAG = AreaView.class.getSimpleName();
    private ListView typeView1, typeView2, typeView3;
    private MyArrayAdapter<String> adapter1, adapter2, adapter3;
    private OnSelectedListener mOnSelectedListener = null;

    public AreaView(Context mContext) {
        super(mContext);
        this.initialize(mContext, null, 0, 0);
    }

    public AreaView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.initialize(mContext, attrs, 0, 0);
    }

    public AreaView(Context mContext, AttributeSet attrs, int defStyleAttr) {
        super(mContext, attrs, defStyleAttr);
        this.initialize(mContext, attrs, defStyleAttr, 0);
    }

    public void initialize(Context mContext, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        LayoutInflater.from(mContext).inflate(R.layout.view_area_selector, this);
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, new int[] { android.R.attr.listSelector}, defStyleAttr, defStyleRes);
        int itemStyleResId = mTypedArray.getResourceId(0, 0);
        mTypedArray.recycle();
        super.setFocusable(true);
        typeView1 = (ListView) findViewById(R.id.listview_type1);
        typeView2 = (ListView) findViewById(R.id.listview_type2);
        typeView3 = (ListView) findViewById(R.id.listview_type3);

        adapter1 = new MyArrayAdapter<String>(mContext, itemStyleResId);
        adapter2 = new MyArrayAdapter<String>(mContext, itemStyleResId);
        adapter3 = new MyArrayAdapter<String>(mContext, itemStyleResId);

        typeView1.setAdapter(adapter1);
        typeView2.setAdapter(adapter2);
        typeView3.setAdapter(adapter3);

        typeView1.setOnItemClickListener(this);
        typeView2.setOnItemClickListener(this);
        typeView3.setOnItemClickListener(this);
        ((LinearLayout.LayoutParams) typeView3.getLayoutParams()).weight = 2;
        findViewById(R.id.divider1).setVisibility(View.GONE);
        typeView1.setVisibility(View.GONE);
        typeView2.setVisibility(View.VISIBLE);
        typeView3.setVisibility(View.VISIBLE);
        this.setBorderStyle(0xFF333333,1f);
        reset();
    }
    public void reset() {
        adapter1.setDataSet(Metadata.getChildren("RootData1"));
        typeView1.setItemChecked(0, true);
        typeView1.setSelection(0);
        adapter2.setDataSet(Metadata.getChildren("RootData1", adapter1.getItem(0)));
        typeView2.clearChoices();
        adapter3.setDataSet(new ArrayList<String>(0));
        typeView3.clearChoices();
    }
    public void setBorderStyle(int color,float width) {
        PaintDrawable d = new PaintDrawable(color);
        d.getPaint().setStyle(Style.STROKE);
        d.getPaint().setStrokeWidth(width);
        d.setPadding((int)(width+0.5), (int)(width+0.5), (int)(width+0.5), (int)(width+0.5));
        this.getChildAt(0).setBackgroundDrawable(d);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == typeView1) {
            String path1 = (String) typeView1.getItemAtPosition(typeView1.getCheckedItemPosition());
            adapter2.setDataSet(Metadata.getChildrenWithEmptyString("不限", "RootData1", path1));
            typeView2.clearChoices();
            typeView2.setSelection(0);
            typeView2.setVisibility(View.VISIBLE);
            adapter3.setDataSet(new ArrayList<String>(0));
            typeView3.clearChoices();
            typeView3.setVisibility(View.VISIBLE);
            if (mOnSelectedListener != null) {
                mOnSelectedListener.onSelected(path1, "不限", "不限");
            }
        } else if (parent == typeView2) {
            String path1 = (String) typeView1.getItemAtPosition(typeView1.getCheckedItemPosition());
            String path2 = (String) typeView2.getItemAtPosition(typeView2.getCheckedItemPosition());
            if (!"不限".equals(path2)) {
                adapter3.setDataSet(Metadata.getChildrenWithEmptyString("不限", "RootData1", path1, path2));
                typeView3.clearChoices();
                typeView3.setSelection(0);
                typeView3.setVisibility(View.VISIBLE);
            } else {
                adapter3.setDataSet(new ArrayList<String>(0));
                typeView3.clearChoices();
                typeView3.setVisibility(View.VISIBLE);
            }
            if (mOnSelectedListener != null) {
                mOnSelectedListener.onSelected(path1, path2, "不限");
            }
        } else if (parent == typeView3) {
            String path1 = (String) typeView1.getItemAtPosition(typeView1.getCheckedItemPosition());
            String path2 = (String) typeView2.getItemAtPosition(typeView2.getCheckedItemPosition());
            String path3 = (String) typeView3.getItemAtPosition(typeView3.getCheckedItemPosition());
            Log.d(TAG, path1 + "-" + path2 + " - " + path3);
            if (mOnSelectedListener != null) {
                mOnSelectedListener.onSelected(path1, path2, path3);
            }
        }
    }

    public void setOnSelectedListener(OnSelectedListener mOnSelectedListener) {
        this.mOnSelectedListener = mOnSelectedListener;
    }

    public static interface OnSelectedListener {
        public void onSelected(String path1, String path2, String path3);
    }

    private static class MyArrayAdapter<T> extends ArrayAdapter<T> {
        private int itemStyleResId = 0;
        private float SP = 0;

        public MyArrayAdapter(Context context, int itemStyleResId) {
            super(context, R.layout.libs_list_item_checked_1);
            this.SP=context.getResources().getDisplayMetrics().density;
            this.itemStyleResId=itemStyleResId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             TextView v=(TextView)super.getView(position, convertView, parent);
             if(itemStyleResId<R.anim.imageloader_slide_in){
                 return v;
             }
             TypedArray mTypedArray=getContext().obtainStyledAttributes(itemStyleResId, new int[]{android.R.attr.textColor,android.R.attr.background,android.R.attr.padding});
             ColorStateList colorList=mTypedArray.getColorStateList(0);
             if(colorList!=null){
                 //colorList = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{android.R.attr.state_checked},{}}, new int[] {0xFFFF0000,0xFFFF0000,0xFF333333}); 
                 v.setTextColor(colorList);
             }
             Drawable d=mTypedArray.getDrawable(1);
             if(d!=null){
                 //d=new ColorDrawable(0xFFFFFFFF);
                 v.setBackgroundDrawable(d);
             }
             int padding=mTypedArray.getDimensionPixelSize(2, -1);
             if(padding>=0){
                 v.setPadding(padding, padding, padding, padding);
             }
             mTypedArray.recycle();
             return v;
        }

        public void setDataSet(List<T> list) {
            super.clear();
            for (T value : list) {
                super.add(value);
            }
            super.notifyDataSetChanged();
        }
    }
}
