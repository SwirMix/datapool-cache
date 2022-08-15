package org.datapool.core.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.ignite.IgniteCache;
import org.springframework.web.multipart.MultipartFile;

import javax.cache.Cache;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class PersistenceCsvService {
    private File baseFolder;

    public File importCache(IgniteCache<Integer, Map> cache, String[] columns, String cacheName, String project) throws IOException {
        File resultFile = prepareFilePath(cacheName, project);
        Iterator<Cache.Entry<Integer, Map>> iterator = cache.iterator();
        try (BufferedWriter writer = Files.newBufferedWriter(resultFile.toPath())) {
            try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(columns))){
                while (iterator.hasNext()) {
                    Cache.Entry<Integer, Map> entry = iterator.next();
                    csvPrinter.printRecord(((HashMap<String,String>) entry.getValue()).values());
                }
            }
        }
        return resultFile;
    }

    public void saveCsvFile(MultipartFile file, String project) {
        try {
            File projectDir = baseFolder.toPath().resolve(project).toFile();
            if (!projectDir.exists()) {
                projectDir.mkdir();
            }
            Files.copy(file.getInputStream(), baseFolder.toPath().resolve(project).resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public File getBaseFolder() {
        return baseFolder;
    }

    public PersistenceCsvService setBaseFolder(File baseFolder) {
        this.baseFolder = baseFolder;
        return this;
    }

    public PersistenceCsvService(){

    }

    public File prepareFilePath(String fileName, String project){
        return baseFolder.toPath().resolve(project).resolve(fileName).toFile();
    }

    public PersistenceCsvService(File csvFolder){
        this.baseFolder = csvFolder;
    }

    public String[] fileNames(){
        return baseFolder.list();
    }

    public ArrayList<File> getFiles(String projectId){
        File[] files = new File(baseFolder, projectId).listFiles();
        ArrayList<File> results = new ArrayList<>();
        for (File item : files){
            if (item.isDirectory()){
                for (File projectFile : item.listFiles()){
                    if (projectFile.isFile()){
                        results.add(projectFile);
                    }
                }
            }
            results.add(item);
        }
        return results;
    }

    public ArrayList<File> getFiles(){
        File[] files = baseFolder.listFiles();
        ArrayList<File> results = new ArrayList<>();
        for (File item : files){
            if (item.isDirectory()){
                for (File projectFile : item.listFiles()){
                    if (projectFile.isFile()){
                        results.add(projectFile);
                    }
                }
            }
        }
        return results;
    }
}
