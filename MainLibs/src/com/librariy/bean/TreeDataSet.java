package com.librariy.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class TreeDataSet<P, C> implements Serializable {
    public final P mHolder;
    private int totalCount = 1;
    private ArrayList<C> children = new ArrayList<C>();

    public TreeDataSet(P mHolder) {
        this.mHolder = mHolder;
    }

    public C getChildAt(int index) {
        return (index < children.size()) ? children.get(index) : null;
    }

    public boolean isLoaded(int postion) {
        if (totalCount <= 0) {
            return true;
        } else if (postion < children.size() || postion >= totalCount) {
            return true;
        } else {
            return false;
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getRealCount() {
        return children.size();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void addChildren(C item) {
        children.add(item);
    }

    public void resetToPostion(int startIndex) {
        // eg: children.size()=9, startIndex=10;
        while (children.size() < startIndex) {
            children.add(null);
        }
        // eg: children.size()=11, startIndex=10;
        while (startIndex < children.size() && !children.isEmpty()) {
            children.remove(children.size() - 1);
        }
    }

    public static class TreeChild<P, C> {
        public final P parent;
        public final C self;
        public TreeChild(P parent, C self) {
            this.parent=parent;
            this.self=self;
        }
    }
}
