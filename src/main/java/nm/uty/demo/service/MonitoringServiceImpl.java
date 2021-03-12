//package nm.uty.demo.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//import java.nio.file.WatchEvent;
//import java.nio.file.WatchKey;
//import java.nio.file.WatchService;
//
//@Slf4j
//@Service
////@AllArgsConstructor
//public class MonitoringServiceImpl {
//
//    @Value("${monitoring-folder}")
//    private String folderPath;
//
//    private final WatchService watchService;
//    private final ReaderServiceImpl readerService;
//
//
//    public MonitoringServiceImpl(WatchService watchService, ReaderServiceImpl readerService) {
//        this.watchService = watchService;
//        this.readerService = readerService;
//    }
//
////    @Async
//    @PostConstruct
//    public void launchMonitoring() {
//        log.info("START_MONITORING");
//        try {
//            WatchKey key;
//            while ((key = watchService.take()) != null) {
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    log.debug("Event kind: {}; File affected: {}", event.kind(), event.context());
//                    log.info("Event kind: {}; File affected: {}", event.kind(), event.context());
//                    readerService.reader(folderPath + "/" + event.context());
//                }
//                key.reset();
//            }
//        } catch (InterruptedException e) {
//            log.warn("interrupted exception for monitoring service");
//        }
//    }
//
//    @PreDestroy
//    public void stopMonitoring() {
//        log.info("STOP_MONITORING");
//
//        if (watchService != null) {
//            try {
//                watchService.close();
//            } catch (IOException e) {
//                log.error("exception while closing the monitoring service");
//            }
//        }
//    }
//}
