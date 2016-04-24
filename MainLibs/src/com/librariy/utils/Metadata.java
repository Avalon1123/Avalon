package com.librariy.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Metadata {
    private static final String TAG=Metadata.class.getSimpleName();
    private static final String SPLIT="###";
    private static LinkedHashMap mData=new LinkedHashMap();
    static{
        try {
            BufferedReader in=new BufferedReader(new InputStreamReader(Metadata.class.getResourceAsStream("Metadata.bin"),"UTF-8"));         
            String line=null;
            while((line=in.readLine())!=null){
                String paths[]=line.split(SPLIT);
                LinkedHashMap temp=mData;
                for(String path:paths){
                    if(!temp.containsKey(path)){
                        temp.put(path,new LinkedHashMap());
                    };
                    temp=(LinkedHashMap)temp.get(path);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> getChildren(String... paths){
        ArrayList<String> retArrays=new ArrayList<String>();
        try {
            LinkedHashMap temp=mData;
            for(String path:paths){
                if(!temp.containsKey(path)){
                    return retArrays;
                }
                temp=(LinkedHashMap)temp.get(path);
            }
            retArrays.addAll(temp.keySet());
            return retArrays;
        } catch (Exception e) {
            System.out.println("getNames()");
            return retArrays;
        }
    }
    public static ArrayList<String> getExtendChildren(String mExtendItem,String... paths){
        ArrayList<String> retArrays=Metadata.getChildren(paths);
        if(mExtendItem!=null){
            retArrays.add(0,mExtendItem);
        }
        return retArrays;
    }
    public static ArrayList<String> getChildrenWithEmptyString(String mEmptyString,String... paths){
        ArrayList<String> retArrays=new ArrayList<String>();
        try {
            LinkedHashMap temp=mData;
            for(String path:paths){
                if(!temp.containsKey(path)){
                	 retArrays.add(0,mEmptyString);
                	 return retArrays;
                }
                temp=(LinkedHashMap)temp.get(path);
            }
            retArrays.addAll(temp.keySet());
        } catch (Exception e) {
            System.out.println("getNames()");
        }
        retArrays.add(0,mEmptyString);
        return retArrays;
    }
    public static int getHouseResourceType(String mHouseResourceType){
        int mHouseResourceType_I=-1;
        if("新房".equals(mHouseResourceType)){
            mHouseResourceType_I=1;
        }else if("二手房".equals(mHouseResourceType)){
            mHouseResourceType_I=2;
        }else if("租房".equals(mHouseResourceType)){
            mHouseResourceType_I=4;
        }else if("厂房".equals(mHouseResourceType)){
            mHouseResourceType_I=8;
        }else if("写字楼".equals(mHouseResourceType)){
            mHouseResourceType_I=16;
        }else if("商铺".equals(mHouseResourceType)){
            mHouseResourceType_I=32;
        }else if("仓库".equals(mHouseResourceType)){
            mHouseResourceType_I=128;
        }else{
            mHouseResourceType_I=-1;
        }
        return mHouseResourceType_I;
    }
    
    public static void main(String[] args) {
        System.out.println(Metadata.getExtendChildren("全部","RootData1"));
        System.out.println(Metadata.getExtendChildren("全部","RootData1","武汉市","全部"));
        System.out.println(Metadata.getChildrenWithEmptyString("全部","RootData1","武汉市","硚口"));
        System.out.println(Metadata.getExtendChildren("全部","RootData2","买","新房"));
    }
}
