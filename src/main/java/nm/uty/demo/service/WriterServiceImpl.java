package nm.uty.demo.service;

import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.utils.DataCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class WriterServiceImpl {
    @Value(value = "${outputFolder}")
    private String myPath;
    @Value(value = "${stCL}")
    private int stCodeLarge;
    @Value(value = "${fn}")
    private String fileName;


    private SenderServiceImpl senderService;
    private final DataCache dataCache;

    public WriterServiceImpl(SenderServiceImpl senderService, DataCache dataCache) {
        this.senderService = senderService;
        this.dataCache = dataCache;
    }

    public void writer(List<String> list) {
        dataCache.getIndexes().stream().forEach(idx -> {
            if (dataCache.getIndexesWagons().get(idx) == null || dataCache.getIndexesWagons().get(idx).getWagons().isEmpty()) {
                Path path = Paths.get(myPath + File.separator + fileName);
                try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("cp866"))) {
                    log.info(String.format("(:213 %d:%s 922:)", stCodeLarge, idx));
                    writer.write(String.format("(:213 %d:%s 922:)", stCodeLarge, idx));

                } catch (IOException ex) {
                    log.warn("IOException: " + ex.getMessage());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.warn("InterruptedException: " + e.getMessage());
            }
        });
        log.info("All idxs is sended to ASOUP");
    }
}
