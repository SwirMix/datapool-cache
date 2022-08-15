package org.datapool.services;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;
import org.datapool.core.CacheStatus;
import org.datapool.core.DataSources;
import org.datapool.core.cache.CachQueryTask;
import org.datapool.core.cache.CacheFactory;
import org.datapool.core.cache.CacheMetadata;
import org.datapool.core.csv.CsvLoadTask;
import org.datapool.core.csv.PersistenceCsvService;
import org.datapool.core.jdbc.PostgreSqlImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

@Service
public class DataImportService {
    @Autowired
    private Ignite ignite;
    @Autowired
    private PersistenceCsvService csvService;
    private IgniteCache<String, CacheMetadata> metadataIgniteCache;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConcurrentHashMap<String, Future<DataImportService.CsvImportStatus>> importFutures = new ConcurrentHashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(30);

    public Future getFutureInfo(String futureId){
        return importFutures.get(futureId);
    }

    public ConcurrentHashMap<String, Future<DataImportService.CsvImportStatus>> getAllFutures(){
        return importFutures;
    }
    public String importCacheToCsv(String cacheName, String fileName, String project){
        CacheMetadata metadata = getMetadataIgniteCache().get(cacheName);
        List<String> projectList = metadata.getProject();
        if (!projectList.contains(project)){
            projectList.add(project);
            metadata.setProject(projectList);
        }
        if (metadata==null) throw new NotFoundException("cache with name " + cacheName + " not found");
        Future<DataImportService.CsvImportStatus> future = executorService.submit(new ImportCsvFuture(fileName, metadata.getColumns(), ignite.cache(cacheName), project));
        String uuid = UUID.randomUUID().toString();
        importFutures.put(uuid, future);
        return uuid;
    }

    public IgniteCache<String, CacheMetadata> getMetadataIgniteCache() {
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        return metadataIgniteCache;
    }

    public DataImportService(){

    }

    public CacheMetadata getCacheMetadata(String cacheName){
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        return metadataIgniteCache.get(cacheName);
    }

    public IgniteCache<String, CacheMetadata> getCaches(){
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        return metadataIgniteCache;
    }

    public void deleteCacheMetadata(String cacheName){
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        metadataIgniteCache.remove(cacheName);
    }


    public void updateCacheMetadata(CacheMetadata cacheMetadata){
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        metadataIgniteCache.put(cacheMetadata.getCacheName(), cacheMetadata);
    }

    public DataImportService(Ignite ignite){
        this.ignite = ignite;
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
    }

    public Future<CacheMetadata> importData(String query, Properties connectionProperties, DataSources type, String cacheName){
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        CacheMetadata metadata = metadataIgniteCache.get(cacheName);
        if (metadata==null){
            metadata = buildCacheMetadata(query, connectionProperties, type, cacheName);
            return importProcessing(metadata);
        } else {
            return importProcessing(metadata);
        }
    }

    public Future<CacheMetadata> importProcessing(CacheMetadata metadata){
        metadata.setStatus(CacheStatus.BUSY);
        metadataIgniteCache.put(metadata.getCacheName(), metadata);
        switch (metadata.getType()){
            case POSTGRESQL:
                PostgreSqlImportService postgreSqlImportService = new PostgreSqlImportService(metadata.getConnectionProperties());
                cacheMetadataInit(metadata);
                CachQueryTask task = new CachQueryTask(metadata, ignite, metadata.getCacheName());
                task.setJdbcService(postgreSqlImportService);
                Future<CacheMetadata> cacheMetadataFuture = executorService.submit(task);
                return cacheMetadataFuture;
            case CSV:
                CsvLoadTask csvLoadTask = new CsvLoadTask(metadata, ignite, csvService.prepareFilePath(metadata.getQuery(), metadata.getBaseProject()));
                csvLoadTask.setImportService(this);
                Future<CacheMetadata> csvLoader = executorService.submit(csvLoadTask);
                return csvLoader;
        }
        throw new UnsupportedOperationException();
    }

    public Future<CacheMetadata> reloadCache(String cacheName) throws BusyException, InterruptedException, ExecutionException {
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        CacheMetadata metadata = metadataIgniteCache.get(cacheName);
        if (metadata.getStatus()!=CacheStatus.CLEANING | metadata.getStatus()!=CacheStatus.DELETED){
            metadata.setStatus(CacheStatus.CLEANING);
            metadata.setRowCount(0);
            metadataIgniteCache.put(cacheName, metadata);
            IgniteCache pool = ignite.cache(cacheName);
            return executorService.submit(new Callable<CacheMetadata>() {
                @Override
                public CacheMetadata call() throws Exception {
                    pool.clear();
                    while (pool.size(CachePeekMode.ALL) > 0){
                        Thread.sleep(1000);
                    }
                    return importProcessing(metadata).get();
                }
            });
        }
        return importProcessing(metadata);
    }

    public CacheMetadata destroyCache(String cacheName){
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        CacheMetadata metadata = metadataIgniteCache.get(cacheName);
        metadata.setStatus(CacheStatus.DELETED);
        IgniteCache cache = ignite.cache(metadata.getCacheName());
        if (cache!=null){
            cache.destroy();
        }
        metadataIgniteCache.put(cacheName, metadata);
        return metadata;
    }

    public void deleteAllData(String cacheName){
        if (metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.prepareMetadataCacheConfiguration());
        }
        destroyCache(cacheName);
        metadataIgniteCache.remove(cacheName);
    }


    public CacheMetadata cacheMetadataInit(CacheMetadata metadata){
        if (this.metadataIgniteCache==null){
            metadataIgniteCache = ignite.getOrCreateCache(CacheFactory.DEFAULT_METADATA_CACHE);
        }
        metadataIgniteCache.put(metadata.getCacheName(), metadata);
        return metadata;
    }

    public static CacheMetadata buildCacheMetadata(String query, Properties connectionProperties, DataSources type, String cacheName){
        CacheMetadata metadata = new CacheMetadata(type, connectionProperties);
        metadata.setQuery(query);
        metadata.setCacheName(cacheName);
        metadata.setStatus(CacheStatus.BUSY);
        metadata.setType(type);
        return metadata;
    }

    public class CsvImportStatus {
        private int status = -1;
        private File resultFile;

        public int getStatus() {
            return status;
        }

        public CsvImportStatus setStatus(int status) {
            this.status = status;
            return this;
        }

        public File getResultFile() {
            return resultFile;
        }

        public CsvImportStatus setResultFile(File resultFile) {
            this.resultFile = resultFile;
            return this;
        }
    }

    public class ImportCsvFuture implements Callable<CsvImportStatus> {
        private CsvImportStatus csvImportStatus = new CsvImportStatus();
        private IgniteCache<Integer, Map> cache;
        private String cacheName;
        private String[] columns;
        private String project;

        public ImportCsvFuture(String cacheName, String[] columns, IgniteCache<Integer, Map> cache, String project){
            this.cache = cache;
            this.cacheName = cacheName;
            this.columns = columns;
            this.project = project;
        }

        @Override
        public CsvImportStatus call() throws Exception {
            try {
                File file = csvService.importCache(cache, columns, cacheName, project);
                csvImportStatus.setResultFile(file);
                csvImportStatus.status = 0;
                return csvImportStatus;
            } catch (Exception e){
                e.printStackTrace();
                return csvImportStatus;
            }
        }
    }
}
