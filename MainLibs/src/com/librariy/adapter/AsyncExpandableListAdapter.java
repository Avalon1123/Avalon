package com.librariy.adapter;
import com.librariy.bean.TreeDataSet;
import com.librariy.view.RotateRingView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import cn.sharesdk.R;

/**
 * 内部类： MyExpandableListAdapter 作用：用于分组显示的ListViewAdapter
 */
public abstract class AsyncExpandableListAdapter<G,C> extends BaseExpandableListAdapter {
    private static final String TAG="GroupListAdapter";
    private Activity mActivity;
    private TreeDataSet<Object,TreeDataSet<G,C>> mRootData = new TreeDataSet<Object,TreeDataSet<G,C>>(null);

    public AsyncExpandableListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }
    protected void setTotalGroupCount(int totalGroupCount) {
        mRootData.setTotalCount(totalGroupCount);
    }
    protected void setTotalChildCount(int groupPosition,int totalChildCount) {
        TreeDataSet<G,C> parent=mRootData.getChildAt(groupPosition);
        if(parent==null) return;
            parent.setTotalCount(totalChildCount);
    }
    protected void resetToGroupPostion(int startIndex ) {
        mRootData.resetToPostion(startIndex);
    }
    protected TreeDataSet<G,C> addGroupItem(G gItem) {
        TreeDataSet<G,C> retObj=new TreeDataSet<G,C>(gItem);
        mRootData.addChildren(retObj);
        return retObj;
    }
    protected boolean resetToChildrenPostion(int groupPosition,int startIndex ) {
        TreeDataSet<G,C> parent=mRootData.getChildAt(groupPosition);
        if(parent==null){
            return false;
        }else{
            parent.resetToPostion(startIndex);
            return true;
        }
    }
    protected C addChildrenItem(int groupPosition, C cItem) {
        TreeDataSet<G,C> parent=mRootData.getChildAt(groupPosition);
        if(parent==null){
            return null;
        }else{
            parent.addChildren(cItem);
            return cItem;
        }
    }

    @Override
    public int getGroupCount() {
        int totalCount=mRootData.getTotalCount();
        int realCount= mRootData.getRealCount();
        if(realCount<totalCount){
            Log.d(TAG, String.format("getGroupCount(), realCount=%s, totalCount=%s, retCount=%s", realCount,totalCount,realCount+1));
            return realCount+1;
        }else{
            Log.d(TAG, String.format("getGroupCount(), realCount=%s, totalCount=%s, retCount=%s", realCount,totalCount,realCount));
            return realCount;
        }
    }
    @Override
    public G getGroup(int groupPosition) {
        TreeDataSet<G,C> g=mRootData.getChildAt(groupPosition);
        return g==null?null:g.mHolder;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        TreeDataSet<G,C> parent=mRootData.getChildAt(groupPosition);
        if (parent == null) {
            return 0;
        }
        int totalCount=parent.getTotalCount();
        int realCount= parent.getRealCount();
        if(realCount<totalCount){
            Log.d(TAG, String.format("getChildrenCount(), realCount=%s, totalCount=%s, retCount=%s", realCount,totalCount,realCount+1));
            return realCount+1;
        }else{
            Log.d(TAG, String.format("getChildrenCount(), realCount=%s, totalCount=%s, retCount=%s", realCount,totalCount,realCount));
            return realCount;
        }
    }

    @Override
    public TreeDataSet.TreeChild<G, C> getChild(int groupPosition, int childPosition) {
        TreeDataSet<G,C> parent=mRootData.getChildAt(groupPosition);
        C child=parent.getChildAt(childPosition);
        return (parent != null&&child!=null) ? new TreeDataSet.TreeChild<G, C>(parent.mHolder,child): null;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {        
        if (getGroupType(groupPosition) == 1) {
            Log.d(TAG, String.format("getGroupView(), groupPosition=%s, isExpanded=%s, item_loading_more", groupPosition,isExpanded));
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_loading_more, null);
            }
            this.onGroupLoading(groupPosition);
            return convertView;
        } else {
            Log.d(TAG, String.format("getGroupView(), groupPosition=%s, isExpanded=%s, item=%s", groupPosition,isExpanded,getGroup(groupPosition)));
            return createGroupView(groupPosition,convertView);
        }
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (getChildType(groupPosition, childPosition) == 1) {
            Log.d(TAG, String.format("getChildView(), groupPosition=%s, childPosition=%s, isLastChild=%s, item_loading_more", groupPosition,childPosition,isLastChild));
            G g = this.getGroup(groupPosition);
            if (convertView == null) {
                convertView = new LoaddingView(mActivity);
            }
            this.onChildLoading(groupPosition, childPosition,g);
            return convertView;
        } else {
            Log.d(TAG, String.format("getChildView(), groupPosition=%s, childPosition=%s, isLastChild=%s, item=%s", groupPosition,childPosition,isLastChild,getChild(groupPosition, childPosition)));
            return this.createChildView(groupPosition,childPosition,convertView);
        }
    }
    @Override
    public int getGroupType(int groupPosition) {
        return mRootData.isLoaded(groupPosition)?0:1;       
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        TreeDataSet<G,C> parent=mRootData.getChildAt(groupPosition);
        if(parent!=null)
            Log.d(TAG, String.format("getChildType(), groupPosition=%s, childPosition=%s,  parent.getRealCount()=%s, parent.getTotalCount()=%s",groupPosition,childPosition, parent.getRealCount(),parent.getTotalCount()));
        return (parent==null||parent.isLoaded(childPosition))?0:1;
    }
    @Override
    public int getGroupTypeCount() {
        return 2;
    }
    @Override
    public int getChildTypeCount() {
        return 2;
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    protected abstract View createGroupView(int groupPosition, View convertView);
    protected abstract View createChildView(int groupPosition,int childPosition,View convertView);
    public abstract void onGroupLoading(int groupPosition);
    public abstract void onChildLoading(int groupPosition, int childPosition,G gItem);
    private static class LoaddingView extends FrameLayout {
        public LoaddingView(Context context) {
            super(context);
            int padding = (int)(context.getResources().getDisplayMetrics().density*5);
            super.setPadding(padding, padding, padding, padding);
            super.addView(new RotateRingView(getContext()), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.TOP));
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (getParent() instanceof ListView) {
                ListView p = (ListView) getParent();
                int maxHeight = p.getMeasuredHeight() - p.getPaddingTop() - p.getPaddingBottom() - p.getDividerHeight();
                for (int i = 0; i < p.getChildCount(); i++) {
                    View c = p.getChildAt(i);
                    if (c == this)
                        continue;
                    maxHeight -= c.getMeasuredHeight();
                }
                if (maxHeight > 0) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}