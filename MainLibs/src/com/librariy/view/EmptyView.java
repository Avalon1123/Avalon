package com.librariy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.R;


public class EmptyView extends LinearLayout {

	ImageView img;
	TextView lable;
	
	
	public EmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Intial();
	}

	public EmptyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		Intial();
	}
	
	
   public void Intial()
   {
	   LayoutInflater l=LayoutInflater.from(getContext());
	   l.inflate(R.layout.view_empty, this);
	   img=(ImageView) this.findViewById(R.id.empty_img);
	   lable=(TextView) findViewById(R.id.emptyText);
	   
   }
   
   
   public void setEmptyImageSource(int id)
   {
	   img.setImageResource(id);
   }
   
   public void setEmptyText(String text)
   {
	   lable.setText(text);
   }
   
	
	

}
