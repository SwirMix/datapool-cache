import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.datapool.core.csv.PersistenceCsvService;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class SimpleReader {
    private final String file = "/home/ponchick/Desktop/datapool-service/service-source/data/users.csv";
    private final String csvFolder = "/home/ponchick/Desktop/datapool-service/service-source/data";
    private PersistenceCsvService service;

    //@BeforeTest
    public void init(){
        service = new PersistenceCsvService(new File(csvFolder));
    }

    //@Test
    public void getFileNames(){
        for (String file : service.fileNames()){
            System.out.println(file);
        }
    }

    //@Test
    public void readerTest() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))){
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
            for (CSVRecord record : parser){
                System.out.println(record);
            }
        }
    }

    //@Test
    public void simpleWriter() throws IOException {
        for (int fileNum = 0; fileNum < 100; fileNum++){
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("sample-" + fileNum + ".csv"))) {
                try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("ID",
                                "login",
                                "pass",
                                "sample-1",
                                "sample-2",
                                "sample-3",
                                "sample-4",
                                "sample-4")
                        )
                    )
                {
                    for (int i = 0; i < 1000000; i++){
                        csvPrinter.printRecord(
                                i,
                                "ponchick-" + i,
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString()
                        );
                        csvPrinter.flush();
                    }

                }
            }
        }

    }
}
