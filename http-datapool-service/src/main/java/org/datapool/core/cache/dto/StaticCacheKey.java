package org.datapool.core.cache.dto;

public class StaticCacheKey {
    private String project;
    private String cacheUUID;

    public StaticCacheKey(String project, String cacheUUID){
        this.cacheUUID = cacheUUID;
        this.project = project;
    }

    public StaticCacheKey(){

    }

    public String getProject() {
        return project;
    }

    public StaticCacheKey setProject(String project) {
        this.project = project;
        return this;
    }

    public String getCacheUUID() {
        return cacheUUID;
    }

    public StaticCacheKey setCacheUUID(String cacheUUID) {
        this.cacheUUID = cacheUUID;
        return this;
    }
}
