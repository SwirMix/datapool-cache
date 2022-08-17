package org.datapool;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.datapool.core.DataSources;
import org.datapool.core.Strategy;
import org.datapool.core.cache.CacheMetadata;
import org.datapool.core.cache.dto.StaticCacheKey;
import org.datapool.core.cache.dto.StaticCacheValue;
import org.datapool.core.jwt.impl.JwtService;
import org.datapool.dto.*;
import org.datapool.services.BusyException;
import org.datapool.services.DatapoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/datapool")
public class DatapoolRestController {
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private DatapoolManager datapoolManager;
    @Autowired
    private Ignite ignite;
    @Autowired
    private PrometheusMetrics metrics;
    @Autowired
    private JwtService jwtService;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/static/single")
    public ResponseEntity getSingleStaticEntity(
            @RequestParam(required = true) String cacheUuid,
            @RequestParam(required = true) String projectId,
            @RequestParam(required = true) String consumer,
            @RequestHeader(required = true) String remoteToken
    ){
        StaticCacheKey key = new StaticCacheKey(projectId, cacheUuid);
        if (checkRemoteTokenStaticCachePerm(remoteToken, key)) {
            metrics.incrementGetCacheMetric(projectId + "_" + cacheUuid, consumer);
            StaticCacheValue value = datapoolManager.getStaticParameter(key);
            return new ResponseEntity(new ParametersResponse()
                    .setStatus(Result.SUCCESS)
                    .setErrorMessage(new ErrorMessage("OK"))
                    .setParams(value)
                    .setStrategy(Strategy.UNIQUE), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorMessage("permission denied"), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/parameters/single")
    public ResponseEntity getSingleEntity(
            @RequestParam(required = true) String cacheName,
            @RequestParam(required = true) Integer key,
            @RequestParam(required = true) String consumer,
            @RequestHeader(required = true) String remoteToken
    ){
        if (checkRemoteToken(remoteToken, cacheName)) {
            metrics.incrementGetCacheMetric(cacheName, consumer);
            datapoolManager.addConsumer(cacheName, consumer);
            IgniteCache<Object, Object> cache = ignite.cache(cacheName);
            return new ResponseEntity(new ParametersResponse()
                    .setStatus(Result.SUCCESS)
                    .setErrorMessage(new ErrorMessage("OK"))
                    .setParams(cache.get(key))
                    .setStrategy(Strategy.UNIQUE), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorMessage("permission denied"), HttpStatus.FORBIDDEN);
        }
    }

    public boolean checkRemoteToken(String remoteToken, String cacheName){
        Map tokenData = jwtService.decryptRemoteToken(remoteToken);
        CacheMetadata metadata = datapoolManager.getCacheMetadata(cacheName);
        List projects = (List) tokenData.get("projects");
        if (((List<String>)tokenData.get("projects")).contains(metadata.getBaseProject())) {
            return true;
        } else return false;
    }

    public boolean checkRemoteTokenStaticCachePerm(String remoteToken, StaticCacheKey key){
        Map tokenData = jwtService.decryptRemoteToken(remoteToken);
        List projects = (List) tokenData.get("projects");
        if (((List<String>)tokenData.get("projects")).contains(key.getProject())) {
            return true;
        } else return false;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/parameters/single")
    public ResponseEntity postSingleEntity(
            @RequestBody PostParameters parameters,
            @RequestHeader(required = true) String remoteToken
    ){
        if (checkRemoteToken(remoteToken, parameters.getCacheName())) {
            try {
                Map data = datapoolManager.postCacheData(parameters);
                metrics.incrementPutCacheMetric(parameters.getCacheName());
                return new ResponseEntity(new ParametersResponse()
                        .setStatus(Result.SUCCESS)
                        .setErrorMessage(new ErrorMessage("OK"))
                        .setParams(data)
                        .setStrategy(Strategy.UNIQUE), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity(new ErrorMessage("invalid columns"), HttpStatus.BAD_REQUEST);
            }
        } else return new ResponseEntity(new ErrorMessage("permission denied"), HttpStatus.FORBIDDEN);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/parameters")
    public ResponseEntity getNextRow(
            @RequestParam(required = true) String cacheName,
            @RequestParam(required = true) Strategy strategy,
            @RequestParam(required = true) String consumer,
            @RequestParam(required = false) Integer size,
            @RequestHeader(required = true) String remoteToken
    ){
        if (checkRemoteToken(remoteToken, cacheName)) {
            CacheMetadata cacheMetadata = datapoolManager.getCacheMetadata(cacheName);
            if (cacheMetadata == null) {
                return new ResponseEntity(
                        new ErrorMessage("cache not found"),
                        HttpStatus.NOT_FOUND);
            }
            switch (strategy) {
                case UNIQUE:
                case RANDOM:
                    try {
                        metrics.incrementGetCacheMetric(cacheName, consumer);
                        datapoolManager.addConsumer(cacheName, consumer);
                        Map<String, Object> row = datapoolManager.getRow(cacheName, strategy);
                        if (row != null) {
                            return new ResponseEntity(new ParametersResponse()
                                    .setStatus(Result.SUCCESS)
                                    .setErrorMessage(new ErrorMessage("OK"))
                                    .setParams(row)
                                    .setStrategy(strategy), HttpStatus.OK);
                        } else return new ResponseEntity(new ParametersResponse()
                                .setStrategy(strategy)
                                .setStatus(Result.ERROR)
                                .setParams(null)
                                .setErrorMessage(new ErrorMessage("internal error")), HttpStatus.INTERNAL_SERVER_ERROR
                        );
                    } catch (Exception e) {
                        return new ResponseEntity(new ParametersResponse()
                                .setStrategy(strategy)
                                .setStatus(Result.ERROR)
                                .setParams(null)
                                .setErrorMessage(new ErrorMessage("internal error")), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                case BATCH_UNIQUE:
                case BATCH_RANDOM:
                    metrics.incrementGetBatchCacheMetric(cacheName, consumer, size);
                    datapoolManager.addConsumer(cacheName, consumer);
                    if (size != null) {
                        if (size > 0) {
                            return new ResponseEntity(new ParametersResponse()
                                    .setStrategy(strategy)
                                    .setStatus(Result.SUCCESS)
                                    .setParams(datapoolManager.getBatch(size, cacheName, strategy))
                                    .setErrorMessage(new ErrorMessage("OK")), HttpStatus.OK);
                        } else return new ResponseEntity(new ParametersResponse()
                                .setStrategy(strategy)
                                .setStatus(Result.ERROR)
                                .setParams(null)
                                .setErrorMessage(new ErrorMessage("negative value")), HttpStatus.BAD_REQUEST);
                    } else return new ResponseEntity(new ParametersResponse()
                            .setStrategy(strategy)
                            .setStatus(Result.ERROR)
                            .setParams(null)
                            .setErrorMessage(new ErrorMessage("internal error")), HttpStatus.BAD_REQUEST);

                default:
                    return new ResponseEntity(new ParametersResponse()
                            .setStrategy(strategy)
                            .setStatus(Result.ERROR)
                            .setParams(null)
                            .setErrorMessage(new ErrorMessage("not supported")), HttpStatus.NOT_FOUND);
            }
        } else return new ResponseEntity(new ErrorMessage("permission denied for this cache"), HttpStatus.FORBIDDEN);
    }

    /*{
        "query": "select * from bookings.aircrafts_data",
            "connectionProperties": {
        "url": "jdbc:postgresql://localhost:5432/demo?currentSchema=bookings",
                "login": "perfcona",
                "password": "perfcona"
    },
        "type": "POSTGRESQL",
            "cacheName": "aircraft",
            "projectId": "20c794f4-46d2-46a1-af19-5efbd1addf5d"
    }
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/cache/create")
    public ResponseEntity createCacheRequest(
            @RequestBody CreateCacheRequest request,
            @RequestHeader String token
    ) {
        if (request.getType().equals(DataSources.RUNTIME)){
            return new ResponseEntity(new ErrorMessage("pls, use only for " + DataSources.POSTGRESQL.name()), HttpStatus.BAD_REQUEST);
        }
        try {
            MetadataResponse response = datapoolManager.initCache(request);
            return new ResponseEntity(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new ErrorMessage("cache already exist"), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/cache/runtime/create")
    public ResponseEntity createRuntimeCacheRequest(@RequestBody CreateRuntimeCacheRequest request) {
        CreateCacheRequest createCacheRequest = new CreateCacheRequest();
        createCacheRequest.setQuery("no query");
        createCacheRequest.setType(DataSources.RUNTIME);
        createCacheRequest.setProjectId(request.getProjectId());
        createCacheRequest.setCacheName(request.getProjectId() + "_" + request.getCacheName());
        try {
            MetadataResponse response = datapoolManager.initRuntimeCache(createCacheRequest, request.getColumns());
            return new ResponseEntity(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new ErrorMessage("cache already exist"), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/cache/{cacheName}")
    public ResponseEntity getCacheMetadata(@PathVariable String cacheName){
        CacheMetadata metadata = datapoolManager.getCacheMetadata(cacheName);
        if (metadata==null) return new ResponseEntity(new ErrorMessage("not found"), HttpStatus.NOT_FOUND);
        else return new ResponseEntity(metadata, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/cache/{cacheName}")
    public ResponseEntity deleteCache(@PathVariable String cacheName){
        try {
            datapoolManager.deleteCacheData(cacheName);
        } catch (Exception e){
            return new ResponseEntity(new ErrorMessage("delete cache error [ " + cacheName + " ]"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(new ErrorMessage("success"), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/cache/{cacheName}")
    public ResponseEntity reloadCache(@PathVariable String cacheName){
        try {
            MetadataResponse response = datapoolManager.reloadCache(cacheName);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (BusyException e) {
            return new ResponseEntity(new ErrorMessage("cache is busy. Wait READY"), HttpStatus.FORBIDDEN);
        } catch (ExecutionException e) {
            return new ResponseEntity(new ErrorMessage("internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            return new ResponseEntity(new ErrorMessage("internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
