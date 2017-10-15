package com.example.yetti.toneplayer.network;

import java.util.Map;

public final class Request {

    private final Map<String, String> mHeaders;
    private final String mUrl;
    private final String mMethod;
    private final String mBody;

    private Request(final RequestBuilder pRequestBuilder) {
        this.mHeaders = pRequestBuilder.mHeaders;
        this.mUrl = pRequestBuilder.mUrl;
        this.mMethod = pRequestBuilder.mMethod;
        this.mBody = pRequestBuilder.mBody;
    }

    String getUrl() {
        return mUrl;
    }

    String getMethod() {
        return mMethod;
    }

    String getBody() {
        return mBody;
    }

    Map<String, String> getHeaders() {
        return mHeaders;
    }

    public static class RequestBuilder {

        private Map<String, String> mHeaders;
        private final String mUrl;
        private String mMethod;
        private String mBody;

        public RequestBuilder(final String pUrl) {
            this.mUrl = pUrl;
        }

        public RequestBuilder headers(final Map<String, String> pHeaders) {
            this.mHeaders = pHeaders;
            return this;
        }

        public RequestBuilder method(final String pMethod) {
            this.mMethod = pMethod;
            return this;
        }

        public RequestBuilder body(final String pBody) {
            this.mBody = pBody;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }
}
