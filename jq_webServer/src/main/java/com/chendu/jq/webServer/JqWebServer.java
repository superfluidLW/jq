package com.chendu.jq.webServer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.chendu.jq.core", "com.chendu.jq.webServer"})
@Slf4j
public class JqWebServer {
    public static void main(String[] args) {
        SpringApplication.run(JqWebServer.class, args);
    }
}
