package com.librariy.annotactions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@EViewGroup
public abstract class ViewGroupBase extends LinearLayout{

	
	public ViewGroupBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ViewGroupBase(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	@AfterViews
	protected abstract void init();
	 

	 

}
