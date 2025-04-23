package org.example.statservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class StatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatServiceApplication.class, args);
    }

}
