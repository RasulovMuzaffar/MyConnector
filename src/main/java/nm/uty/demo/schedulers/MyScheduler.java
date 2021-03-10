package nm.uty.demo.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Slf4j
//@Component
@EnableScheduling
public class MyScheduler {

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleFixedDelayTask() {
        log.info("start scheduler");
        Path path = Paths.get("src/main/resources/qwerty.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("cp866"))) {
            writer.write("йцу");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
