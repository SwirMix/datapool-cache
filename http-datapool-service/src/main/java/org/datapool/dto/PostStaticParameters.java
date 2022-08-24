package org.datapool.dto;

import org.datapool.core.cache.dto.StaticCacheKey;
import org.datapool.core.cache.dto.StaticCacheValue;

public class PostStaticParameters {
    private StaticCacheValue value;
    private StaticCacheKey key;

    public PostStaticParameters(){

    }

    public PostStaticParameters(StaticCacheKey key, StaticCacheValue value){
        this.key = key;
        this.value = value;
    }

    public StaticCacheValue getValue() {
        return value;
    }

    public PostStaticParameters setValue(StaticCacheValue value) {
        this.value = value;
        return this;
    }

    public StaticCacheKey getKey() {
        return key;
    }

    public PostStaticParameters setKey(StaticCacheKey key) {
        this.key = key;
        return this;
    }
}
