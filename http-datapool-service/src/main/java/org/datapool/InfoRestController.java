package org.datapool;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.cluster.ClusterNode;
import org.datapool.core.DataSources;
import org.datapool.core.Strategy;
import org.datapool.core.cache.CacheMetadata;
import org.datapool.core.cache.dto.StaticCacheKey;
import org.datapool.core.cache.dto.StaticCacheValue;
import org.datapool.core.csv.PersistenceCsvService;
import org.datapool.core.jwt.TokenObject;
import org.datapool.core.jwt.impl.JwtService;
import org.datapool.dto.ErrorMessage;
import org.datapool.dto.ParametersResponse;
import org.datapool.dto.PostStaticParameters;
import org.datapool.dto.Result;
import org.datapool.services.DataImportService;
import org.datapool.services.DatapoolManager;
import org.datapool.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/")
public class InfoRestController {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private ExecutorService reloadExecutor = Executors.newSingleThreadExecutor();
    @Autowired
    private PersistenceCsvService persistenceCsvService;
    @Autowired
    private DataImportService dataImportService;
    @Autowired
    private DatapoolManager datapoolManager;
    @Autowired
    private Ignite ignite;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    public boolean checkProjectByToken(String remoteToken, StaticCacheKey key){
        TokenObject tokenData = jwtService.decryptToken(remoteToken);
        List projects = userService.getProjectsList(tokenData.getUserId());
        if (projects.contains(key.getProject())) {
            return true;
        } else return false;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/static/create")
    public ResponseEntity postSingleStaticEntity(
            @RequestBody(required = true) PostStaticParameters parameters,
            @RequestHeader(required = true) String token
    ){
        StaticCacheValue value = parameters.getValue();
        value.setUpdateDate(new Date());
        if (checkProjectByToken(token, parameters.getKey())) {
            PostStaticParameters response = datapoolManager.updateStaticParameter(parameters.getKey().setCacheUUID(UUID.randomUUID().toString()), parameters.getValue());
            return new ResponseEntity(new ParametersResponse()
                    .setStatus(Result.SUCCESS)
                    .setErrorMessage(new ErrorMessage("OK"))
                    .setParams(response)
                    .setStrategy(Strategy.UNIQUE), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorMessage("permission denied"), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/static/single")
    public ResponseEntity deleteSingleStaticEntity(
            @RequestParam(required = true) String cacheUuid,
            @RequestParam(required = true) String projectId,
            @RequestHeader(required = true) String token
    ){
        StaticCacheKey key = new StaticCacheKey(projectId, cacheUuid);
        if (checkProjectByToken(token, key)) {
            datapoolManager.deleteStaticParameter(new StaticCacheKey(projectId, cacheUuid));
            return new ResponseEntity(new ErrorMessage("success"), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorMessage("permission denied"), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PatchMapping("/static/update")
    public ResponseEntity updateSingleStaticEntity(
            @RequestBody(required = true) PostStaticParameters parameters,
            @RequestHeader(required = true) String token
    ){
        StaticCacheValue value = parameters.getValue();
        value.setUpdateDate(new Date());
        if (checkProjectByToken(token, parameters.getKey())){
            PostStaticParameters response = datapoolManager.updateStaticParameter(parameters.getKey(), parameters.getValue());
            return new ResponseEntity(new ParametersResponse()
                    .setStatus(Result.SUCCESS)
                    .setErrorMessage(new ErrorMessage("OK"))
                    .setParams(response)
                    .setStrategy(Strategy.UNIQUE), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorMessage("permission denied"), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/static/caches")
    public ResponseEntity getSingleCaches(
            @RequestParam(required = true) String projectId
    ){
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for (Map parameters : datapoolManager.getAllStaticCaches()){
            if (parameters.get("projectId").equals(projectId)){
                result.add(parameters);
            }
        };
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/cluster")
    public ResponseEntity getCluster() throws SocketException {
        IgniteCluster cluster = ignite.cluster();
        ArrayList<Map> responseBody = new ArrayList<>();
        for (ClusterNode node : cluster.nodes()){
            HashMap<String, Object> item = new HashMap<>();
            item.put("nodeId", node.id().toString());
            item.put("instanceName", node.attribute("org.apache.ignite.ignite.name"));
            item.put("port", node.attribute("TcpCommunicationSpi.comm.tcp.port"));
            item.put("cores", node.attribute("org.apache.ignite.data.streamer.pool.size"));
            item.put("ips",node.addresses());
            responseBody.add(item);
        }
        return new ResponseEntity(responseBody, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/strategy")
    public Strategy[] getAvailableStrategies(){
        return Strategy.values();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/datasources/types")
    public DataSources[] getDataSourcesTypes(){
        return DataSources.values();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/caches")
    public ResponseEntity getCaches(
            @RequestParam(required = false) String project
    ){
        try {
            List<CacheMetadata> caches = datapoolManager.getMetadataCaches();
            if (project!=null && !project.equals("")){
                List<CacheMetadata> sortedData = new ArrayList<>();
                for (CacheMetadata cacheMetadata : caches){
                    try {
                        if (cacheMetadata.getBaseProject().equals(project)){
                            sortedData.add(cacheMetadata);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        datapoolManager.deleteCacheData(cacheMetadata.getCacheName());
                    }

                }
                return new ResponseEntity(sortedData, HttpStatus.OK);
            } else {
                return new ResponseEntity(caches, HttpStatus.OK);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(new ErrorMessage("internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/caches/drop")
    public ResponseEntity dropAllCaches(){
        ArrayList<CacheMetadata> csvCaches = (ArrayList<CacheMetadata>) datapoolManager.getAllCaches();
        reloadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                String[] csvFiles = persistenceCsvService.fileNames();
                for (CacheMetadata cache : csvCaches){
                    datapoolManager.deleteCacheData(cache.getCacheName());
                }
            }
        });
        return new ResponseEntity(csvCaches, HttpStatus.CREATED);
    }

}
