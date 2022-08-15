package org.hydra.client;

public class HttpPoolError extends Exception{
    private Object body;
    public HttpPoolError(Object body){
        this.body = body;
    }
}
