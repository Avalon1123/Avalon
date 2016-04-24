package com.librariy.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.librariy.utils.Judge;
/**
 *  图片获取处理集合
 * @author 易申
 *
 */
public class PhotosBean implements Serializable,Cloneable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 图片路径
     */
    public ArrayList<String> paths=new ArrayList<String>();
    /**
     * 容量
     */
    public int maxSize=1;
    
    public boolean needCrop=false;
    
    public boolean isNeedCrop() {
		return needCrop;
	}
	public void setNeedCrop(boolean needCrop) {
		this.needCrop = needCrop;
	}

	public String TAG="";
    
    /**
     * 标题
     */
    public String title;
    
    /**
     * key
     */
    public String key;
    /**
     * 宽度
     */
    public int width=800;
    /**
     * 高度
     */
    public int higth=600;
    public PhotosBean ( String TAG, String title)
    {
        this.TAG=TAG;
        this.title=title;
    }
    public PhotosBean ( String TAG, String title,String key)
    {
    	this.TAG=TAG;
    	this.title=title;
    	this.key=key;
    }
    public PhotosBean ( String TAG, String title,String key,int w,int h,boolean needCrop)
    {
    	this.TAG=TAG;
    	this.title=title;
    	this.key=key;
    	setSize(w, h);
    	setNeedCrop(needCrop);
    }
    @Override 
    public Object clone() throws CloneNotSupportedException { 
//        PhotosBean b=null;
//        b=(PhotosBean) super.clone();
//        ArrayList<String> temp=new ArrayList<String>();
//        if (Judge.ListNotNull(paths)) {
//            for(String j:paths)
//            {
//                temp.add((String) j.clone());
//            }
//            b.paths=temp;
//        }
//        return b; 
        return  super.clone();
    } 
    public void  setSize(int w,int h) {
    	width=w;
    	higth=h;
	}
    public void copyTo(PhotosBean b)
    {
        Field[]f=PhotosBean.class.getFields();
        for(Field t:f)
        {
            if(Modifier.isFinal(t.getModifiers()) ) {
                continue;
            }
            try {
                t.set(this, t.get(b));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 获取当前图片数及容量  
     * PS：2/3
     * @return
     */
    public String getCapacity()
    {
        String s="";
        if (paths!=null) {
            s="("+paths.size()+"/"+maxSize+")";
        }
        
        return s;
    }
    /**
     * 获取当前剩余容量  
     * 
     * @return
     */
    public int getLeftCapacity()
    {
        int s=maxSize;
        if (paths!=null) {
            s=s-paths.size();
        }
        
        return s;
    }
    
    /**
     * 是否可以继续添加
     * @return
     */
    public boolean canAdd()
    {
        
        if(Judge.ListNotNull(paths))
            return maxSize>paths.size();
        return true;
    }
}
