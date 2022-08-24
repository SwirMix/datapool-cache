package org.datapool.dto;

import org.datapool.core.Strategy;

import java.io.Serializable;

public class ParametersResponse implements Serializable {
    private Result status;
    private Object params;
    private ErrorMessage errorMessage;
    private Strategy strategy;

    public Strategy getStrategy() {
        return strategy;
    }

    public ParametersResponse setStrategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public ParametersResponse setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public ParametersResponse(){

    }

    public ParametersResponse(Result status, Object params, ErrorMessage errorMessage, Strategy strategy){
        this.params = params;
        this.status = status;
        this.errorMessage = errorMessage;
        this.strategy = strategy;
    }

    public Result getStatus() {
        return status;
    }

    public ParametersResponse setStatus(Result status) {
        this.status = status;
        return this;
    }

    public Object getParams() {
        return params;
    }

    public ParametersResponse setParams(Object params) {
        this.params = params;
        return this;
    }
}
