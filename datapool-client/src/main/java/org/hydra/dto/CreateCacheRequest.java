package org.hydra.dto;

import java.util.List;
import java.util.Properties;

public class CreateCacheRequest {
    protected String query = "";
    protected Properties connectionProperties = new Properties();
    protected DataSources type;
    protected String cacheName;
    protected String projectId;
    protected List<String> columns;

    public List<String> getColumns() {
        return columns;
    }

    public CreateCacheRequest setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public CreateCacheRequest setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public CreateCacheRequest setQuery(String query) {
        this.query = query;
        return this;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public CreateCacheRequest setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
        return this;
    }

    public DataSources getType() {
        return type;
    }

    public CreateCacheRequest setType(DataSources type) {
        this.type = type;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CreateCacheRequest setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }
}
