package com.librariy.bean;

import com.librariy.base.FragmentBase;

import android.support.v4.app.Fragment;
import android.view.View;
/**
 * tabview标签
 * @author 易申
 *
 */
public class TabViewItem {

	
	public String title;
	
	public FragmentBase fragment;

	public View view;
	
	public TabViewItem(String title, FragmentBase fragment) {
	
		this.title = title;
		this.fragment = fragment;
		if(fragment!=null)
			fragment.setCache(true);
	}

	public TabViewItem(String title, View view) {	
		this.title = title;
		this.view = view;
	}
	
	
	
	
}
