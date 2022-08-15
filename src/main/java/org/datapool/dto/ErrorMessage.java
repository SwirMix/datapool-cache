package org.datapool.dto;

public class ErrorMessage {
    private String msg;

    public ErrorMessage(){

    }

    public ErrorMessage(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public ErrorMessage setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
