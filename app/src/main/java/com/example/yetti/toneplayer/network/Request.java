package com.example.yetti.toneplayer.network;

import java.util.Map;

public class Request {

    private Map<String, String> mHeaders;
    private String mUrl;
    private String mMethod;
    private String mBody;

    public Request(final RequestBuilder pRequestBuilder) {
        this.mHeaders = pRequestBuilder.mHeaders;
        this.mUrl = pRequestBuilder.mUrl;
        this.mMethod = pRequestBuilder.mMethod;
        this.mBody = pRequestBuilder.mBody;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getMethod() {
        return mMethod;
    }

    public String getBody() {
        return mBody;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public static class RequestBuilder {

        private Map<String, String> mHeaders;
        private String mUrl;
        private String mMethod;
        private String mBody;

        public RequestBuilder(final String pUrl) {
            this.mUrl = pUrl;
        }

        public RequestBuilder headers(final Map<String, String> pHeaders) {
            this.mHeaders = pHeaders;
            return this;
        }

        public RequestBuilder method(String pMethod) {
            this.mMethod = pMethod;
            return this;
        }

        public RequestBuilder body(String pBody) {
            this.mBody = pBody;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }
}
