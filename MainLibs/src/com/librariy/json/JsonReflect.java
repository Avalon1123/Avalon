package com.librariy.json;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class JsonReflect {
    private static final String TAG = JsonReflect.class.getSimpleName();
    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value=ElementType.FIELD)
    public static @interface FieldAlias{
        public String value();
    }
    /**
     * 转换：JavaBean对象->JSON对象
     */
    public static <T> JsonObject parseBeanToJson(T mBean) {
        JsonObject json = new JsonObject();
        try {
            Field mFields[] = mBean.getClass().getDeclaredFields();
            for (Field f : mFields) {
                f.setAccessible(true);
                String fieldName = f.getName();
                for(Annotation a:f.getAnnotations()){
                    if(a instanceof FieldAlias){
                        fieldName =((FieldAlias) a).value();
                        break;
                    }
                    if(a instanceof SerializedName){
                        fieldName = ((SerializedName) a).value();
                        break;
                    }
                }
                json.put(fieldName, f.get(mBean));
            }
        } catch (Exception e) {
            System.err.println(TAG+"parseBean(" + mBean+"), 失败");
        }
        return json;
    }
    /**
     * 转换：JSON对象->JavaBean对象
     */
    public static <T> T cast(JsonObject json, Class<T> mClass) {
        T bean = null;
        try {
            Field mFields[] = mClass.getDeclaredFields();
            bean = mClass.newInstance();
            for (Field f : mFields) {
                f.setAccessible(true);
                String fieldName = f.getName();
                for(Annotation a:f.getAnnotations()){
                    if(a instanceof FieldAlias){
                        fieldName =((FieldAlias) a).value();
                        break;
                    }
                    if(a instanceof SerializedName){
                        fieldName = ((SerializedName) a).value();
                        break;
                    }
                }
                Class type = f.getType();
                if(!json.has(fieldName)){
                    System.out.println(TAG+":"+fieldName + "["+fieldName + "]:该字段为空,不需要赋值");
                }else if (type == Boolean.TYPE||Boolean.class.isAssignableFrom(type)) {
                    f.set(bean, json.optBoolean(fieldName, f.getBoolean(bean)));
                } else if (type == Short.TYPE||Short.class.isAssignableFrom(type)) {
                    f.set(bean, (short)json.optInt(fieldName, f.getShort(bean)));
                }else if (type == Integer.TYPE||Integer.class.isAssignableFrom(type)) {
                    f.set(bean, json.optInt(fieldName, f.getInt(bean)));
                } else if (type == Long.TYPE||Long.class.isAssignableFrom(type)) {
                    f.set(bean, json.optLong(fieldName, f.getLong(bean)));
                } else if (type == Float.TYPE||Float.class.isAssignableFrom(type)) {
                    f.set(bean, (float)json.optDouble(fieldName, f.getFloat(bean)));
                } else if (type == Double.TYPE||Double.class.isAssignableFrom(type)) {
                    f.set(bean, json.optDouble(fieldName, f.getDouble(bean)));
                } else if (String.class.isAssignableFrom(type)) {
                    f.set(bean, json.optString(fieldName, (String)f.get(bean)));
                } else {
                    System.out.println(TAG+": ["+fieldName + "]:不支持转换的数据类型" + type);
                }
            }
            return bean;
        } catch (Exception e) {
            System.err.println(TAG+": getBean(" + json + ",  " + mClass + ") -> 失败！");
            return bean;
        }
    }
    public static <T> ArrayList<T> cast(JsonArray jsonArray, Class<T> mClass) {
        ArrayList<T> retList = new ArrayList<T>();
        try {
            if(jsonArray==null) return retList;
            for (int i = 0; i < jsonArray.size(); i++) {
                retList.add(JsonReflect.cast(jsonArray.getJsonObject(i), mClass));
            }
        } catch (Exception e) {
            System.err.println(TAG+": getBeanList(" + jsonArray + ",  " + mClass + ") ->失败！");
        }
        return retList;
    }
    public static int cast(Object value, int fallback) {
        try {
            if (value == null)
                return fallback;
            if (value instanceof Integer) {
                return (Integer) value;
            }
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return fallback;
        }
    }
    public static JsonArray getJsonArray(String responseText) {
        return new JsonArray(responseText);
    }
    public static JsonObject getJsonObject(String responseText) {
        return new JsonObject(responseText);
    }
}
