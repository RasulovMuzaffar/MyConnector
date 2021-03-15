package nm.uty.demo.utils;

import nm.uty.demo.service.ReaderServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
public class MyWatcher {
//    private Reader reader;
    @Value("${monitoring-folder}")
    private String inPath;

    private ReaderServiceImpl readerService;

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
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                    Thread.sleep(1000);
                    readerService.reader(inPath + "/" + event.context());
//                    Reader reader = new Reader();
//                    reader.reader(path + "/" + event.context());
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
