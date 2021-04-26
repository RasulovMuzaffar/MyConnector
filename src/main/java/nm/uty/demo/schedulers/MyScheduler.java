package nm.uty.demo.schedulers;

import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.utils.DataCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;


@Slf4j
@Component
public class MyScheduler {
    private final DataCache dataCache;

    @Value(value = "${outputFolder}")
    private String outputPath;// = "C:\\Users\\m.rasulov\\Desktop\\MY\\testFolder\\out";
    @Value("${inputFolder}")
    private String inPath;
    @Value(value = "${stCL}")
    private int stCodeLarge;
    @Value(value = "${stCS}")
    private int stCodeSmall;
    @Value(value = "${fn}")
    private String fileName;

    public MyScheduler(DataCache dataCache) {
        this.dataCache = dataCache;
    }

        @Scheduled(fixedDelay = 1800000)
//    @Scheduled(fixedDelay = 60000)
    public void scheduleFixedDelayTask() {
        log.info("start scheduler");
        log.info("separator " + File.separator);
        try {
            Arrays.stream(Objects.requireNonNull(new File(inPath).listFiles())).forEach(File::delete);
            log.warn("all files is deleted in directory!!!");
        } catch (Exception e) {
            log.warn("exception in delete files in directory: {}", e.getMessage());
        }
//        Path path = Paths.get("src/main/resources/qwerty.txt");
//        if (!dataCache.getIndexes().isEmpty())
//            dataCache.getIndexes().forEach(i -> {
        Path path = Paths.get(outputPath + File.separator + fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("cp866"))) {
            writer.write(String.format("(:212 %d %d:57:)", stCodeLarge, stCodeSmall));
            log.info("request spr57 has been sent to ASOUP! request: {}" + String.format("(:212 %d %d:57:)", stCodeLarge, stCodeSmall));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.warn("InterruptedException: " + e.getMessage());
        }
//            });
//        else
        log.info("dataCache is empty!!!");
    }
}
