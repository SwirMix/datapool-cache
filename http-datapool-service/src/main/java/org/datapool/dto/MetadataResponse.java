package org.datapool.dto;

import org.datapool.core.cache.CacheMetadata;

import java.util.concurrent.Future;

public class MetadataResponse {
    private CacheMetadata cacheMetadata;
    private Future<CacheMetadata> future;

    public CacheMetadata getCacheMetadata() {
        return cacheMetadata;
    }

    public MetadataResponse setCacheMetadata(CacheMetadata cacheMetadata) {
        this.cacheMetadata = cacheMetadata;
        return this;
    }

    public Future<CacheMetadata> getFuture() {
        return future;
    }

    public MetadataResponse setFuture(Future<CacheMetadata> future) {
        this.future = future;
        return this;
    }
}
