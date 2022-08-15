package org.datapool.dto;

public class CreateRuntimeCacheRequest{
    private String cacheName;
    private String[] columns;
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public CreateRuntimeCacheRequest setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CreateRuntimeCacheRequest setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public CreateRuntimeCacheRequest setColumns(String[] columns) {
        this.columns = columns;
        return this;
    }
}
