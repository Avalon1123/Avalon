package com.librariy.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.librariy.adapter.DeleteBaseAdapter;
import com.librariy.view.DeleteView;
import com.librariy.view.DeleteView.OnOkClickListener;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckedTextView;
import cn.sharesdk.R;
import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

/**
 * 在原先的基础上加上了删除功能
 * 
 * @author 申
 * 
 */
@EFragment
public abstract class DeledeRefreshLoadMoreListFragmentBase<B> extends
		RefreshLoadMoreListFragmentBase<B> {
	@ViewById
	protected DeleteView delete;
	protected boolean isShowDelete = false;

	public boolean isShowDelete() {
		return isShowDelete;
	}

	@Override
	protected int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_delete_list;
	}

	public void updataDeleteView(boolean isShow) {
		if (delete != null) {
			delete.setVisibility(isShow ? View.VISIBLE : View.GONE);
			delete.check.setChecked(false);
		}
		
		if (!isShow) {
			updataSelected(isShow);
		}
		if (adapter != null && adapter instanceof DeleteBaseAdapter) {
			DeleteBaseAdapter a = (DeleteBaseAdapter) adapter;
			a.ChangeDelete(isShow);
		}
		isShowDelete = isShow;
		if(null!=swip_refresh)
			swip_refresh.setEnabled(!isShowDelete);
	}
	/**
	 * 更新每一条的选中状态
	 * 
	 * @param isSelected
	 */
	public void updataSelected(boolean isSelected) {
		for (B v : data) {
			if (null != v) {
				setChecked(v, isSelected);
			}
		}
		if (adapter != null && adapter instanceof DeleteBaseAdapter) {
			DeleteBaseAdapter a = (DeleteBaseAdapter) adapter;
			a.notifyDataSetChanged(data);
		}
	}
	/**
	 * 执行删除操作
	 */
	public abstract void delete();

	@Override
	protected void afterView(View v) throws Exception {
		// TODO Auto-generated method stub
		super.afterView(v);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (DeledeRefreshLoadMoreListFragmentBase.this.onLongClickListener != null) {
					DeledeRefreshLoadMoreListFragmentBase.this.onLongClickListener
							.onLongClick(arg1);
				}
				return false;
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if (adapter != null && adapter instanceof DeleteBaseAdapter) {
					DeleteBaseAdapter a = (DeleteBaseAdapter) adapter;
					final B b = adapter.getItem(position);
					if (isShowDelete) {
						((CheckedTextView) v.findViewById(R.id.check)).toggle();
						boolean isChecked=((CheckedTextView) v
								.findViewById(R.id.check)).isChecked();
						if(a.isCanDelete(b))
						{
							a.setChecked(b, isChecked);
							updataChecked(isChecked);
						}
							
					} else {
						a.itemClick(b);
					}
				}

			}
		});
		// list.setOnLongClickListener(new OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// // TODO Auto-generated method stub
		// if (onLongClickListener!=null) {
		// onLongClickListener.onLongClick(view);
		// }
		// return true;
		// }
		// });
		delete.setOnOkClickListener(new OnOkClickListener() {

			@Override
			public void ok() {
				// TODO Auto-generated method stub
				delete();
			}

			@Override
			public void onChange(boolean isChecked) {
				// TODO Auto-generated method stub
				updataSelected(isChecked);
			}
		});
	}
	/**
	 * 对实体进行赋值，更新实体的selected状态
	 * @param b
	 * @param bb
	 */
	public abstract void setChecked(B b, boolean bb);

	@Override
	public void initPager() {
		// TODO Auto-generated method stub
		super.initPager();
		if (delete != null)
			delete.check.setChecked(false);

	}

	public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
		this.onLongClickListener = onLongClickListener;
		// if (adapter!=null&&adapter instanceof DeleteBaseAdapter) {
		// DeleteBaseAdapter a=(DeleteBaseAdapter) adapter;
		// a.setOnLongClickListener(onLongClickListener);
		// }
	}

	public static DeledeRefreshLoadMoreListFragmentBase getInitFragment(
			DeledeRefreshLoadMoreListFragmentBase f,
			OnLongClickListener onLongClickListener) {
		if (null != f) {
			f.setOnLongClickListener(onLongClickListener);
			return f;
		}

		return null;
	}

	public OnLongClickListener onLongClickListener;
	// public void setOnLongClickListener(OnLongClickListener
	// onLongClickListener) {
	// this.onLongClickListener = onLongClickListener;
	// }
	public void updataChecked(boolean isChecked)
	{
		if (delete != null) {
			if (adapter != null && adapter instanceof DeleteBaseAdapter) {
				final DeleteBaseAdapter a = (DeleteBaseAdapter) adapter;
				
				
				if (!isChecked) {
					delete.check.setChecked(false);
				} else {
					int c=Linq4j.asEnumerable(data).count(new Predicate1<B>() {

						@Override
						public boolean apply(B arg0) {
							// TODO Auto-generated method stub
							
							return a.getIsChecked(arg0);
						}
					});
					int c_=Linq4j.asEnumerable(data).count(new Predicate1<B>() {
						
						@Override
						public boolean apply(B arg0) {
							// TODO Auto-generated method stub
							
							return a.isCanDelete(arg0);
						}
					});
					if (c_!=0) {
						delete.check.setChecked(c==c_);
					}
					
					
//					return count==
				}
			}

			
		}
		
	}
}
