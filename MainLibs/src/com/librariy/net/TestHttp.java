package com.librariy.net;

import java.io.File;

public class TestHttp {
    public static void main(String[] args) {
        JsonRequest mJsonRequest = new JsonRequest(HttpRequest.Method.POST_BIN, "/%s/upload.jsp", "169554444");
        mJsonRequest.setApiURI("/%s/upload.jsp", "169554444");
        mJsonRequest.addUrlParameter("id", "13988745542");
        mJsonRequest.addUrlParameter("offset", 0);
        mJsonRequest.addUrlParameter("limit", 20);
        mJsonRequest.addFormField("Name", "张三");
        mJsonRequest.addFormField("Sex", "男");
        mJsonRequest.addFormField("Photo", new File("D:/temp/pic/222222.png"));
        mJsonRequest.addFormField("Photo", new File("D:/temp/pic/20150613103939.png"));
        mJsonRequest.addFormField("IdentityCard", new File("D:/temp/pic/222222.png"));
        mJsonRequest.addFormField("IdentityCard", new File("D:/temp/pic/20150613103939.png"));
        mJsonRequest.setHeader("x", "198.2254444111");
        mJsonRequest.setHeader("y", "25.2254444111");

        HttpClient.request(mJsonRequest, new JsonResponse() {
            @Override
            public void onCompleted(int responseCode, String responseText) {
                /****************************使用示例****************************/
                //1.处理连接异常及接口无法识别的返回码
                if (responseCode !=200) {
                    System.out.println("错误: " + super.getErrorMsg(responseCode, responseText));
                    return;
                }
                //2.处理接口的正常返回状态
                System.out.println("responseCode=" + responseCode);
                // JSONObject data=super.getJsonObject(responseText);
                // Bean bean=super.getBean(data, Class);
                // JSONArray datas=super.getJsonArray(responseText);
                // List<Bean> beanList=super.getBean(datas, Class);
            }
        });
    }
}
