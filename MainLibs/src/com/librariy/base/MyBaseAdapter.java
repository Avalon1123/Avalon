package com.librariy.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
/**
 * 简化 baseadapter 可配套loadingmorefragmen 使用
 * @author 易申
 *
 * @param <B> 代表list里面类型
 */
public abstract class MyBaseAdapter<B> extends BaseAdapter {
	public Context context;
	public List<B> date=new ArrayList<B>();
	protected LayoutInflater  layoutInflater; 
	public MyBaseAdapter(Context context)
	{
		this.context=context;
		layoutInflater=LayoutInflater.from(context);
	}
	public void notifyDataSetChanged(List<B> date) {
		// TODO Auto-generated method stub
		if(date!=null)
			this.date=date;
		else {
			this.date.clear();
		}
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return date.size();
	}
	@Override
	public B getItem(int position) {
		// TODO Auto-generated method stub
		return date.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void startActivity(Intent intent)
	{
	    if(context!=null)
	        context.startActivity(intent);
	}
	public void startActivityForResult(Intent intent,int requestCode)
	{
	    if(context==null)
	        return;
	    try {
	        Activity a=(Activity) context;
	        a.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
	    
	}
}
