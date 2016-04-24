package com.librariy.annotactions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import com.librariy.base.FragmentBase;

import android.os.Bundle;
import android.view.View;
/**
 * 在 FragmentBase基础中加入注解框架
 * @author 易申
 *
 */
@EFragment
public abstract class AnnotationsFragmentBase extends FragmentBase{

 	@AfterViews
 	protected void init() 
 	{
 	   if (isIntialized&&isCache) 
 	       return;
			try {
				this.afterView(view);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.afterInit();
 	   
 	}
	protected abstract void afterView(View v) throws Exception;
	@Override
	protected void IntialComponent(View v, Bundle savedInstanceState)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void IntialListener(View v, Bundle savedInstanceState)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
 	
}
