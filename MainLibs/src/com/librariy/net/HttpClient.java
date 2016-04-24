package com.librariy.net;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.librariy.utils.Log;

import android.text.TextUtils;

public class HttpClient {
    public final static String TAG=HttpClient.class.getSimpleName();
    private static String token = "";    
    public static String getToken() {
        return token;
    }
    public static void setToken(String token) {
        HttpClient.token = token;
    }
    public static void request(final HttpRequest request, final HttpResponse callback) {
        if (!TextUtils.isEmpty(token)) {
            request.setHeader("Authorization", token);
        }
        new MyAsyncTask(){
            @Override
            protected HttpResult doInBackground(Void... params) {
                if(request.method==HttpRequest.Method.POST_BIN){
                    return requestHttpAsBinary(request,this);
                }else{
                    return requestHttpAsText(request,this);
                }
            }            
            @Override
            protected void onProgressUpdate(Float... values) {
                super.onProgressUpdate(values);
                if(values==null||values.length<1) return;
                Log.v(TAG, String.format("The HTTP request progress :  %.2f%%",values[0]));
            }

            @Override
            protected void onPostExecute(HttpResult result) {
                super.onPostExecute(result);
                Log.d(TAG, result.toString());
                if(result==null) return;
                callback.onCompleted(result.responseCode, result.responseText);
            }            
        }.execute();
    }

    /**
     * Http文本请求
     * 
     * @param HttpRequest
     * @return HttpResult
     */
    private static HttpResult requestHttpAsText(HttpRequest request,MyAsyncTask task) {
        try {
            Log.d(TAG, "requestHttpAsText():\n"+request);
            Log.d(TAG, "encodeURI="+request.getFinalUrl("UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) (new URL(request.getFinalUrl("UTF-8"))).openConnection();
            conn.setRequestMethod(request.method.name());
            conn.setConnectTimeout(30 * 1000);
            conn.setReadTimeout(30 * 1000);
            conn.setDoInput(true);

            for (HttpRequest.FieldItem item : request.headers) {
                conn.setRequestProperty(item.fieldName, (String)item.fieldValue);
            }
            if (!TextUtils.isEmpty(request.getBody())) {
                conn.setDoOutput(true);
                OutputStream requestOut = conn.getOutputStream();
                requestOut.write(request.getBody().getBytes("UTF-8"));
                requestOut.flush();
                requestOut.close();
            }else if (!request.formFileds.isEmpty()) {
                StringBuffer sb = new StringBuffer("");
                for (HttpRequest.FieldItem item : request.formFileds) {
                    sb.append("&" + item.toString("UTF-8"));
                }
                if (!sb.equals("")) {
                    sb.deleteCharAt(0);
                }
                conn.setDoOutput(true);
                OutputStream requestOut = conn.getOutputStream();
                requestOut.write(sb.toString().getBytes("UTF-8"));
                requestOut.flush();
                requestOut.close();
            }
            // 读取返回数据
            ByteArrayOutputStream responseOut = new ByteArrayOutputStream();
            int mResponseCode = conn.getResponseCode();
            BufferedInputStream responseIn = new BufferedInputStream(mResponseCode == 200 ? conn.getInputStream() : conn.getErrorStream());
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = responseIn.read(buff)) > 0) {
                responseOut.write(buff, 0, len);
            }
            responseIn.close();
            return new HttpResult(mResponseCode, responseOut.toString("UTF-8"));
        } catch (Exception e) {
            Log.e(TAG, "请求出错",e);
            return new HttpResult(-1, "请求出错");
        }
    }

    /**
     * Http二进制请求
     * 
     * @param HttpRequest
     * @return HttpResult
     */
    private static HttpResult requestHttpAsBinary(HttpRequest request,MyAsyncTask task) {
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
        try {
            task.publishProgressFloat(0f);
            request.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            Log.d(TAG, "requestHttpAsBinary():\n"+request);
            Log.d(TAG, "encodeURI="+request.getFinalUrl("UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) (new URL(request.getFinalUrl("UTF-8"))).openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setChunkedStreamingMode(100*1024);
            //conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            for (HttpRequest.FieldItem item : request.headers) {
            	conn.setRequestProperty(item.fieldName, (String)item.fieldValue);
            }
            // text
            if (request.formFileds != null) {
                conn.setDoOutput(true);
                OutputStream requestOut = conn.getOutputStream();
                int len=request.formFileds.size();
                for (int i=0;i<len;i++) {
                    HttpRequest.FieldItem item=request.formFileds.get(i);
                    item.writeBodyPart(requestOut, BOUNDARY);
                    task.publishProgressFloat(i*1.0f/len);
                }
                requestOut.write(("\r\n--" + BOUNDARY + "--\r\n").getBytes());
                requestOut.flush();
                requestOut.close();
            }
            // 读取返回数据
            ByteArrayOutputStream responseOut = new ByteArrayOutputStream();
            int mResponseCode = conn.getResponseCode();
            BufferedInputStream responseIn = new BufferedInputStream(mResponseCode == 200 ? conn.getInputStream() : conn.getErrorStream());
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = responseIn.read(buff)) > 0) {
                responseOut.write(buff, 0, len);
            }
            responseIn.close();
            task.publishProgressFloat(1f);
            return new HttpResult(mResponseCode, responseOut.toString("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new HttpResult(-1, "上传的文件不存在");
        }catch (Exception e) {
            Log.e(TAG, "服务器连接异常，请检查网络",e);
            return new HttpResult(-2, "服务器连接异常，请检查网络");
        }
    }
    private static class HttpResult{
        protected int responseCode = -1;
        protected String responseText = "";
        protected HttpResult(int responseCode, String responseText) {
            this.responseCode = responseCode;
            this.responseText = responseText;
        }
        @Override
        public String toString() {
            return "[HttpResult]: responseCode="+responseCode+",  responseText="+responseText;
        }
    }
    private static abstract class MyAsyncTask extends android.os.AsyncTask<Void,Float,HttpResult>{
        public MyAsyncTask(){}
        public void publishProgressFloat(Float... values) {
            super.publishProgress(values);
        }
    }
}