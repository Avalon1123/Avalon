package com.librariy.net;

import java.util.ArrayList;
import com.librariy.json.*;

public class JsonResponse implements HttpResponse {
    public static enum ResponseKey{IsSuccess,TotalRecords,Datas, Data,ErrorMessage};
    private static final String TAG = JsonResponse.class.getSimpleName();

    /**
     * <pre>
     *         public void onCompleted(int responseCode, String responseText) {
     *             if(responseCode!=200){
     *                 String mErrorMsg=getErrorMsg(responseCode,responseText));
     *                 //显示默认错误消息
     *                 return;
     *             }
     *             JSONObject data=super.getJsonObject(responseText);
     *             if(data.optBoolean("IsSuccess",false)){
     *                 data.optString("ErrorMessage","修改失败! ")
     *                 //显示操作成功
     *             }else{
     *                 String mErrorMsg=data.optBoolean("IsSuccess",false)
     *                 //显示失败后服务器返回的错误消息
     *             }
     *         }
     * </pre>
     */
    @Override
    public void onCompleted(int responseCode, String responseText) {

    }

    public JsonArray getJsonArray(String responseText) {
        return JsonReflect.getJsonArray(responseText);
    }

    public JsonObject getJsonObject(String responseText) {
        return JsonReflect.getJsonObject(responseText);
    }

    public String getErrorMsg(int responseCode, String responseText) {
        if (responseCode < 0) {
            return responseText;
        } else {
            return "服务器异常,请稍候再试！";
        }
    }

    /**
     * 公用方法：将Bean中所有值拷贝到action中 参 数：java bean对象
     */
    public <T> T getBean(JsonObject json, Class<T> mClass) {
        return JsonReflect.cast(json, mClass);
    }

    /**
     * 公用方法：将Bean中所有值拷贝到action中 参 数：java bean对象
     */
    public <T> ArrayList<T> getBeanList(JsonArray jsonArray, Class<T> mClass) {
        return JsonReflect.cast(jsonArray, mClass);
    }
}
