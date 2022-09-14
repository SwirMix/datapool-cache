package org.datapool.services;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.datapool.core.CacheStatus;
import org.datapool.core.DataSources;
import org.datapool.core.Strategy;
import org.datapool.core.cache.CacheFactory;
import org.datapool.core.cache.CacheMetadata;
import org.datapool.core.cache.dto.StaticCacheKey;
import org.datapool.core.cache.dto.StaticCacheValue;
import org.datapool.core.csv.PersistenceCsvService;
import org.datapool.core.datapool.Datapool;
import org.datapool.core.datapool.ServiceFactory;
import org.datapool.dto.CreateCacheRequest;
import org.datapool.dto.MetadataResponse;
import org.datapool.dto.PostParameters;
import org.datapool.dto.PostStaticParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.datapool.NodeConfiguration.STATIC_CACHE_NAME;

@Service
public class DatapoolManager{
    @Autowired
    private Ignite ignite;
    @Autowired
    private DataImportService importService;
    @Autowired
    private PersistenceCsvService csvService;
    private IgniteCache<StaticCacheKey, StaticCacheValue> staticParameters;

    private ConcurrentHashMap<Strategy, Datapool> datapools = new ConcurrentHashMap<>();

    public DatapoolManager(){

    }

    private IgniteCache<StaticCacheKey, StaticCacheValue> initStaticCache(){
        if (staticParameters==null){
            staticParameters = ignite.getOrCreateCache(
                    CacheFactory.prepareDefaultStaticCacheConfiguration(STATIC_CACHE_NAME)
            );
        }
        return staticParameters;
    }

    public StaticCacheValue getStaticParameter(StaticCacheKey key){
        return initStaticCache().get(key);
    }

    public PostStaticParameters updateStaticParameter(StaticCacheKey key, StaticCacheValue value){
        initStaticCache().put(key, value);
        return new PostStaticParameters(key, value);
    }

    public void deleteStaticParameter(StaticCacheKey key){
        initStaticCache().remove(key);
    }

    public Map postCacheData(PostParameters postParameters){
        CacheMetadata cacheMetadata = importService.getCacheMetadata(postParameters.getCacheName());
        String[] cacheColumns = cacheMetadata.getColumns();
        Map<String, String> postData = new HashMap<>();
        for (String column : cacheColumns){
            String value = postParameters.getParameters().get(column);
            if (value==null) throw new InvalidParameterException();
            postData.put(column, value);
        }
        int key = cacheMetadata.getNextNum();
        if (key < 0) {
            key = 1;
            cacheMetadata.setRowCount(1);
        }

        importService.updateCacheMetadata(cacheMetadata);
        IgniteCache<Integer, Map> cache = ignite.cache(cacheMetadata.getCacheName());
        cache.put(key, postData);
        return cache.get(key);
    }

    public List<CacheMetadata> getCsvCaches(){
        IgniteCache<String, CacheMetadata> caches = importService.getCaches();
        ArrayList arrayList = new ArrayList();
        Iterator<Cache.Entry<String, CacheMetadata>> iterator = caches.iterator();
        while (iterator.hasNext()){
            Cache.Entry<String, CacheMetadata> entry = iterator.next();
            CacheMetadata metadata = entry.getValue();
            if (metadata.getType().equals(DataSources.CSV)){
                arrayList.add(metadata);
            }
        }
        return arrayList;
    }

    public List<CacheMetadata> getCsvCaches(String projectId){
        IgniteCache<String, CacheMetadata> caches = importService.getCaches();
        ArrayList arrayList = new ArrayList();
        Iterator<Cache.Entry<String, CacheMetadata>> iterator = caches.iterator();
        while (iterator.hasNext()){
            Cache.Entry<String, CacheMetadata> entry = iterator.next();
            CacheMetadata metadata = entry.getValue();
            if (metadata.getType().equals(DataSources.CSV) && metadata.getBaseProject().equals(projectId)){
                arrayList.add(metadata);
            }
        }
        return arrayList;
    }

    public List<CacheMetadata> getAllCaches(){
        IgniteCache<String, CacheMetadata> caches = importService.getCaches();
        ArrayList arrayList = new ArrayList();
        Iterator<Cache.Entry<String, CacheMetadata>> iterator = caches.iterator();
        while (iterator.hasNext()){
            Cache.Entry<String, CacheMetadata> entry = iterator.next();
            CacheMetadata metadata = entry.getValue();
            arrayList.add(metadata);
        }
        return arrayList;
    }

    public List<Map<String, Object>> getAllStaticCaches(){
        IgniteCache<StaticCacheKey, StaticCacheValue> caches = initStaticCache();
        ArrayList<Map<String,Object>> arrayList = new ArrayList();
        Iterator<Cache.Entry<StaticCacheKey, StaticCacheValue>> iterator = caches.iterator();
        while (iterator.hasNext()){
            Cache.Entry<StaticCacheKey, StaticCacheValue> entry = iterator.next();
            StaticCacheValue metadata = entry.getValue();
            StaticCacheKey key = entry.getKey();
            Map<String, Object> item = new HashMap<>();
            item.put("name", metadata.getName());
            item.put("cacheUUID", key.getCacheUUID());
            item.put("projectId", key.getProject());
            item.put("lastUpdate", metadata.getUpdateDate());
            item.put("parameters", metadata.getParameters());
            arrayList.add(item);

        }
        return arrayList;
    }

    public Map updateCacheData(PostParameters postParameters, int key){
        CacheMetadata cacheMetadata = importService.getCacheMetadata(postParameters.getCacheName());
        IgniteCache<Integer, Map> cache = ignite.cache(cacheMetadata.getCacheName());
        cache.put(key, postParameters.getParameters());
        return cache.get(key);
    }

    public void deleteCacheData(String cacheName){
        importService.deleteCacheMetadata(cacheName);
        IgniteCache<Integer, Map> cache = ignite.cache(cacheName);
        if (cache!=null){
            cache.destroy();
        }

    }

    public MetadataResponse initCache(CreateCacheRequest createCacheRequest) throws Exception {
        if (checkPool(createCacheRequest.getProjectId() + "_" + createCacheRequest.getCacheName())) throw new Exception();
        else {
            if (createCacheRequest.getColumns()==null){
                createCacheRequest.setColumns(new ArrayList<>());
            }
            return initCache(
                    createCacheRequest.getQuery(),
                    createCacheRequest.getConnectionProperties(),
                    createCacheRequest.getType(),
                    createCacheRequest.getProjectId() + "_" + createCacheRequest.getCacheName(),
                    createCacheRequest.getColumns().toArray(new String[0]),
                    createCacheRequest.getProjectId()
            );
        }
    }

    public MetadataResponse initRuntimeCache(CreateCacheRequest createCacheRequest, String[] columns) throws Exception {
        if (checkPool(createCacheRequest.getCacheName())) throw new Exception();
        else {
            return initCache(
                    createCacheRequest.getQuery(),
                    createCacheRequest.getConnectionProperties(),
                    createCacheRequest.getType(),
                    createCacheRequest.getCacheName(),
                    columns,
                    createCacheRequest.getProjectId()
            );
        }
    }

    public MetadataResponse initCache(String query, Properties connectionProperties, DataSources type, String cacheName, String[] columns, String project){
        MetadataResponse response = new MetadataResponse();
        Future<CacheMetadata> future = null;
        IgniteCache metadataCache = importService.getMetadataIgniteCache();
        switch (type){
            case POSTGRESQL:
                CacheMetadata cacheMetadata = importService.buildCacheMetadata(query, connectionProperties, type, cacheName);
                cacheMetadata.setBaseProject(project);
                metadataCache.put(cacheName, cacheMetadata);
                future = importService.importProcessing(cacheMetadata);
                response.setCacheMetadata(importService.getMetadataIgniteCache().get(cacheName));
                response.setFuture(future);
                break;
            case RUNTIME:
                CacheMetadata runtimeCache = importService.buildCacheMetadata(query, connectionProperties, type, cacheName);
                runtimeCache.setStatus(CacheStatus.READY);
                runtimeCache.setRowCount(0);
                runtimeCache.setType(DataSources.RUNTIME);
                runtimeCache.setColumns(columns);
                runtimeCache.setBaseProject(project);
                metadataCache.put(runtimeCache.getCacheName(), runtimeCache);
                ignite.getOrCreateCache(CacheFactory.prepareDefaultCacheConfiguration(runtimeCache.getCacheName()));
                response.setCacheMetadata(runtimeCache);
                response.setFuture(null);
                break;
        }

        return response;
    }

    public MetadataResponse reloadCache(String cacheName) throws BusyException, ExecutionException, InterruptedException {
        Future<CacheMetadata> future = importService.reloadCache(cacheName);
        MetadataResponse response = new MetadataResponse();
        response.setCacheMetadata(importService.getMetadataIgniteCache().get(cacheName));
        response.setFuture(future);
        return response;
    }

    public CacheMetadata getCacheMetadata(String cacheName){
        return importService.getCacheMetadata(cacheName);
    }

    public void addConsumer(String cacheName, String consumer){
        CacheMetadata metadata = getCacheMetadata(cacheName);
        List<String> consumers = metadata.getConsumers();
        if (!consumers.contains(consumer)){
            consumers.add(consumer);
            metadata.setConsumers(consumers);
            importService.updateCacheMetadata(metadata);
        }
    }

    public List<CacheMetadata> getMetadataCaches(){
        List cacheList = new ArrayList<>();
        Iterator<Cache.Entry<String, CacheMetadata>> iterator = importService.getMetadataIgniteCache().iterator();
        while (iterator.hasNext()) {
            Cache.Entry<String, CacheMetadata> cache = iterator.next();
            cacheList.add(cache.getValue());
        }
        return cacheList;
    }

    public DatapoolManager(Ignite ignite, DataImportService importService){
        this.ignite = ignite;
        this.importService = importService;
    }

    public boolean checkPool(String cacheName){
        for (String cache : ignite.cacheNames()){
            if (cache.equals(cacheName)) return true;
        }
        return false;
    }


    public Map<String, Object> getRow(String cacheName, Strategy strategy) {
        Datapool datapool = getDatapool(strategy);
        return datapool.getRow(cacheName);
    }

    public List<Map<String, Object>> getBatch(int size, String cacheName, Strategy strategy) {
        if (strategy==Strategy.BATCH_RANDOM){
            Datapool datapool = getDatapool(Strategy.RANDOM);
            return datapool.getBatch(size, cacheName);
        } else {
            Datapool datapool = getDatapool(Strategy.UNIQUE);
            return datapool.getBatch(size, cacheName);
        }
    }

    private Datapool getDatapool(Strategy strategy){
        Datapool datapool = datapools.get(strategy);
        if (datapool==null){
            datapool = ServiceFactory.getDatapool(strategy, ignite);
            datapools.put(strategy, datapool);
        }
        return datapool;
    }
}
