package org.datapool.dto;

import java.util.Map;

public class PostParameters {
    private Map<String, String> parameters;
    private String cacheName;

    public String getCacheName() {
        return cacheName;
    }

    public PostParameters setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public PostParameters setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }
}
