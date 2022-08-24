package org.datapool;

import org.apache.ignite.Ignite;
import org.datapool.core.csv.PersistenceCsvService;
import org.datapool.db.UserRepository;
import org.datapool.services.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApplicationStarter implements CommandLineRunner {
    @Autowired
    private PersistenceCsvService persistenceCsvService;
    @Autowired
    private Ignite ignite;
    @Autowired
    private DataImportService dataImportService;
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String[] csvFiles = persistenceCsvService.fileNames();
    }
}