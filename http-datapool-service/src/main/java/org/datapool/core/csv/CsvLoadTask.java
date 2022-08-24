package org.datapool.core.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.datapool.core.CacheStatus;
import org.datapool.core.cache.CacheFactory;
import org.datapool.core.cache.CacheMetadata;
import org.datapool.services.DataImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
public class CsvLoadTask implements Callable {
    private String uuid = UUID.randomUUID().toString();
    private CacheMetadata cacheMetadata;
    private Ignite ignite;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private String cacheName;
    private File dataFile;

    private DataImportService importService;

    public DataImportService getImportService() {
        return importService;
    }

    public CsvLoadTask setImportService(DataImportService importService) {
        this.importService = importService;
        return this;
    }

    public CacheMetadata getCacheMetadata() {
        return cacheMetadata;
    }

    public static Map<String, String> rowProcessing(CSVRecord record) throws SQLException, ClassNotFoundException {
        return record.toMap();
    }

    public CsvLoadTask setCacheMetadata(CacheMetadata cacheMetadata) {
        this.cacheMetadata = cacheMetadata;
        return this;
    }

    public CsvLoadTask(CacheMetadata metadata, Ignite ignite, File dataFile){
        this.cacheMetadata = metadata;
        this.ignite = ignite;
        this.cacheName = metadata.getCacheName();
        this.dataFile = dataFile;
    }

    public IgniteCache getDataCache(){
        IgniteCache<Integer, HashMap<String, Object>> dataCache = ignite.getOrCreateCache(CacheFactory.prepareDefaultCacheConfiguration(cacheName));
        return dataCache;
    }

    @Override
    public CacheMetadata call() throws Exception {
        IgniteCache<Integer, Map<String, String>> dataCache = getDataCache();
        cacheMetadata.setRowCount(-1);
        try (BufferedReader reader = Files.newBufferedReader(dataFile.toPath())) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
            Map<String, Integer> headers = new HashMap<>();
            for (CSVRecord record : parser) {
                Map<String, String> line = rowProcessing(record);
                Integer key = cacheMetadata.getNextNum();
                if (key <= 0){
                    int size = record.size();
                    for (int index = 0; index < size; index++){
                        headers.put(record.get(index), index);
                    }
                    cacheMetadata.setColumns(headers.keySet().toArray(new String[0]));
                } else {
                    for (String headEntity : headers.keySet()) {
                        String value = record.get(headers.get(headEntity));
                        line.put(headEntity, value);
                    }
                    dataCache.put(key, line);
                }
                logger.debug("Cache [ key=" + key +" ] " + cacheMetadata.getCacheName() +": " + line.toString());

            }
        }
        cacheMetadata.setRowCount(dataCache.size());
        cacheMetadata.setStatus(CacheStatus.READY);
        cacheMetadata.setRowCount(dataCache.size());
        IgniteCache<String, CacheMetadata> metadataIgniteCache = importService.getMetadataIgniteCache();
        metadataIgniteCache.put(cacheMetadata.getCacheName(), cacheMetadata);
        //ignite.getOrCreateCache(CacheFactory.DEFAULT_METADATA_CACHE).put(cacheMetadata.getCacheName(), cacheMetadata);
        return cacheMetadata;
    }
}
