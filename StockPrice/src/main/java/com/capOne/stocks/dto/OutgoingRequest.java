package com.capOne.stocks.dto;

import com.capOne.stocks.util.RequestType;

import lombok.ToString;

@ToString
public class OutgoingRequest {
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(String jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public String getLogMsg() {
        return logMsg;
    }

    public void setLogMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    private String uri;

    private int timeout;

    private RequestType requestType;

    private String path;

    private String jsonRequest;

    private String logMsg;

    private OutgoingRequest(RequestBuilder requestBuilder) {
        this.uri = requestBuilder.uri;
        this.timeout = requestBuilder.timeout;
        this.requestType = requestBuilder.requestType;
        this.path = requestBuilder.path;
        this.jsonRequest = requestBuilder.jsonRequest;
        this.logMsg = requestBuilder.logMsg;
    }

    public static class RequestBuilder {
        private String uri;

        private int timeout;

        private RequestType requestType;

        private String path;

        private String jsonRequest;

        private String logMsg;

        public RequestBuilder(String uri, int timeout, RequestType requestType) {
            this.uri = uri;
            this.timeout = timeout;
            this.requestType = requestType;
        }

        public RequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        public RequestBuilder jsonRequest(String jsonRequest) {
            this.jsonRequest = jsonRequest;
            return this;
        }

        public RequestBuilder logMsg(String logMsg) {
            this.logMsg = logMsg;
            return this;
        }

        public RequestBuilder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public OutgoingRequest build() {
            OutgoingRequest outgoingRequest = new OutgoingRequest(this);
            return outgoingRequest;
        }
    }
}
