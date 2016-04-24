package com.librariy.net;

public class JsonRequest extends HttpRequest {
    /**Please refer to： {@link com.librariy.net.HttpRequest.Method} */
    public JsonRequest(Method method) {
        super(method);
    }
    /**Please refer to： {@link com.librariy.net.HttpRequest.Method} */
    public JsonRequest(Method method, String formatApiURI, Object... args) {
        super(method, formatApiURI, args);
    }

    @Override
    protected void initializeDefault() {
        // 在各个项目中需要覆盖这些默认参数
        super.setBaseURI("http://127.0.0.1:8080");
        super.setHeader("Authorization", "Token");
    }
}