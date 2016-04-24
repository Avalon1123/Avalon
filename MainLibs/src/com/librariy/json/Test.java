package com.librariy.json;

import com.librariy.net.JsonResponse.ResponseKey;


public class Test {
    public static void main(String[] args) {
      JsonObject jsonObject = new JsonObject("{'TotalRecords':1,'Datas':[null,null,{'Id':141,'GroupName':'开发商'}],'Data':'null','IsSuccess':true,'ErrorMessage':null}"); 
      System.out.println(jsonObject.toString());
      JsonObject jsonObject1 = new JsonObject(jsonObject.toString());
      System.out.println(jsonObject1);
      
//      System.out.println(jsonObject.optInt(ResponseKey.TotalRecords,0));
//      System.out.println((JsonObject)jsonObject.optJsonArray(ResponseKey.Datas,new JsonArray()).optJsonObject(0,new JsonObject()));
//      System.out.println(jsonObject.optString(ResponseKey.Data,"default"));
//      System.out.println(jsonObject.optBoolean(ResponseKey.IsSuccess,false));
//      System.out.println(jsonObject.optBoolean(ResponseKey.IsSuccess,false));
//      
//      JsonObject j22 = new JsonObject();
//      System.out.println(j22.optJsonArray("Datas", new JsonArray()).join(","));
//      j22.accumulate("Datas", 1);
//      System.out.println(j22.optJsonArray("Datas", new JsonArray()).join(","));
//      j22.accumulate("Datas", 2);
//      System.out.println(j22.optJsonArray("Datas", new JsonArray()).join(","));
//      j22.accumulate("Datas", 3);
//      System.out.println(j22.optJsonArray("Datas", new JsonArray()).join(","));
//      
//      System.out.println(JsonObject.NULL.equals(JsonObject.NULL));
//      
//      
//      System.out.println(JsonObject.NULL.equals(JsonObject.NULL));
    }
}

