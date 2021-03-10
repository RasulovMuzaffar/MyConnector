package nm.uty.demo.config;

import lombok.extern.slf4j.Slf4j;
//import nm.uty.demo.utils.Reader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Configuration
public class MonitoringConfig {
    @Value("${monitoring-folder}")
    private String folderPath;

    @Bean
    public WatchService watchService() {
        log.debug("MONITORING_FOLDER: {}", folderPath);
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(folderPath);

            if (!Files.isDirectory(path)) {
                throw new RuntimeException("incorrect monitoring folder: " + path);
            }

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
        } catch (IOException e) {
            log.error("exception for watch service creation:", e);
        }
        return watchService;
    }
}
