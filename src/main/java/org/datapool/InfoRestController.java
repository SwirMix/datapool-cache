package org.datapool;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.cluster.BaselineNode;
import org.apache.ignite.cluster.ClusterNode;
import org.datapool.core.DataSources;
import org.datapool.core.Strategy;
import org.datapool.core.cache.CacheMetadata;
import org.datapool.core.csv.PersistenceCsvService;
import org.datapool.dto.ErrorMessage;
import org.datapool.services.DataImportService;
import org.datapool.services.DatapoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
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
