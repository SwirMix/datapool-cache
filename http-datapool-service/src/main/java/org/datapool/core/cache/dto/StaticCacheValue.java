package org.datapool.core.cache.dto;

import java.util.Date;
import java.util.Map;

public class StaticCacheValue {
    private String name;
    private Date updateDate;
    private String projectId;
    private Map<String, Object> parameters;

    public String getName() {
        return name;
    }

    public StaticCacheValue setName(String name) {
        this.name = name;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public StaticCacheValue setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public StaticCacheValue setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public StaticCacheValue setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }
}
