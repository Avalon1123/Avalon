package com.librariy.view;

import com.librariy.base.ItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;
import cn.sharesdk.R;
/**
 * 删除浮现框
 * 用于列表删除
 * @author 申
 *
 */
public class DeleteView extends ItemView {
public TextView delete;
public CheckedTextView check;

	public DeleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DeleteView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		check=(CheckedTextView) findViewById(R.id.check);
		delete=(TextView) findViewById(R.id.delete_d);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (onOkClickListener!=null) {
					onOkClickListener.ok();
				}
			}
		});
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				check.toggle();
				if (onOkClickListener!=null) {
					onOkClickListener.onChange(check.isChecked());
				}
			}
		});
	}

	@Override
	public void update(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.view_delete;
	}
	OnOkClickListener onOkClickListener; 
	public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
		this.onOkClickListener = onOkClickListener;
	}
	public interface OnOkClickListener
	{
		public void ok();
		public void onChange(boolean isChecked);
	}
}
