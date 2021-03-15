package nm.uty.demo.service;

import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.utils.DataCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class WriterServiceImpl {
    @Value(value = "${outputfolder}")
    private String myPath;

    private SenderServiceImpl senderService;
    private final DataCache dataCache;

    public WriterServiceImpl(SenderServiceImpl senderService, DataCache dataCache) {
        this.senderService = senderService;
        this.dataCache = dataCache;
    }

    public void writer(List<String> list) {
        dataCache.getIndexes().stream().forEach(l -> {
            if (dataCache.getIndexesWagons().get(l) == null || dataCache.getIndexesWagons().get(l).isEmpty()) {
                Path path = Paths.get(myPath + "/" + l);
                try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("cp866"))) {
                    writer.write(l);
                    Thread.sleep(1000);
                } catch (IOException ex) {
                    log.warn("IOException: " + ex.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
