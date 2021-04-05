package nm.uty.demo.schedulers;

import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.utils.DataCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Slf4j
@Component
public class MyScheduler {
    private final DataCache dataCache;

    @Value(value = "${outputFolder}")
    private String outputPath;// = "C:\\Users\\m.rasulov\\Desktop\\MY\\testFolder\\out";
    @Value(value = "${stationCode}")
    private int stCode;

    public MyScheduler(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void scheduleFixedDelayTask() {
        log.info("start scheduler");
//        Path path = Paths.get("src/main/resources/qwerty.txt");
//        if (!dataCache.getIndexes().isEmpty())
//            dataCache.getIndexes().forEach(i -> {
        Path path = Paths.get(outputPath + "/0102000.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("cp866"))) {
            writer.write(String.format("(:212 72010 7201:57:)", stCode));
            log.info("request spr57 has been sent to ASOUP!");
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
