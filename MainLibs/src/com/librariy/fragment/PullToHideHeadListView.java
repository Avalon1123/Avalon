package com.librariy.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class PullToHideHeadListView extends RefreshLoadMoreListFragmentBase {
	public View headView;
	public abstract View GetHeadView();
	
	@Override
	protected void afterView(View v) throws Exception {
		// TODO Auto-generated method stub
		headView=GetHeadView();
		if(null!=headView)
		{
			list.addHeaderView(headView);
		}
//		list.geto
		list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (null!=headView) {
					if (scrollState==SCROLL_STATE_FLING) {
						headView.setVisibility(View.GONE);
					} else {
						headView.setVisibility(View.VISIBLE);
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		super.afterView(v);
	}
}
