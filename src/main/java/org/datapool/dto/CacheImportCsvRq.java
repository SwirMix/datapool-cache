package org.datapool.dto;

public class CacheImportCsvRq {
    private String cacheName;
    private String fileName;
    private boolean override;
    private String project;

    public String getProject() {
        return project;
    }

    public CacheImportCsvRq setProject(String project) {
        this.project = project;
        return this;
    }

    public boolean isOverride() {
        return override;
    }

    public CacheImportCsvRq setOverride(boolean override) {
        this.override = override;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public CacheImportCsvRq setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CacheImportCsvRq setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }
}
