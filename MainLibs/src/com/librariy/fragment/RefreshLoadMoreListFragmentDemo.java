package com.librariy.fragment;

import java.util.List;

import android.view.View;

import com.librariy.base.MyBaseAdapter;
import com.librariy.reader.base.ReaderBase;

public class RefreshLoadMoreListFragmentDemo<B> extends
		RefreshLoadMoreListFragmentBase<B> {

	@Override
	protected void afterView(View v) throws Exception {
		// TODO Auto-generated method stub
		super.afterView(v);
		//设置分割线
//		  list.setDivider(getResources().getDrawable(R.drawable.plan_detail_list_line));
//	        int dp_10=DimenTool.dip2px(getActivity(), 10);
//	        list.setPadding(dp_10, 0, dp_10, 0);
		
		//不是Viewpager中嵌套多个的话 调用readData（）加载第一次数据
//	        readData();
	}
	
	@Override
	public MyBaseAdapter<B> getAdapter() {
		// TODO Auto-generated method stub
//		返回一个对应的MyBaseAdapter
//	     adapter = new MyBaseAdapter(getActivity());
//	        return adapter;
		return null;
	}

	@Override
	public ReaderBase getReader() {
		// TODO Auto-generated method stub
//		 return new ReaderBase(count + "", pageSize + "", "2");
		return null;
	}

	@Override
	public List<B> getDateList(ReaderBase reader) {
		// TODO Auto-generated method stub
//		ReaderBase re=(ReaderBase) reader;
//        return re.getDateList();
		return null;
	}

}
