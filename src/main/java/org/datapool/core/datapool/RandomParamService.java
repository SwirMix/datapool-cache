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
import java.util.Random;

public class RandomParamService implements Service, Datapool {

    @IgniteInstanceResource
    private Ignite ignite;

    @Override
    public Map<String, Object> getRow(String cacheName) {
        IgniteCache<String, CacheMetadata> metadataCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        CacheMetadata metadata = metadataCache.get(cacheName);
        IgniteCache targetCache = ignite.cache(metadata.getCacheName());
        int index = getRandomNumberUsingNextInt(1, metadata.getRowCount());
        if (metadata.getRowCount()==0){
            metadata.setRowCount(targetCache.size());
            metadataCache.put(cacheName, metadata);
        }
        Map<String, Object> params =  (Map<String, Object>) targetCache.get(index);
        params.put("uuid", index);
        if (params==null){
            return params = getRow(cacheName);
        } else {
            return params;
        }
    }



    @Override
    public List<Map<String, Object>> getBatch(int size, String cacheName) {
        IgniteCache<String, CacheMetadata> metadataCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        CacheMetadata metadata = metadataCache.get(cacheName);
        IgniteCache targetCache = ignite.cache(metadata.getCacheName());
        if (metadata.getRowCount()==0){
            metadata.setRowCount(targetCache.size());
            metadataCache.put(cacheName, metadata);
        }
        List<Map<String, Object>> items = new ArrayList<>();
        for (int stIndex = 1; stIndex <= size; stIndex++){
            items.add((Map<String, Object>) getRow(cacheName));
        }
        return items;
    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        if (min==max){
            return min;
        }
        return new Random().nextInt(max - min) + min;
    }
}
