package com.librariy.utils;

public interface IHttp {

	public void OnPostProcess(int newProgress);
	
	public void OnException(Exception e);
	public void OnPostStart();
	
	
	public void OnPostCompelete();
}
