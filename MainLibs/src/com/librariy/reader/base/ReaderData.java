package com.librariy.reader.base;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * 返回数据解析集合
 * @author 易申
 *IsSuccess、ErrorMessage、Datas、TotalRecords,Data
 */
public class ReaderData {
	/**
	 * 单条数据集
	 */
	public JSONObject data;
	/**
	 * 多条数据集合
	 */
	public JSONArray datas ;
	/**
	 * 是否成功取得数据
	 */
	public boolean isSuccess;
	/**
	 * 失败时错误信息
	 */
	public String errorMessage;
	/**
	 * 取分页的信息时，分页信息的总条数
	 */
	public int totalRecords;
}
