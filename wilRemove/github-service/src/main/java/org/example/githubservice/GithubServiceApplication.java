package org.example.githubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GithubServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubServiceApplication.class, args);
    }

}
