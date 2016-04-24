package com.librariy.base;

import com.librariy.utils.UIHelper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import cn.sharesdk.R;

public abstract class  DialogBase extends Dialog {
	View layout;
	public DialogBase(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public DialogBase(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public DialogBase(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(getViewId());
			layout =findViewById(R.id.layout);
			this.IntialComponent(savedInstanceState);
			this.IntialListener(savedInstanceState);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		// TODO Auto-generated method stub
		super.setCanceledOnTouchOutside(cancel);
		if (cancel&&null!=layout) {
			layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
	}
	public void toast(String msg)
	{
		if (getContext()!=null) {
			UIHelper.showToast(getContext(), msg);
		}
		
	}
	public abstract int getViewId();
	protected abstract void IntialComponent( Bundle savedInstanceState)throws Exception;
	protected abstract void IntialListener(Bundle savedInstanceState) throws Exception;
}
