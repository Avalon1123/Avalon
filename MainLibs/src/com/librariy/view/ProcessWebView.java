package com.librariy.view;


import com.librariy.utils.IHttp;
import com.librariy.view.HijackWebView.IHijackPost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.sharesdk.R;

public class ProcessWebView extends LinearLayout {

	String firstUrl="";
	String url;
	IHttp  httpListener;
	HijackWebView webview;
	public HijackWebView getWebview() {
		return webview;
	}

	ProgressBar progress;
	public ProcessWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Intial();
		
	
	}

	public ProcessWebView(Context context) {
		super(context);
		Intial();
	}
	
	public void setHijackPostListener(IHijackPost hijackPostListener) {
		webview.setHijackPostListener(hijackPostListener);
	}

	public void setProcessListener(IHttp httpListener) {
		this.httpListener = httpListener;
		
	}
	
	public String getCurrentUrl()
	{
		return webview.getUrl();
	}
	
	public boolean IsBacktoReloadPoint()
	{
		return firstUrl.equals(getCurrentUrl());
	}
	

//	@SuppressLint("SetJavaScriptEnabled")
	private void Intial()
	{
		LayoutInflater l=LayoutInflater.from(getContext());
		l.inflate(R.layout.api_process_webview, this);
		webview=(HijackWebView) findViewById(R.id.api_progresswebview_webview);
		progress=(ProgressBar) findViewById(R.id.api_progresswebview_processbar);
//		webview.getSettings().setJavaScriptEnabled(true);
		WebSettings setting=webview.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setDomStorageEnabled(true);
		webview.setProceesChangeListener(new IHttp() {
			
			@Override
			public void OnPostStart() {
				// TODO Auto-generated method stub
				
				progress.setVisibility(View.VISIBLE);
				if(httpListener!=null)
					httpListener.OnPostStart();
			}
			
			@Override
			public void OnPostCompelete() {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);
				if(httpListener!=null)
					httpListener.OnPostCompelete();
			}
			
			@Override
			public void OnPostProcess(int newProgress) {
				// TODO Auto-generated method stub
//				Log.i("newProgress", newProgress+"");
				progress.setProgress(newProgress);
				if(httpListener!=null)
					httpListener.OnPostProcess(newProgress);
			}

			@Override
			public void OnException(Exception e) {
				if(httpListener!=null)
					httpListener.OnException(e);
				// TODO Auto-generated method stub
				
			}

		
		});
		
	
		
	}
	
	public void loadUrl(String url)
	{
		this.url=url;
		this.webview.loadUrl(url);
	}
	
	
	public void  loadString(String s,String url)
	{
		webview.loadString(s,url);
	}
   
	public void clearHistory()
	{
		
		webview.clearHistory();
	}
	public boolean canGoBack()
	{
		return webview.canGoBack();
	}
	
	public void goBack()
	{
		if(webview.canGoBack())
			webview.goBack();
		
	}
	public void goForward()
	{
		if(webview.canGoForward())
			webview.goForward();
	}
	
	public void Reload()
	{
		webview.reload();
	}

	public void loadDataWithBaseURL(String baseUrl, String data,
			String mimeType, String encoding, String historyUrl) {
		// TODO Auto-generated method stub
		webview.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
	}
	
	
	

}
