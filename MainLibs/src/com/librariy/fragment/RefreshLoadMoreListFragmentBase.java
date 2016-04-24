package com.librariy.fragment;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.librariy.annotactions.AnnotationsFragmentBase;
import com.librariy.base.MyBaseAdapter;
import com.librariy.reader.base.ReaderBase;
import com.librariy.reader.base.ReaderTast;
import com.librariy.utils.Judge;
import com.librariy.view.EmptyView;
import com.librariy.view.LoadMoreListView;
import com.librariy.view.LoadMoreListView.OnLoadMoreListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import cn.sharesdk.R;
/**
 * 上拉加载更多  ，下拉刷新  listview显示
 * @author 易申
 *
 */
@EFragment
public abstract class RefreshLoadMoreListFragmentBase<B> extends AnnotationsFragmentBase {

   
   protected int count=0;
   protected int pageSize=getPageSize();
   protected int pageIndex=1;
   protected boolean isRestart=false;
    @ViewById 
    protected SwipeRefreshLayout swip_refresh;
    @ViewById
    protected EmptyView empty;
    protected boolean hasMore=true;
    protected boolean needToast=isNeedToast();
    public boolean isNeedToast() {
		return true;
	}

	public void setNeedToast(boolean needToast) {
		this.needToast = needToast;
	}
	protected  boolean needLoadingDialog;
    public void setNeedLoadingDialog(boolean needLoadingDialog) {
        this.needLoadingDialog = needLoadingDialog;
    }
    protected MyBaseAdapter<B> adapter;
    public List<B> data=new ArrayList<B>();
    @ViewById 
    protected LoadMoreListView list;
    
    public LoadMoreListView getListView() {
		return list;
	}

	@Override
    protected void afterView(View v) throws Exception {
        // TODO Auto-generated method stub
        needLoadingDialog=isNeedLoadingDialog();
        if(adapter==null)
        	adapter=getAdapter();
        if(adapter!=null)
            list.setAdapter(adapter);
        if (isUpLoadingMore()) {
            list.setOnLoadMoreListener(new OnLoadMoreListener() {
                
                @Override
                public void onLoadMore() {
                    // TODO Auto-generated method stub
                    toLoading();
                }
            });
        }
       if (isPullToRefresh()&&swip_refresh!=null) {
           swip_refresh.setOnRefreshListener(new OnRefreshListener() {
            
            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                try {
                    restart(false);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    if(swip_refresh!=null)
                        swip_refresh.setRefreshing(false);
                }
               
            }
        });
    }else {
        if(swip_refresh!=null)
            swip_refresh.setEnabled(false);
    }
    
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected int getViewId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_refresh_load_more_base;
    }
    /**
     * 获取 adapter
     * @return
     */
    public abstract MyBaseAdapter<B> getAdapter();
    /**
     * 一次访问获取多少条数据
     * @return
     */
    public  int getPageSize()
    {
        return 20;
    }
    /**
     * 是否下拉刷新
     * @return
     */
    public boolean isPullToRefresh()
    {
        return true;
    }
    /**
     * 加载时是否需要加载dialog
     * @return
     */
    public boolean isUpLoadingMore()
    {
        return true;
    }
    /**
     * 加载时是否需要加载dialog
     * @return
     */
    public boolean isNeedLoadingDialog()
    {
        return true;
    }
    /**
     * 初加载读取数据
     */
    public void readData()
    {
        if(data==null)
            data=new ArrayList<B>();
        setNeedLoadingDialog(true);
        if (data.size()<1) {
        
            toLoading();
        }else {
            if(adapter!=null)
                adapter.notifyDataSetChanged(data);
        }
    }
    public void restart()
    {
        restart(true);
    }
    public void restart(boolean needDialog)
    {
        setNeedLoadingDialog(needDialog);
//        data.clear();
        count=0;
        pageIndex=1;
        isRestart=true;
        toLoading();
    }
    /**
     * 获取访问 接口
     * @return
     */
    public abstract ReaderBase getReader();
    /**
     * 获取接口返回数据
     * @return
     */
    public abstract List<B> getDateList(com.librariy.reader.base.ReaderBase reader);
    /**
     * 页码 计算
     */
    public void initPager()
    {
        count=data.size();
        pageIndex++;
        if (count>0&&needToast) {
			toast("当前共加载数据"+count+"条");
		}
    }
    /**
     * 获取数据
     */
    public void toLoading()
    {
        Loading re=new Loading();
        re.execute();
    }
    /**
     * 发送请求
     * @author 易申
     *
     */
    class Loading extends ReaderTast
    {
        @Override
        public void doStart() {
            // TODO Auto-generated method stub
            super.doStart();
             if(isPullToRefresh()&&swip_refresh!=null)
                 swip_refresh.setRefreshing(true);
             else if(needLoadingDialog)
          {
              dismissDialog();
              showDialog();
          }
                
        }
        @Override
        public com.librariy.reader.base.ReaderBase doReader(String... params)
                throws SocketTimeoutException, IOException, Exception {
            // TODO Auto-generated method stub
            return getReader();
        }

        @Override
        public void doSuccess(com.librariy.reader.base.ReaderBase reader) {
            // TODO Auto-generated method stub
            List<B> temp=getDateList(reader);
            if (Judge.ListNotNull(temp)) {
            	reading(reader);
                if (isRestart) {
                    data=temp;
                } else {
                    data.addAll(temp);
                }
               
                initPager();
                if (count<reader.data.totalRecords) {
                    hasMore=true;
                }else {
                    hasMore=false;
                }
                
            } else {
            	 if (isRestart)
            		 data.clear();
                hasMore=false;
            }
            if(adapter!=null)
                adapter.notifyDataSetChanged(data);
            
            if(list!=null)
                list.setHasMore(hasMore);
        }
        @Override
        public void doComplete(com.librariy.reader.base.ReaderBase reader) {
            // TODO Auto-generated method stub
            super.doComplete(reader);
            isRestart=false;
          if(needLoadingDialog)
              dismissDialog();
             if(swip_refresh!=null)
                 swip_refresh.setRefreshing(false);
            if(list!=null)
                list.onLoadMoreComplete();
            if (empty!=null) {
                if (data==null||data.size()==0) {
                	if(list!=null)
                		list.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }else {
                	if(list!=null)
                		list.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
            }
        }
        @Override
        public void toast(String msg) {
            // TODO Auto-generated method stub
            RefreshLoadMoreListFragmentBase.this.toast(msg);
        }
        @Override
        public Context getContext() {
            // TODO Auto-generated method stub
            return getActivity();
        }
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    public void reading(com.librariy.reader.base.ReaderBase reader)
    {
    	
    }
}
