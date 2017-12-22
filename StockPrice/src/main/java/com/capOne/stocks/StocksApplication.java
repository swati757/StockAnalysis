package com.capOne.stocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRetry
public class StocksApplication {
    private static final Logger log = LoggerFactory.getLogger(StocksApplication.class);

    public static void main(String[] args) {
        log.info("Spring.Profiles.Active :-> " + System.getProperty("spring.profiles.active"));
        SpringApplication.run(StocksApplication.class, args);
        log.info("Started Stocks Microservice...");
    }
}
