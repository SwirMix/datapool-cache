package org.datapool.core.datapool;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.datapool.core.cache.CacheFactory;
import org.datapool.core.cache.CacheMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UniqueCountParamService implements Service, Datapool {
    @IgniteInstanceResource
    private Ignite ignite;

    @Override
    public Map<String, Object> getRow(String cacheName) {
        IgniteCache<String, CacheMetadata> metadataCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        CacheMetadata metadata = metadataCache.get(cacheName);
        IgniteCache<CacheMetadata, Integer> sequential = (IgniteCache<CacheMetadata, Integer>) ignite.getOrCreateCache(CacheFactory.prepareSequentialCache());
        IgniteCache targetCache = ignite.cache(metadata.getCacheName());
        if (metadata.getRowCount()==0){
            metadata.setRowCount(targetCache.size());
            metadataCache.put(cacheName, metadata);
        }
        Integer currentCursor = sequential.get(metadata);
        if (currentCursor==null) currentCursor=1;
        Map<String, Object> item = (Map<String, Object>) targetCache.get(cursorIncrement(metadata, currentCursor, sequential));
        if (item==null){
            item = getRow(cacheName);
            item.put("uuid", currentCursor);
            return item;
        } else {
            item.put("uuid", currentCursor);
            return item;
        }
    }

    @Override
    public List<Map<String, Object>> getBatch(int size, String cacheName) {
        List<Map<String, Object>> items = new ArrayList<>();
        for (int cursor = 0; cursor < size; cursor++){
            items.add((Map<String, Object>) getRow(cacheName));
        }

        return items;
    }

    private Integer cursorIncrement(CacheMetadata metadata, Integer currentCursor, IgniteCache<CacheMetadata, Integer> sequential){
        if (currentCursor == null){
            sequential.put(metadata, 0);
            return 0;
        } else currentCursor +=1;
        if (currentCursor > metadata.getRowCount()){
            currentCursor = 0;
        }
        sequential.put(metadata, currentCursor);
        return currentCursor;
    }

}
