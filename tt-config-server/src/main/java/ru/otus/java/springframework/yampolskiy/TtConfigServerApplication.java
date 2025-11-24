package ru.otus.java.springframework.yampolskiy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class TtConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtConfigServerApplication.class, args);
    }

}
