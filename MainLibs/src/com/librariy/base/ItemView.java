package com.librariy.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
/**
 *  ViewGroupBase
 * @author 易申
 *
 */
public abstract class ItemView extends LinearLayout{

	public ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial();
		// TODO Auto-generated constructor stub
	}

	public ItemView(Context context) {
		super(context);
		initial();
		// TODO Auto-generated constructor stub
	}
	
	void initial()
	{
		LayoutInflater.from(getContext()).inflate(getViewId(), this);
		init();
	}
	
	public abstract void init();
	
	public abstract void update(Object o);
	
	public abstract int getViewId();

}
