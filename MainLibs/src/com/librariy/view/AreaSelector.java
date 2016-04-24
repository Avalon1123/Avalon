package com.librariy.view;

import java.util.ArrayList;
import java.util.List;

import com.librariy.utils.Metadata;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import cn.sharesdk.R;

public class AreaSelector extends PopupWindow implements AdapterView.OnItemClickListener {
    private static final String TAG = AreaSelector.class.getSimpleName();
    private View rootView;
    private ListView typeView1, typeView2, typeView3;
    private MyArrayAdapter<String> adapter1, adapter2, adapter3;
    private OnSelectedListener mOnSelectedListener = null;
    public AreaSelector(Context mContext) {
        this(mContext,R.layout.libs_list_item_checked_1);
    }
    public AreaSelector(Context mContext,int itemResId) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, mContext.getResources().getDisplayMetrics()));
        rootView = LayoutInflater.from(mContext).inflate(R.layout.view_area_selector, null);
        super.setContentView(rootView);
        super.setBackgroundDrawable(new PaintDrawable(0));
        super.setOutsideTouchable(true);
        super.setFocusable(true);
        typeView1 = (ListView) rootView.findViewById(R.id.listview_type1);
        typeView2 = (ListView) rootView.findViewById(R.id.listview_type2);
        typeView3 = (ListView) rootView.findViewById(R.id.listview_type3);

        adapter1 = new MyArrayAdapter<String>(mContext,itemResId);
        adapter2 = new MyArrayAdapter<String>(mContext,itemResId);
        adapter3 = new MyArrayAdapter<String>(mContext,itemResId);

        typeView1.setAdapter(adapter1);
        typeView2.setAdapter(adapter2);
        typeView3.setAdapter(adapter3);

        typeView1.setOnItemClickListener(this);
        typeView2.setOnItemClickListener(this);
        typeView3.setOnItemClickListener(this);

        adapter1.setDataSet(Metadata.getChildren("RootData1"));
        typeView1.setItemChecked(0, true);
        typeView1.setSelection(0);
        adapter2.setDataSet(Metadata.getChildrenWithEmptyString("不限", "RootData1", adapter1.getItem(0)));
        typeView2.setSelection(0);
    }
    public void setBorderStyle(int color,int left, int top, int right, int bottom){
        PaintDrawable d=new PaintDrawable(color);
        d.setPadding(left, top, right, bottom);
        super.setBackgroundDrawable(d);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == typeView1) {
            String path1 = (String) typeView1.getItemAtPosition(typeView1.getCheckedItemPosition());
            adapter2.setDataSet(Metadata.getChildrenWithEmptyString("不限", "RootData1", path1));
            typeView2.clearChoices();
            typeView2.setSelection(0);
            typeView2.setVisibility(View.VISIBLE);
            typeView3.setVisibility(View.GONE);
        } else if (parent == typeView2) {
            String path1 = (String) typeView1.getItemAtPosition(typeView1.getCheckedItemPosition());
            String path2 = (String) typeView2.getItemAtPosition(typeView2.getCheckedItemPosition());
            if (!"不限".equals(path2)) {
                adapter3.setDataSet(Metadata.getChildrenWithEmptyString("不限", "RootData1", path1, path2));
                typeView3.clearChoices();
                typeView3.setSelection(0);
                typeView3.setVisibility(View.VISIBLE);
            } else if (mOnSelectedListener != null) {
                adapter3.setDataSet(new ArrayList<String>());
                typeView3.clearChoices();
                typeView3.setVisibility(View.GONE);
                mOnSelectedListener.onSelected(path1, "不限", "不限");
                this.dismiss();
            } else {
                this.dismiss();
            }
        } else if (parent == typeView3) {
            String path1 = (String) typeView1.getItemAtPosition(typeView1.getCheckedItemPosition());
            String path2 = (String) typeView2.getItemAtPosition(typeView2.getCheckedItemPosition());
            String path3 = (String) typeView3.getItemAtPosition(typeView3.getCheckedItemPosition());
            Log.e(TAG, path1 + "-" + path2 + " - " + path3);
            if (mOnSelectedListener != null) {
                mOnSelectedListener.onSelected(path1, path2, path3);
            }
            this.dismiss();
        }
    }

    public void showAsDropDown(View anchor, OnSelectedListener mOnSelectedListener) {
        this.mOnSelectedListener = mOnSelectedListener;
        super.showAsDropDown(anchor, 0, 0);
    }

    public void dismiss() {
        this.mOnSelectedListener = null;
        super.dismiss();
    }

    public static interface OnSelectedListener {
        public void onSelected(String path1, String path2, String path3);
    }

    private static class MyArrayAdapter<T> extends ArrayAdapter<T> {
        public MyArrayAdapter(Context context,int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
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
