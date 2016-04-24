package com.librariy.adapter;

import com.librariy.utils.Judge;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class DeleteBaseAdapterDemo<B> extends DeleteBaseAdapter<B> {

	public DeleteBaseAdapterDemo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getViewId() {
		// TODO Auto-generated method stub
		return 0;//布局文件ID
	}

	@Override
	public void itemClick(B item) {
		// TODO Auto-generated method stub
// Item 的单机时间，长按事件已封装为触发勾选删除
		
//		  Intent intent = new Intent(context, MyOrderDetailActivity.class);
//          intent.putExtra("plan", item);
//          startActivity(intent);
	}

	@Override
	public void updataView(com.librariy.adapter.DeleteBaseAdapter.Item item, B b) {
		// TODO Auto-generated method stub
		
	//  根据每一项的值更新Item
		
//		Item_ item=new Item_();
//		item.check=(CheckedTextView) convertView.findViewById(R.id.check);
//		item.title=(TextView) convertView.findViewById(R.id.title);
//		item.money=(TextView) convertView.findViewById(R.id.money);
//		item.status=(TextView) convertView.findViewById(R.id.status);
//		item.date=(TextView) convertView.findViewById(R.id.date);
//		item.time=(TextView) convertView.findViewById(R.id.time);
//		return item;

	}

	@Override
	public com.librariy.adapter.DeleteBaseAdapter.Item initView(int position,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	//  根据找到每个Item 的Id、
//		Item_ item=(Item_) arg0;
//		item.title.setText(arg1.getName()+"");
//		item.money.setText("¥"+arg1.getAllAmount()+"元");
//		 item.date.setText(arg1.getCreateAt()+"");
//         views.put(item.time, arg1);
//         // 根据状态来判断两个状态tv的显示
//         if ("unpay".equals(arg1.getStatus())) {
//        	
//             item.time.setVisibility(View.VISIBLE);
//             item.status.setBackgroundResource(R.drawable.red_round_button_small_selector);
//             item.status.setTextColor(white);
//         } else if ("paid".equals(arg1.getStatus())) {
//        	   item.time.setVisibility(View.GONE);
//        	 if ("已兑付".equals(arg1.StatusStr)) {
//        		 item.status.setBackgroundResource(R.drawable.round_line_kuang_green_light);
//                 item.status.setTextColor(green);
//			} else if ("延期兑付".equals(arg1.StatusStr)) {
//	             item.status.setBackgroundResource(R.drawable.round_line_kuang_orange_light);
//	             item.status.setTextColor(orange);
//			}else {
//				 item.status.setBackgroundResource(R.drawable.round_line_kuang_blue_light);
//                 item.status.setTextColor(blue);
//			}
//            
//         } else if ("failed".equals(arg1.getStatus())) {
//        	 item.time.setVisibility(View.GONE);
//             item.status.setBackgroundResource(R.drawable.round_line_kuang_gray_light);
//             item.status.setTextColor(gray);
//         }
//         if (Judge.StringNotNull(arg1.StatusStr)) {
//        	  item.status.setText(arg1.StatusStr+"");
// 		}
		return null;
	}

	@Override
	public boolean getIsChecked(B b) {
		// TODO Auto-generated method stub
		//判断是否勾选
		
//		return b.checked;
		return false;
	}

	@Override
	public void setChecked(B b, boolean bb) {
		// TODO Auto-generated method stub
		//跟上面的方法对应，给勾选的bean赋值
		
//		b.checked=bb;
	}
	@Override
	public boolean isCanDelete(B item) {
		// TODO Auto-generated method stub
		//判断每一项是否可以勾选 例如订单的只有失败的菜可以勾选
		
//		if (null!=item) {
//			return "failed".equals(item.getStatus());
//		}
//		return false;
		return true;
	}
}
