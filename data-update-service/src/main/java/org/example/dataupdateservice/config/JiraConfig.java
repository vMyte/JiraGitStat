package org.example.dataupdateservice.config;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Data
@Configuration
@ConfigurationProperties(prefix = "jira")

public class JiraConfig {
    private String token;
    private String baseurl;
    private String email;

    @Bean
    public WebClient jiraWebClient() {
        String auth = Base64.getEncoder().encodeToString((email + ":" + token).getBytes());

        return WebClient.builder()
                .baseUrl(baseurl)
                .defaultHeader("Authorization", "Basic " + auth)
                .defaultHeader("Accept", "application/json")
                .build();
    }
    //hihihhihhihihih


}
