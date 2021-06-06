package com.kddimitrov.exchangeClient;

import com.kddimitrov.exchangeClient.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableConfigurationProperties(value = ApplicationConfig.class)
@EnableAsync
@SpringBootApplication
public class
Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
