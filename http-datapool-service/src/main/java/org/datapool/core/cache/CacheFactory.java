package org.datapool.core.cache;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.datapool.NodeConfiguration;
import org.datapool.core.cache.dto.StaticCacheKey;
import org.datapool.core.cache.dto.StaticCacheValue;

import java.util.HashMap;

public class CacheFactory {
    public static final String DEFAULT_METADATA_CACHE = "Metadata";
    public static final String SEQUENTIAL_CACHE = "SEQUENTIAL_CACHE";
    public static CacheConfiguration<Integer, HashMap<String, Object>> prepareDefaultCacheConfiguration(String cacheName){
        CacheConfiguration<Integer, HashMap<String, Object>> cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(cacheName);
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheConfiguration.setDataRegionName(NodeConfiguration.DEFAULT_REGION_NAME);
        return cacheConfiguration;
    }

    public static CacheConfiguration<StaticCacheKey, StaticCacheValue> prepareDefaultStaticCacheConfiguration(String cacheName){
        CacheConfiguration<StaticCacheKey, StaticCacheValue> cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(cacheName);
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheConfiguration.setDataRegionName(NodeConfiguration.DEFAULT_REGION_NAME);
        return cacheConfiguration;
    }

    public static CacheConfiguration<String, CacheMetadata> prepareMetadataCacheConfiguration(){
        CacheConfiguration<String, CacheMetadata> cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(DEFAULT_METADATA_CACHE);
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setBackups(3);
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cacheConfiguration.setDataRegionName(NodeConfiguration.DEFAULT_REGION_NAME);
        return cacheConfiguration;
    }

    public static CacheConfiguration<CacheMetadata, Integer> prepareSequentialCache(){
        CacheConfiguration<CacheMetadata, Integer> cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(SEQUENTIAL_CACHE);
        cacheConfiguration.setCacheMode(CacheMode.REPLICATED);
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cacheConfiguration.setDataRegionName(NodeConfiguration.DEFAULT_REGION_NAME);
        return cacheConfiguration;
    }
}
