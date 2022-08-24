package org.datapool.core.cache;


import org.datapool.core.CacheStatus;
import org.datapool.core.DataSources;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CacheMetadata {
    private String query = "";
    private Integer rowCount = -1;
    private CacheStatus status;
    private Properties connectionProperties;
    private DataSources type;
    private String cacheName;
    private CachePermissions permissions = CachePermissions.PUBLIC;
    private List<String> consumers = new ArrayList<>();
    private String[] columns;
    private List project = new ArrayList();
    private String baseProject;

    public String getBaseProject() {
        return baseProject;
    }

    public CacheMetadata setBaseProject(String baseProject) {
        this.baseProject = baseProject;
        return this;
    }

    public List getProject() {
        return project;
    }

    public CacheMetadata setProject(List project) {
        this.project = project;
        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public CacheMetadata setColumns(String[] columns) {
        this.columns = columns;
        return this;
    }

    public List<String> getConsumers() {
        return consumers;
    }

    public CacheMetadata setConsumers(List<String> consumers) {
        this.consumers = consumers;
        return this;
    }

    public CachePermissions getPermissions() {
        return permissions;
    }

    public CacheMetadata setPermissions(CachePermissions permissions) {
        this.permissions = permissions;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CacheMetadata setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public DataSources getType() {
        return type;
    }

    public int getNextNum(){
        synchronized (rowCount){
            rowCount +=1;
            return rowCount;
        }
    }

    public void setRowCount(int index){
        rowCount = index;
    }

    public CacheMetadata setType(DataSources type) {
        this.type = type;
        return this;
    }

    public CacheMetadata(DataSources type, Properties properties){
        this.type = type;
        this.connectionProperties = properties;
    }

    public String getQuery() {
        return query;
    }

    public CacheMetadata setQuery(String query) {
        this.query = query;
        return this;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public CacheMetadata setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    public CacheStatus getStatus() {
        return status;
    }

    public CacheMetadata setStatus(CacheStatus status) {
        this.status = status;
        return this;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public CacheMetadata setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
        return this;
    }

    public enum CachePermissions {
        PRIVATE,
        PUBLIC
    }
}
