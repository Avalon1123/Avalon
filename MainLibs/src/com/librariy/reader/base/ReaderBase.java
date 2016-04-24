package com.librariy.reader.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.librariy.http.AjaxParams;
import com.librariy.net.HttpRequest.FieldItem;
import com.librariy.utils.Judge;

import android.text.TextUtils;

/**
 * 数据解析base类  
 * 需在自己项目实现getToken();和getToken（）
 * 默认 post请求 可以在getMethod（）中修改
 * 默认带token 验证 可以在hasToken（）中修改
 * @author 易申
 *
 */
public abstract class ReaderBase {
     HashMap<String, String> params=new HashMap<String, String>();
     HashMap<String, String> headMap=new HashMap<String, String>();
//     HashMap<String, String> fileMap=new HashMap<String, String>();
     protected ArrayList<FieldItem> fileMap = new ArrayList<FieldItem>();
	public HashMap<String, String> getParams() {
        return params;
    }
	
    public static final String TOKEN = "Authorization";
	/**
	 * 服务器URL地址
	 */
	public static ComplieMode complieMode;
	/**
	 * 返回原始字符串，用作缓存
	 */
	public String originData;
	/**
	 * 连接地址后缀(接口地址)
	 */
	public String name="";
	/**
	 * 返回数据集合封装bean
	 */
	public ReaderData data;
	/**
	 * 用于api中有name的，apiStrBeforeName为前面一部分，apiStrAfterName为后面一部分
	 * 例如 /api/push/{username}/call apiStrBeforeName为push/  apiStrAfterName为/call
	 */
	protected String apiStrBeforeName,apiStrAfterName;
	/**
	 * api里面是否需要传user
	 */
	protected boolean hasUser=false;
	public void setName(String name) {
		this.name = name;
	}

	public ReaderBase (String name){
		this.name=name;
		hasUser =false;
	}
	
	public enum Method {
		POST, GET, PUT, DELETE
	}
	public ReaderBase () {
	}
	
	public ReaderBase(String apiUrl,Method method)
	{
		
	}
	
	/**
	 * 接口地址 
	/**
	 * 用于api中有name的，apiStrBeforeName为前面一部分，apiStrAfterName为后面一部分
	 * 例如 /api/push/{username}/call apiStrBeforeName为push/  apiStrAfterName为/call
	 */
	public ReaderBase(String apiStrBeforeName,String apiStrAfterName)
	{
		this.apiStrBeforeName=apiStrBeforeName;
		this.apiStrAfterName=apiStrAfterName;
		hasUser =true;
	}
	/**
	 * 进行数据解析
	 * @param s 服务器返回原始字符串
	 * @throws Exception
	 */
	public void initData(String s) throws Exception
	{
		originData=s;
		if(TextUtils.isEmpty(s))
		{
			return;
		}
		
		JSONObject j;
		try {
			 j=new JSONObject(s);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		try {
			data = new ReaderData();
			if(j.has("Data")&&!j.isNull("Data"))
				data.data = j.optJSONObject("Data");
			if(j.has("Datas")&&!j.isNull("Datas"))
				data.datas = j.optJSONArray("Datas");
			if(j.has("ErrorMessage"))
				data.errorMessage=j.optString("ErrorMessage","");
			data.isSuccess=j.getBoolean("IsSuccess");
			if(j.has("TotalRecords")&&!j.isNull("TotalRecords"))
				data.totalRecords=j.getInt("TotalRecords");
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			throw new Exception("errorMessage or isSuccess has wrong");
		}
	}
	/**
	 * 没有实体。直接连接后面带？key=value
	 * @param parms ？后面字符串参数
	 */
	public void init(String apiStrBeforeName,String parms)
	{
//		init(getUrl()+URLEncoder.encode(parms), null,  getMethod());
		init(getUrl()+parms, null,  getMethod());
	}
	/**
	 * 没有实体。直接连接后面带？key=value
	 * @param parms ？后面字符串参数
	 */
	public void init(String parms)
	{
		init(getUrl()+parms, null,  getMethod());
	}
	/**
	 * 有实体 参数以map方式传递
	 * 或者GET请求拼字符串，已做处理
	 * @param parms
	 */
	public void init(Map<String, String> parms)
	{
		if (getMethod()==Method.GET) {
			init(getUrl()+getGetParms(parms), null,  getMethod());
		} else {
			init(getUrl(), parms,  getMethod());
		}
		
	}
	private String getGetParms(Map<String, String> parms) {
		String s="";
		if (Judge.MapNotNull(parms)) {
			s+="?";
			for(String k:parms.keySet())
			{
				if(Judge.StringNotNull(k))
				{
					s+=k+"=";
					String v=parms.get(k);
					if (Judge.StringNotNull(v)) {
						s+=v+"&";
					} else {
						s+="&";
					}
				}
				
			}
			s=s.substring(0, s.length()-1);
		}
		return s;
	}
	/**
	 * 记录网络连接参数
	 * @param url 连接地址
	 * @param paramMap 参数map
	 * @param method 请求方式默认post
	 */
	public void init(String url, Map<String, String> paramMap,
			 Method method)
	{
		
		finalUrl=url;
		if (null != paramMap) {
			ajaxParams =new AjaxParams();
//			try {
//                resolveFile();
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
			for (Map.Entry me : paramMap.entrySet()) {
				String key = (String) me.getKey();
				String value = (String) me.getValue();
				ajaxParams.put(key, value);
				params.put(key, value);
			}
		}


	}
	AjaxParams ajaxParams;
	/**
	 * 最终Url
	 */
	String finalUrl="";
	
	public String getFinalUrl() {
        return finalUrl;
    }

	public String getUserTag()
	{
		return "{username}";
		
	}
	/**
	 * 
	 * @return 验证token
	 */
	public abstract String getToken();
	/**
	 * 是否有token验证,默认有
	 * @return
	 */
	public boolean hasToken()
	{
		return true;
	}
	/**
	 * 封装头信息
	 * @return 头信息map
	 */
	public Map<String, String> getHeadMap()
	{
//		if (!headMap.containsKey("Content-Type")) {
//			headMap.put("Content-Type", "application/x-www-form-urlencoded");
//		}
//		
//		headMap.put("Accept", "application/json");
		if (hasToken()&&!TextUtils.isEmpty(getToken())) {
			headMap.put(TOKEN, getToken());
		}
		
		return headMap;
	}
	public String getContentType()
	{
		return "application/x-www-form-urlencoded";
	}
	/**
	 * 网络请求链接地址
	 * @return
	 */
	public String getUrl()
	{
		return getPostUrl()+name;
	}
	/**
	 * method 请求方式默认post
	 * @return
	 */
	public Method getMethod()
	{
		return Method.POST;
	}
	/**
	 * 服务器地址
	 * @return
	 */
	public abstract String getPostUrl();
	/**
	 * 
	 * @return 错误信息
	 */
	public String getErrorMessage()
	{
		
		if(data==null||TextUtils.isEmpty(data.errorMessage))
			return "服务器异常";
		return data.errorMessage;
		
	}
	/**
	 * 请求是否成功
	 * @return
	 */
	public boolean getIsSuccess()
	{
		if(data!=null)
			return data.isSuccess;
		return false;
	}
	
	public ArrayList<FieldItem> getFileMap ()
	{
	    
	    return fileMap;
	}
//	public void resolveFile() throws FileNotFoundException
//	{
//	    HashMap<String , String> files= getFileMap();
//	    if (Judge.MapNotNull(files)) {
//	        for(String s:files.keySet())
//	        {
//	            if (Judge.StringNotNull(s)) {
//	                ajaxParams.put(s, new File(files.get(s)));
//                }
//	        }
//        }
//	}
	public void addParams(String key,String value)
	{
	    params.put(key, value);
	}
	public void addHeadMap(String key,String value)
	{
	    headMap.put(key, value);
	}
	public void addFileMap(Map<String, String>files)
	{
		if (Judge.MapNotNull(files)) {
			for(String p:files.keySet())
			{
				if(Judge.StringNotNull(p))
					addFileMap(p,files.get(p));
			}
		}
	}
	public void addFileMap(String key,String path)
	{
		if(fileMap==null)
			fileMap= new ArrayList<FieldItem>();
		if(!Judge.StringNotNull(key,path))
			return;
	    fileMap.add(new FieldItem(key, new File(path)));
	}
	public void addAllFileMap(ArrayList<FieldItem> fileMap)
	{
		if(Judge.ListNotNull(fileMap))
			this.fileMap.addAll(fileMap);
	}
	
	public static ReaderBase JSON_to(final JSONObject j)
	{
		
		try {
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
				@Override
				public Map<String, String> getHeadMap() {
					// TODO Auto-generated method stub
					try {
						return new Gson().fromJson(j.getJSONObject("head")+"", new TypeToken<HashMap<String, String>>(){}.getType());
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
				@Override
				public HashMap<String, String> getParams() {
					// TODO Auto-generated method stub
					try {
						return new Gson().fromJson(j.getJSONObject("params")+"", new TypeToken<HashMap<String, String>>(){}.getType());
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
				@Override
				public Method getMethod() {
					// TODO Auto-generated method stub
					try {
						return new Gson().fromJson(j.getString("method")+"", new TypeToken<Method>(){}.getType());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
				@Override
				public ArrayList<FieldItem> getFileMap() {
					// TODO Auto-generated method stub
					ArrayList<FieldItem> fileMap = new ArrayList<FieldItem>();
					try {
						JSONArray file=j.getJSONArray("file");
						for (int i = 0; i < file.length(); i++) {
							JSONObject temp=(JSONObject) file.get(i);
							fileMap.add(new FieldItem(temp.get("path")+"", new File(temp.get("name")+"") ));
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return fileMap;
				}
			};
			re.finalUrl=j.getString("url");
			return re;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
	public JSONObject toJSON()
	{
		try {
			JSONObject j=new JSONObject();
			j.put("url", getFinalUrl());
			j.put("method", getMethod());
			JSONObject head=new JSONObject(getHeadMap());
			JSONArray file=new JSONArray();
			for(FieldItem i:fileMap)
			{
				if (i!=null) {
					JSONObject temp=new JSONObject();
					temp.put("name", i.getFieldValue());
					temp.put("path", i.getFieldName());
					file.put(temp);
				}
			}
			JSONObject params=new JSONObject(getParams());
			j.put("file", file);
			j.put("params", params);
			j.put("head", head);
			
			return j;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	public boolean isNeedRemoveContentType()
	{
		return true;
	}
}
