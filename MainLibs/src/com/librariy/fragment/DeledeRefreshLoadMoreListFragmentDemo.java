package com.librariy.fragment;

import java.util.List;

import android.view.View;

import com.librariy.base.MyBaseAdapter;
import com.librariy.reader.base.ConnectionTast;
import com.librariy.reader.base.ReaderBase;
import com.librariy.reader.base.ReaderCallBack;
import com.librariy.utils.DimenTool;

public class DeledeRefreshLoadMoreListFragmentDemo<B> extends
		DeledeRefreshLoadMoreListFragmentBase<B> {

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
	public void delete() {
		// TODO Auto-generated method stub
		
		//执行对应的删除操作
		
//        String s = "";
//        for (int i = 0; i < data.size(); i++) {
//            if (data.get(i).isSelect) {
//                s = s + data.get(i).getId() + ",";
//            }
//
//        }
//
//        if (!s.equals("")) {
//            new ConnectionTast(getActivity()).excute(new DeleteMsgReader(s.substring(0, s.length() - 1)), new ReaderCallBack() {
//
//                @Override
//                public void doSuccess(com.librariy.reader.base.ReaderBase reader) {
//                    // TODO Auto-generated method stub
//                    UIHelper.showToast(getActivity(), "删除成功！");
//                    restart();
//
//                }
//            });
//        } else {
//            UIHelper.showToast(getActivity(), "您还未勾选任何选项喔！");
//
//        }
	}

	@Override
	public void setChecked(B b, boolean bb) {
		// TODO Auto-generated method stub
		//对对应的model赋值，更新其选中状态
		
//		b.isSelect=bb;
	}

	@Override
	public MyBaseAdapter<B> getAdapter() {
		// TODO Auto-generated method stub
//		返回一个对应的DeleteBaseAdapter
//	     adapter = new DeleteBaseAdapter(getActivity());
//	        return adapter;
		return null;
	}

	@Override
	public ReaderBase getReader() {
		// TODO Auto-generated method stub
		//对应的请求数据的方法
//		 return new ReaderBase(count + "", pageSize + "", "2");
		return null;
	}

	@Override
	public List<B> getDateList(ReaderBase reader) {
		// TODO Auto-generated method stub
		//返回对应的 List<B> 数据
		
//		ReaderBase re=(ReaderBase) reader;
//        return re.getDateList();
		return null;
	}
	 @Override
	    public int getPageSize() {
	        // TODO Auto-generated method stub
		 //每次请求条数
	        return 15;
	    }
}
