package com.librariy.utils;

import java.util.LinkedList;
import java.util.List;

import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;
/**
 * 在textView显示不同颜色文字
 * @author 易申
 *
 */
public class HtmlTool {
	/***
	 * 给TextView赋值，防止空  如果文字为空 默认“”
	 * @param v
	 * @param s
	 */
	public static void setText(TextView v,String s)
	{
		setText(v, s, "");
	}
	/**
	 * 
	 * @param v textview
	 * @param value  显示文字
	 * @param def 文字为空时默认文字
	 */
	public static void setText(TextView v,String value,String def)
	{
		if (v!=null) {
			if (Judge.StringNotNull(value)) {
				v.setText(value);
			}else {
				v.setText(def+"");
			}
		}
	}
	List<Ct> order=new LinkedList<Ct>();	
	public  Spanned ToSpannedText()
	{
		String s="";
		for(Ct ct:order)		
		{
			if (ct.isEnte) {
				s+="<br></br>";
			} else {
				s+="<font "+ct.color+">"+ct.text+"</font>";
			}
			
			
		}
//		Log.i("s",s);
		
		return Html.fromHtml(s);

	}
	
	public void AddColor(String color,String text)
	{						
		String v=" color='"+color+"' ";
		Ct ct=	new Ct();
		ct.color=v;
		ct.text=text;
		order.add(ct);
		
	}
	public void AddEnter()
	{						
		Ct ct=	new Ct();
		ct.isEnte=true;
		order.add(ct);
		
	}
	
	public class Ct
	{
		
		public String text;
		public String color;
		public boolean isEnte=false;
	}
	
	

	
	
	
	
}
