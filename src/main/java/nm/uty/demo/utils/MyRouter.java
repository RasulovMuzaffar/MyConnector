package nm.uty.demo.utils;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component
public class MyRouter extends RouteBuilder {
    @Value(value = "${monitoring-folder}")
    private String inPath;

    @Value(value = "${outputfolder}")
    private String outPath;

    @Override
    public void configure() throws Exception {
        from("file:/C:/Users/m.rasulov/Desktop/MY/testFolder/in")
                .to("file:/C:/Users/m.rasulov/Desktop/out");
    }
}
