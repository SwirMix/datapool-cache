package org.datapool.core.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.datapool.core.CacheStatus;
import org.datapool.services.AbstractImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.*;

public class CachQueryTask implements Callable{
    private CacheMetadata cacheMetadata;
    private AbstractImportService jdbcService;
    private Ignite ignite;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private String cacheName;

    public IgniteCache getDataCache(){
        IgniteCache<Integer, HashMap<String, Object>> dataCache = ignite.getOrCreateCache(CacheFactory.prepareDefaultCacheConfiguration(cacheName));
        return dataCache;
    }

    public CachQueryTask(CacheMetadata metadata, Ignite ignite, String cacheName){
        this.cacheMetadata = metadata;
        this.ignite = ignite;
        this.cacheName = cacheName;
    }

    public CacheMetadata getCacheMetadata() {
        return cacheMetadata;
    }

    public CachQueryTask setCacheMetadata(CacheMetadata cacheMetadata) {
        this.cacheMetadata = cacheMetadata;
        return this;
    }


    public AbstractImportService getJdbcService() {
        return jdbcService;
    }

    public CachQueryTask setJdbcService(AbstractImportService jdbcService) {
        this.jdbcService = jdbcService;
        return this;
    }

    public static HashMap<String, Object> rowProcessing(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        HashMap<String, Object> row = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        String[] columns = new String[metaData.getColumnCount()];
        for (int index = 0; index < metaData.getColumnCount(); index++){
            columns[index] = metaData.getColumnName(index+1);
            Object value = resultSet.getObject(index+1);
            row.put(metaData.getColumnName(index+1), value);
        }

        return row;
    }

    public void clearCache(){
        this.ignite.getOrCreateCache(CacheFactory.prepareDefaultCacheConfiguration(cacheName)).clear();
    }

    @Override
    public CacheMetadata call() throws Exception {
        IgniteCache<Integer, HashMap<String, Object>> dataCache = getDataCache();
        String query = cacheMetadata.getQuery();
        Connection connection = null;
        try {
            connection = jdbcService.getJdbcConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            cacheMetadata.setStatus(CacheStatus.BUSY);
            while (resultSet.next()){
                HashMap<String, Object> line = rowProcessing(resultSet);
                Integer key = cacheMetadata.getNextNum();
                dataCache.put(key, line);
            }
            cacheMetadata.setRowCount(dataCache.size());
            cacheMetadata.setStatus(CacheStatus.READY);
            connection.close();
        } catch (Exception e){
            e.printStackTrace();
            cacheMetadata.setStatus(CacheStatus.READY);
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            logger.error(e.getMessage());
        }
        cacheMetadata.setRowCount(dataCache.size());
        cacheMetadata.setColumns(dataCache.get(1).keySet().toArray(new String[0]));
        ignite.getOrCreateCache(CacheFactory.DEFAULT_METADATA_CACHE).put(cacheMetadata.getCacheName(), cacheMetadata);
        return cacheMetadata;
    }
}
