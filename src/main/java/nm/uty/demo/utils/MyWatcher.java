//package nm.uty.demo.utils;
//
//import java.io.IOException;
//import java.nio.file.*;
//
//public class MyWatcher {
//    private Reader reader;
//
//    public MyWatcher(Reader reader) {
//        this.reader = reader;
//    }
//
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
//                    Reader reader = new Reader();
//                    reader.reader(path + "/" + event.context());
//                }
//                key.reset();
//            }
//        } catch (
//                IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
