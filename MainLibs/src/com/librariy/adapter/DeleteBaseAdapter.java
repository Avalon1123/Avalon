package com.librariy.adapter;

import com.librariy.base.MyBaseAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
/**
 * 在原先的基础上拓充删除功能
 * @author 申
 *
 */
public abstract class DeleteBaseAdapter<B> extends MyBaseAdapter<B> {
	protected boolean isShowDelete=false;
	public DeleteBaseAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Item item;
		if (convertView==null) {
			 convertView = layoutInflater.inflate(getViewId(), null);
			item=initView(position, convertView, parent);
			convertView.setTag(item);
		}else {
			item=(Item) convertView.getTag();
		}
		updataView(position, convertView, parent,item);

		return convertView;
	}
	public abstract int getViewId();
	public  void updataView(int position, View convertView, ViewGroup parent,Item item)
	{
		final B b=getItem(position);
		if (null!=b) {
	        if (isShowDelete&&isCanDelete(b)) {
	        	item.check.setVisibility(View.VISIBLE);
	        	item.check.setChecked(getIsChecked(b));
			}else {
				item.check.setVisibility(View.GONE);
			}
//	        item.check.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (isShowDelete) {
//						((CheckedTextView) v.findViewById(R.id.check)).toggle();
//						setChecked(b,((CheckedTextView) v.findViewById(R.id.check)).isChecked());
//					} else {
//						itemClick(b);
//					}
//					 
//				}
//			});
	        updataView(item,b);
		}
	}
	public abstract void itemClick(B item);
	public abstract void updataView(Item item,B b);
	public abstract Item initView(int position, View convertView, ViewGroup parent);
	
	public  boolean isCanDelete(B item)
	{
		return true;
		
	}
	
	public void ChangeDelete(boolean isDelete)
	{
		isShowDelete=isDelete;
		super.notifyDataSetChanged(date);
	}
	public class Item
	{
		public CheckedTextView check;
	}
	public abstract boolean getIsChecked(B b);
	public abstract void setChecked(B b,boolean bb);
//	public OnLongClickListener onLongClickListener;
//	public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
//		this.onLongClickListener = onLongClickListener;
//	}
}
