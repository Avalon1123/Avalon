/**
 * @Title: LoadingDialog.java 
 * @date 2013-5-6 
 * @version    
 */
package com.librariy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.sharesdk.R;


/** 
 * @ClassName: LoadingDialog 
 * @date 2013-5-6 
 *  
 */
public class LoadingDialog extends Dialog {

	private Context context = null;
	private TextView loadingTv;
	
	public LoadingDialog(Context context) {
		super(context);
		this.context = context;

	}

	public LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		loadingTv = (TextView) view.findViewById(R.id.loading_txt);
		setContentView(view);
	}
	
	public void setMsg(String msg) {
		if (null != loadingTv) {
			loadingTv.setText(msg);
		}
	}

	public void setMsg(int resId) {
		if (null != loadingTv) {
			setMsg(context.getString(resId));
		}
	}
}
