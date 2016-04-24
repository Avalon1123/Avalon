package com.librariy.reader.base;

import android.content.Context;

public class ConnectionTastDemo {
    public static void main(String[] args) {
        Context context = null;
        ReaderBase re=new ReaderBase() {
            
            @Override
            public String getToken() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getPostUrl() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        new ConnectionTast(context).excute(re, new ReaderCallBack(context) {
            
            @Override
            public void doSuccess(ReaderBase reader) {
                // TODO Auto-generated method stub
                
            }
        });
    }
}
