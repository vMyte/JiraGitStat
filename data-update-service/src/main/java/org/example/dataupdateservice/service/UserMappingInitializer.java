package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.model.dto.UserGitHubDTO;
import org.example.dataupdateservice.model.dto.UserJiraDTO;
import org.example.dataupdateservice.model.entity.UserMapping;
import org.example.dataupdateservice.repository.UserMappingRepo;
import org.example.dataupdateservice.config.GitHubConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserMappingInitializer {
    private final WebClient gitHubWebClient;
    private final GitHubConfig gitHubConfig;

    private final WebClient jiraWebClient;
    private final UserMappingRepo userMappingRepo;

    //@PostConstruct
    public void loadUserMapping() {
       Map<String,String> gitHubMap = new HashMap<>();

        List<UserGitHubDTO> usersGitHubList = gitHubWebClient.get()
                .uri("/repos/{owner}/{repo}/commits",
                        gitHubConfig.getOwner(),
                        gitHubConfig.getRepos())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserGitHubDTO>>() {
                })
                .block();

        for(var elem: usersGitHubList){
            UserGitHubDTO.Committer tmp = elem.getCommit().getCommitter();
            String email = tmp.getEmail();
            email = email.toLowerCase();
            email = email.replaceAll("[^a-z0-9.@]","");
            String gitUsername = tmp.getName();
            gitHubMap.put(email,gitUsername);
           // System.out.println(email + " " + gitUsername);
        }

        System.out.println();
        List<UserJiraDTO> usersJiraList = jiraWebClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJiraDTO>>() {
                })
                .block();

        for(var elem: usersJiraList){
            String email = elem.getEmailAddress();
           // System.out.println(elem.getDisplayName());
            if(email != null) {
                email = email.toLowerCase();
                if (gitHubMap.containsKey(email)) {
                    UserMapping userMapping = new UserMapping();
                    userMapping.setEmail(email);
                    userMapping.setGithubUsername(gitHubMap.get(email));
                    userMapping.setJiraUsername(elem.getDisplayName());
                    userMappingRepo.save(userMapping);
                    //System.out.println(userMapping + " -> было добавлено!");
                }
            }
        }
    }
}
