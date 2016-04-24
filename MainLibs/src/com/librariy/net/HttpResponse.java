package com.librariy.net;
public interface HttpResponse {
    public void onCompleted(int responseCode, String responseText);
}