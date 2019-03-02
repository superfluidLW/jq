package com.chendu.jq.webServer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.chendu.jq.core", "com.chendu.jq.webServer"})
@Slf4j
public class JqWebServer {
    public static void main(String[] args) {
        try {
            SpringApplication.run(JqWebServer.class, args);
            Thread.currentThread().join();
        }
        catch (Exception ex){
            log.error("Application starting error, exiting....");
            System.exit(1);
        }
    }
}
