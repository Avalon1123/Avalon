package com.librariy.bean.myinterface;

import com.librariy.reader.base.ReaderData;


public interface ReaderListener extends ReaderLinstenerBase{
    public void start();
    public void compelete();
    public void fail(ReaderData j,String code,String msg);
    public void exception(Object o);
}
