package nm.uty.demo.utils;

import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.service.ReaderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MyWatcher {
    //    private Reader reader;
    @Value("${inputFolder}")
    private String inPath;


    private ReaderServiceImpl readerService;

    @Autowired
    public MyWatcher(ReaderServiceImpl readerService) {
        this.readerService = readerService;
    }
//    public MyWatcher(Reader reader) {
//        this.reader = reader;
//    }

    public void myWatcher() {
        try {
            WatchService watchService
                    = null;

            watchService = FileSystems.getDefault().newWatchService();


//            Path path = Paths.get("/home/muzaffar/test");
            Path path = Paths.get(inPath);

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE);
//                    StandardWatchEventKinds.ENTRY_DELETE,
//                    StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            String filePath = "";
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                    log.info("Event kind:" + event.kind()
                            + ". File affected: " + event.context() + ".");
//                    readerService.reader(inPath + File.separator + event.context());
//                    Thread.sleep(5000);

                    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
                    ses.schedule(() -> readerService.reader(inPath + File.separator + event.context()),
                            5000,
                            TimeUnit.MILLISECONDS);
                    ses.shutdown();
                }
                key.reset();
            }
        } catch (
                IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try {
//            WatchService watchService
//                    = null;
//
//            watchService = FileSystems.getDefault().newWatchService();
//
//
//            Path path = Paths.get("/home/muzaffar/test");
//
//            path.register(
//                    watchService,
//                    StandardWatchEventKinds.ENTRY_CREATE);
////                    StandardWatchEventKinds.ENTRY_DELETE,
////                    StandardWatchEventKinds.ENTRY_MODIFY);
//
//            WatchKey key;
//            while ((key = watchService.take()) != null) {
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    System.out.println(
//                            "Event kind:" + event.kind()
//                                    + ". File affected: " + event.context() + ".");
////                    Reader reader = new Reader();
////                    reader.reader(path + "/" + event.context());
//                }
//                key.reset();
//            }
//        } catch (
//                IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
