package nm.uty.demo;

//import nm.uty.demo.utils.MyRouter;

import nm.uty.demo.utils.MyWatcher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableAsync
@EnableScheduling
//@PropertySource("classpath:application.properties")
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);
//        ApplicationContext ctx=SpringApplication.run(DemoApplication.class, args);
//        CamelContext camelContext=(CamelContext)ctx.getBean("camelContext");
//        camelContext.addRoutes(new MyRouter());
//        camelContext.start();
////        Thread.sleep(1000);
//        camelContext.stop();
    }

    @Bean
    CommandLineRunner lookup(MyWatcher myWatcher) {
        return args -> {
            myWatcher.myWatcher();
        };
    }

}
