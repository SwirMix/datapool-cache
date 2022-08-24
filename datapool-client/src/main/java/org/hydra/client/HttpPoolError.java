package org.hydra.client;

public class HttpPoolError extends Exception{
    private Object body;
    private int responseCode;

    public Object getBody() {
        return body;
    }

    public HttpPoolError setBody(Object body) {
        this.body = body;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public HttpPoolError setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public HttpPoolError(Object body, int responseCode){
        this.body = body;
        this.responseCode = responseCode;
    }
}
