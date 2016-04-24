package com.librariy.view;



import com.librariy.utils.IHttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HijackWebView extends WebView {


	IHttp listener;
	IHijackPost hijackPostListener;
	
	public void setHijackPostListener(IHijackPost hijackPostListener) {
		this.hijackPostListener = hijackPostListener;
	}

	public void setProceesChangeListener(IHttp listener) {
		this.listener = listener;
//		this.addJavascriptInterface(new Object(), interfaceName)
	}

	
	
	public HijackWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Intial();
		// TODO Auto-generated constructor stub
	}

	public HijackWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Intial();
		// TODO Auto-generated constructor stub
	}

	public HijackWebView(Context context) {
		super(context);
		Intial();
		
		// TODO Auto-generated constructor stub
	}
	
	
	
	public void  loadString(String s,String url)
	{
		this.getSettings().setDefaultTextEncodingName("UTF-8") ;
		this.loadDataWithBaseURL(url, s, "text/html", "UTF-8", null);
	}
	
	public void loadUrlWithFilter(String url) {		
	
	}
	
	
	
	
	
	
	
	
	
	
	public void Intial()
	{
		setWebViewClient(null);
		setWebChromeClient(null);
	}

	@Override
	public void setWebChromeClient(WebChromeClient client) {
		// TODO Auto-generated method stub
		super.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if(listener!=null)
					 listener.OnPostProcess(newProgress);
			}
			
		});
	}
	
	
	/**
	 * ��������
	 * @return
	 */
	protected boolean HijackPost()
	{
		return false;
	}
	
	
	@Override
	public void setWebViewClient(WebViewClient client) {
		// TODO Auto-generated method stub
//		super.setWebViewClient(client);
		super.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if(hijackPostListener!=null)
				{
				   if(hijackPostListener.Hijack(url))				   
				   {
					   return true;
				   }
				}
				if(url!=null)
					{
					  return super.shouldOverrideUrlLoading(view, url);
					}
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				if(listener!=null)
					 listener.OnPostCompelete();
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
			
				if(listener!=null)
					 listener.OnPostStart();
			}
			
		});
	}
	
	
	public  static interface IHijackPost
	{
	     public boolean Hijack(String url);
	}
	
	
	
	
	OnHijackStringListener onHijackStringListener;
	public void setOnHijackStringListener(OnHijackStringListener onHijackStringListener) 
	{
		this.onHijackStringListener = onHijackStringListener;
	}


	public static interface OnHijackStringListener
	{
		public String OnHijack(String s);
		
	}
	
	
	
	

}
