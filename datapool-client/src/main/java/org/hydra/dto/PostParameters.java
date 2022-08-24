package org.hydra.dto;

import java.util.Map;

public class PostParameters {
    private Map<String, Object> parameters;
    private String cacheName;

    public String getCacheName() {
        return cacheName;
    }

    public PostParameters setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public PostParameters setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }
}
