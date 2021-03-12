package nm.uty.demo.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
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
//    @Value(value = "${outputfolder}")
    private String myPath = "C:\\Users\\m.rasulov\\Desktop\\MY\\testFolder\\out";

    @Scheduled(cron = "0/10 * * * * ?")
    public void scheduleFixedDelayTask() {
        log.info("start scheduler");

//        Path path = Paths.get("src/main/resources/qwerty.txt");
        Path path = Paths.get(myPath + "\\qwerty.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("cp866"))) {
            writer.write("йцу");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
