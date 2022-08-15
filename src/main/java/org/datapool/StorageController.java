package org.datapool;

import org.datapool.core.DataSources;
import org.datapool.core.cache.CacheMetadata;
import org.datapool.core.csv.PersistenceCsvService;
import org.datapool.core.jwt.TokenObject;
import org.datapool.dto.CacheImportCsvRq;
import org.datapool.dto.ErrorMessage;
import org.datapool.services.DataImportService;
import org.datapool.services.DatapoolManager;
import org.datapool.services.ProjectService;
import org.datapool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/")
public class StorageController {
    @Autowired
    private DatapoolManager datapoolManager;
    @Autowired
    private PersistenceCsvService persistenceCsvService;
    @Autowired
    private DataImportService dataImportService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    private ExecutorService reloadExecutor = Executors.newSingleThreadExecutor();

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(
            value = "/storage/download/{project}/{file}"
    )
    public @ResponseBody ResponseEntity getFile(
            @PathVariable String file,
            @PathVariable String project
    ) throws IOException {
        File storeFile = persistenceCsvService.prepareFilePath(file, project);
        if (storeFile.exists() && storeFile.isFile()){
            InputStream in = new FileInputStream(storeFile);
            return ResponseEntity.ok()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(new InputStreamResource(in));
        } else {
            return new ResponseEntity<>(new ErrorMessage("not found or invalid type"), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(
            path = "/storage/upload",
            method = RequestMethod.POST
    )
    public ResponseEntity uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "project", required = true) String project,
            @RequestHeader(required = true) String token
    ) {

        String message = "";
        try {
            TokenObject tokenObject = userService.decryptToken(token);
            if (projectService.checkProjectPermissionById(tokenObject.getUserId(), project)){
                try {
                    persistenceCsvService.saveCsvFile(file, project);
                    message = "Uploaded the file successfully: " + file.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ErrorMessage(message));
                }
            } else {
                return new ResponseEntity(new ErrorMessage("Permission denied for this project."), HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            return new ResponseEntity(new ErrorMessage("invalid token"), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/storage/import")
    public ResponseEntity importCacheToCsv(@RequestBody CacheImportCsvRq request) throws ExecutionException, InterruptedException {
        File csv = persistenceCsvService.prepareFilePath(request.getFileName(), request.getProject());
        if (csv.exists() && request.getFileName().equals(csv.getName())) {
            return new ResponseEntity(new ErrorMessage("CSV for current cache already exists. " +
                    "Pls use ovveride flag or use another api"), HttpStatus.BAD_REQUEST);
        }
        String futureId = dataImportService.importCacheToCsv(request.getCacheName(), request.getFileName(), request.getProject());
        return new ResponseEntity("{\"future\": " + "\"" + futureId + "\"}", HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/storage/{project}/{file}")
    public ResponseEntity deleteFile(
            @PathVariable String project,
            @PathVariable String file,
            @RequestHeader String token
    ){
        File csv = persistenceCsvService.prepareFilePath(file, project);
        csv.delete();
        return new ResponseEntity(new ErrorMessage("success"), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/storage/future/{futureId}")
    public ResponseEntity getFutureInfo(@PathVariable String futureId) throws ExecutionException, InterruptedException {
        Future future = dataImportService.getFutureInfo(futureId);
        if (future.isDone()){
            Map response = new HashMap();
            response.put("future", future.get());
            response.put("download", "api/v1/storage/download/" +
                    ( (DataImportService.CsvImportStatus) future.get()).getResultFile().getName());
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(future, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/storage/futures")
    public ResponseEntity getAllFutureInfo(){
        ConcurrentHashMap<String, Future<DataImportService.CsvImportStatus>> futures = dataImportService.getAllFutures();
        return new ResponseEntity(futures, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/storage")
    public ResponseEntity getStorageData(
            @RequestParam(required = false) String storage_id
    ){
        if (storage_id!=null){
            if (!storage_id.equals("")){
                ArrayList<File> files = persistenceCsvService.getFiles(storage_id);
                ArrayList<Map> storageData = new ArrayList<>();
                for (File file : files){
                    if (file.isFile()){
                        Map data = new HashMap();
                        data.put("name", file.getName());
                        data.put("size", file.length());
                        data.put("soragePath", file.getAbsolutePath());
                        data.put("project_id", file.getParentFile().getName());
                        data.put("download", "api/v1/storage/download/" + file.getParentFile().getName() + "/" + file.getName());
                        storageData.add(data);
                    }

                }
                return new ResponseEntity(storageData, HttpStatus.OK);
            }
        }
        ArrayList<File> files = persistenceCsvService.getFiles();
        ArrayList<Map> storageData = new ArrayList<>();
        for (File file : files){
            if (file.isFile()){
                Map data = new HashMap();
                data.put("name", file.getName());
                data.put("size", file.length());
                data.put("soragePath", file.getAbsolutePath());
                data.put("project_id", file.getParentFile().getName());
                data.put("download", "api/v1/storage/download/" + file.getName());
                storageData.add(data);
            }

        }
        return new ResponseEntity(storageData, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/storage/reload")
    public ResponseEntity reloadFromStorage(
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String project
    ){
        try {
            if (fileName!=null){
                File file = persistenceCsvService.prepareFilePath(fileName, project);
                if (file.isFile() && file.exists()){
                    CacheMetadata metadata = datapoolManager.getCacheMetadata(fileName);
                    if (metadata!=null){
                        datapoolManager.deleteCacheData(fileName);
                    }
                    CacheMetadata cacheMetadata = new CacheMetadata(DataSources.CSV, new Properties());
                    cacheMetadata.setQuery(file.getName());
                    cacheMetadata.setCacheName(file.getParentFile().getName() + "_" + fileName);
                    cacheMetadata.setBaseProject(file.getParentFile().getName());
                    List<String> projectList = cacheMetadata.getProject();
                    if (!projectList.contains(project)){
                        projectList.add(project);
                        cacheMetadata.setProject(projectList);
                        cacheMetadata.setBaseProject(project);
                    }
                    dataImportService.cacheMetadataInit(cacheMetadata);
                    dataImportService.importProcessing(cacheMetadata);
                    return new ResponseEntity(cacheMetadata, HttpStatus.CREATED);
                }
            } else {
                ArrayList<CacheMetadata> csvCaches = (ArrayList<CacheMetadata>) datapoolManager.getCsvCaches(project);
                ArrayList<File> csvFiles = persistenceCsvService.getFiles(project);
                reloadExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (File cache : csvFiles){
                            CacheMetadata metadata = datapoolManager.getCacheMetadata(cache.getName());
                            if (metadata!=null){
                                datapoolManager.deleteCacheData(cache.getName());
                            }
                            CacheMetadata cacheMetadata = new CacheMetadata(DataSources.CSV, new Properties());
                            cacheMetadata.setQuery(cache.getName());
                            cacheMetadata.setCacheName(cache.getParentFile().getName() + "_" + cache.getName());
                            List<String> projectList = cacheMetadata.getProject();
                            String project = cache.getParentFile().getName();
                            if (!projectList.contains(project)){
                                projectList.add(project);
                                cacheMetadata.setProject(projectList);
                                cacheMetadata.setBaseProject(project);
                            }
                            dataImportService.cacheMetadataInit(cacheMetadata);
                            dataImportService.importProcessing(cacheMetadata);
                        }
                    }
                });
                return new ResponseEntity(new ErrorMessage(), HttpStatus.CREATED);
            }
        } catch (Exception e){
            return new ResponseEntity(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(new ErrorMessage(), HttpStatus.BAD_REQUEST);
    }
}
