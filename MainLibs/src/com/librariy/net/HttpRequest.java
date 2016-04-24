package com.librariy.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import android.text.TextUtils;
import android.util.Log;

public class HttpRequest {
    public final static String TAG = HttpRequest.class.getSimpleName();

    public static enum Method {
        GET, POST, PUT, DELETE, POST_BIN
    }

    protected String BaseUrl = "";
    protected String ApiURI = "/api/token";
    protected Method method = Method.GET;
    protected final HashSet<FieldItem> headers = new HashSet<FieldItem>();
    protected final ArrayList<FieldItem> requestParameters = new ArrayList<FieldItem>();
    protected final ArrayList<FieldItem> formFileds = new ArrayList<FieldItem>();
    private String bodyText = null;

    public HttpRequest(Method method) {
        this.method = method;
        this.reset();
    }

    /**
     * (see {@link com.librariy.net.HttpRequest#setRequestUrl})
     *
     */
    public HttpRequest(Method method, String formatApiURI, Object... args) {
        this.method = method;
        this.setApiURI(formatApiURI, args);
        this.reset();
    }

    protected void initializeDefault() {
    }

    /**
     * Set the Request URL, using the supplied format and arguments
     *
     * @param formatUrl
     *            , the format string (see {@link java.util.Formatter#format})
     * @param args
     *            the list of arguments passed to the formatter. If there are
     *            more arguments than required by {@code format}, additional
     *            arguments are ignored.
     * @throws java.util.IllegalFormatException
     *             if the format is invalid.
     * @since 1.5
     */
    public void setApiURI(String formatURL, Object... args) {
        try {
            ApiURI = String.format(formatURL, args);
        } catch (Exception e) {
            Log.e(TAG, "setRequestUrl", e);
        }
    }

    public void setBaseURI(String mBaseUrl) {
        StringBuffer sb = new StringBuffer(mBaseUrl);
        for (int i = sb.length() - 1; i >= 0; i--) {
            if (sb.charAt(i) != '/') {
                break;
            }
            sb.deleteCharAt(i);
        }
        this.BaseUrl = sb.toString();
    }
    public void addAllFormField(ArrayList<FieldItem> f) {
        this.formFileds.addAll(f);
        this.method = Method.POST_BIN;
    }
    /**
     * 设置请求头参数
     * @param mKey
     * @param mValue ：为空, 删除已存在的参数； 否则添加（如已存在则覆盖）一个请求头参数</br>
     */
    public void setHeader(String mKey, Object mValue) {
        FieldItem item = new FieldItem(mKey, mValue);
        this.headers.remove(item);
        if(mValue!=null){
            this.headers.add(item);
        }
    }
    /**
     * 添加URL参数
     * @param mKey
     * @param mValue ：无论是否已经存在该参数，直接添加一个URL参数（对于同一个Key, 可重复）。</br>
     */
    public void addUrlParameter(String mKey, Object mValue) {
        this.requestParameters.add(new FieldItem(mKey, mValue));
    }
    /**
     * 设置URL参数
     * @param mKey
     * @param mValue ：为空, 删除已存在的参数； 否则添加（如已存在则覆盖）一个URL参数</br>
     */
    public void setUrlParameter(String mKey, Object mValue) {
        FieldItem item=new FieldItem(mKey, mValue);
        this.requestParameters.remove(item);
        if(mValue!=null){
            this.requestParameters.add(item);
        }
    }
    /**
     * 添加Body-Form参数
     * @param fieldName
     * @param fieldValue ：无论是否已经存在该参数，直接添加一个Body-Form参数（对于同一个Key, 可重复）。</br>
     */
    public void addFormField(String fieldName, Object fieldValue) {
        if (!TextUtils.isEmpty(this.bodyText)) {
            Log.w(TAG, "addFormField()", new Exception("The [Body] has been set, Can't set [FormField]"));
            return;
        }
        if(fieldValue instanceof File){
            this.formFileds.add(new FieldItem(fieldName, fieldValue));
            this.method = Method.POST_BIN;
        }else{
            this.formFileds.add(new FieldItem(fieldName, fieldValue==null?"":fieldValue));
        }
    }
    /**
     * 设置Body-Form参数
     * @param fieldName
     * @param fieldValue ：为空, 删除已存在的参数； 否则添加（如已存在则覆盖）一个Body-Form参数</br>
     */
    public void setFormField(String fieldName, Object fieldValue) {
        if (!TextUtils.isEmpty(this.bodyText)) {
            Log.w(TAG, "addFormField()", new Exception("The [Body] has been set, Can't set [FormField]"));
            return;
        }
        FieldItem item = new FieldItem(fieldName, fieldValue);
        this.formFileds.remove(item);
        if(fieldValue instanceof File){
            this.formFileds.add(item);
            this.method = Method.POST_BIN;
        }else if(fieldValue!=null){
            this.formFileds.add(item);
        }
    }
    public void reset() {
        this.headers.clear();
        this.formFileds.clear();
        this.requestParameters.clear();
        this.bodyText=null;
        initializeDefault();
    }
    public void setBody(String bodyText) {
        if (!this.formFileds.isEmpty()) {
            this.formFileds.clear();
            Log.w(TAG, "setBody()", new Exception("The [FormFiled] has been set, will clear them! "));
        }
        this.bodyText = bodyText;
    }

    public String getBody() {
        return this.bodyText;
    }
    public String getFinalUrl() {
        StringBuffer sb = new StringBuffer(BaseUrl + ApiURI);
        for (int i = 0; i < requestParameters.size(); i++) {
            sb.append((i == 0) ? '?' : '&');
            sb.append(requestParameters.get(i).toString());
        }
        return sb.toString();
    }
    public String getFinalUrl(String charset) {
        StringBuffer sb = new StringBuffer(UrlTools.encodeURI(BaseUrl + ApiURI,charset));
        for (int i = 0; i < requestParameters.size(); i++) {
            sb.append((i == 0) ? '?' : '&');
            sb.append(requestParameters.get(i).toString(charset));
        }
        return sb.toString();
    }

    public String toString() {
        return "[RequestUR]: " + method + "#" + getFinalUrl() + "   [Header]: " + headers + "   [BodyForm]: " + formFileds + "   [BodyText]: " + bodyText;
    }



    public static class FieldItem {
        public final static FieldItem BODY_ITEM = new FieldItem("");
        protected final String fieldName;

        public String getFieldName() {
            return fieldName;
        }

        protected final Object fieldValue;

        public Object getFieldValue() {
            return fieldValue;
        }

        public FieldItem(String fieldName, Object fieldValue) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public FieldItem(Object fieldValue) {
            this.fieldName = "FieldItem_BODY_TEXT";
            this.fieldValue = fieldValue;
        }

        @Override
        public int hashCode() {
            return fieldName == null ? -1 : fieldName.hashCode();
        }

        public void writeBodyPart(final OutputStream requestOut, final String BOUNDARY) throws IOException {
            if (fieldValue instanceof File) {
                File localFile = (File) fieldValue;
                String localFileName = localFile.getName();
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + localFileName + "\"\r\n");
                strBuf.append("Content-Type: application/octet-stream\r\n\r\n");
                requestOut.write(strBuf.toString().getBytes());
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(localFile));
                byte[] buff = new byte[1024];
                int len = 0;
                int totalSize = 0;
                while ((len = in.read(buff)) > 0) {
                    requestOut.write(buff, 0, len);
                    totalSize += len;
                    // System.out.println("totalSize=" + totalSize / (1024 *
                    // 1024 * 1.0));
                }
                in.close();
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + fieldName + "\"\r\n");
                sb.append("Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                sb.append(fieldValue);
                requestOut.write(sb.toString().getBytes("UTF-8"));
            }
        }

        @Override
        public boolean equals(Object obj) {
            if ((obj instanceof FieldItem) && fieldName != null) {
                return fieldName.equals(((FieldItem) obj).fieldName);
            }
            return super.equals(obj);
        }

        public String toString() {
            return fieldName + "=" + fieldValue;
        }

        public String toString(String charsetName) {
            return fieldName + "=" + UrlTools.encodeURIComponent(fieldValue == null ? "" : fieldValue.toString(), charsetName);
        }
    }
}