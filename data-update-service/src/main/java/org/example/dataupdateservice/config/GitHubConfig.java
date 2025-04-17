package org.example.dataupdateservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Data
  @Configuration
  @ConfigurationProperties(prefix = "github")

    public class GitHubConfig {
        private  String token;
        private String baseurl;
        private String owner;
        private String repos;

        private String lastDateCommits;

        @Bean
      public WebClient gitHubWebClient(){
            return WebClient
                    .builder()
                    .baseUrl(baseurl)
                    .defaultHeader("Authorization", "Bearer " + token)
                    .codecs(configurer -> {
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10 MB
                    })
                    .build();
        }
    }

