package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.client.github.GitHubClient;
import org.example.dataupdateservice.client.jira.JiraClient;
import org.example.dataupdateservice.model.entity.UserMapping;
import org.example.dataupdateservice.model.mapper.common.UserMappingMapper;
import org.example.dataupdateservice.repository.UserMappingRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMappingLoader {
    private final GitHubClient gitHubClient;
    private final JiraClient jiraClient;
    private final UserMappingMapper mapper;
    private final UserMappingRepo userMappingRepo;

    public void loadUserMapping() {
        //Получаем и обрабатываем GitHub пользователей
        Map<String, String> gitHubUsers = gitHubClient.getUsers()
                .blockOptional()
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors.toMap(
                        dto -> normalizeEmail(dto.getCommit().getCommitter().getEmail()),
                        dto -> dto.getCommit().getCommitter().getName(),
                        (existing, replacement) -> existing
                ));

        // Обрабатываем Jira пользователей, и ищем пересечения по email
        jiraClient.getUsers()
                .blockOptional()
                .orElseGet(Collections::emptyList)
                .forEach(jiraUser -> {
                    String email = normalizeEmail(jiraUser.getEmailAddress());
                    //Добавление проверки на отсутствие записи о пользователе ранее
                    if (email != null ) {
                        if(!userMappingRepo.existsByEmail(email)) {
                            userMappingRepo.save(mapper.toEntity(jiraUser, gitHubUsers.get(email)));
                            System.out.println("Данные пользователя " + email + " были добавлены!");
                        } else {
                          UserMapping userMapping =  userMappingRepo.findByEmail(email);
                          userMapping.setGithubUsername(gitHubUsers.get(email));
                          userMapping.setJiraUsername(jiraUser.getDisplayName());
                          userMappingRepo.save(userMapping);
                          System.out.println("Данные пользователя " + email + " были обновлены!");
                        }
                    }
                });
    }

    private String normalizeEmail(String email) {
        return email != null ?
                email.toLowerCase().replaceAll("[^a-z0-9.@]", "") :
                null;
    }
}
