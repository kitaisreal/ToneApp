package com.example.yetti.toneplayer.network;

public class Request {

    private String mUrl;
    private String mMethod;
    private String mBody;

    public Request(String pUrl, String pMethod, String pBody) {
        this.mUrl = pUrl;
        this.mMethod = pMethod;
        this.mBody = pBody;
    }

    public Request(String url, String method) {
        this.mUrl = url;
        this.mMethod = method;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        this.mMethod = method;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }
}
