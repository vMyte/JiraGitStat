package org.example.dataupdateservice;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.SpringProperties;

@SpringBootApplication
@EnableDiscoveryClient
public class DataUpdateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataUpdateServiceApplication.class, args);
    }

}
