package org.example.dataupdateservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.client.jira.JiraClient;
import org.example.dataupdateservice.config.JiraConfig;
import org.example.dataupdateservice.model.dto.IssueDTO;
import org.example.dataupdateservice.model.dto.IssueDetailsDTO;
import org.example.dataupdateservice.model.entity.IssueEntity;
import org.example.dataupdateservice.model.mapper.jira.JiraIssueMapper;
import org.example.dataupdateservice.repository.IssueRepo;
import org.example.dataupdateservice.repository.UserMappingRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueLoader {
    private final JiraConfig jiraConfig;
    private final JiraClient jiraClient;
    private final IssueRepo issueRepo;
    private final JiraIssueMapper issueMapper;
    public void loadIssues() {
        IssueDetailsDTO details = jiraClient.getIssueDetails()
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Failed to load issues"));

        List<IssueDetailsDTO.Issue> list = details.getIssues();

        //Меняем дату последнего созданного issues
        if(!list.isEmpty()){
            IssueDTO firstIssue = jiraClient.getIssue(list.get(0).getId())
                    .blockOptional()
                    .orElse(null);

            if(firstIssue != null){
                String inputDate = firstIssue.getFields().getCreatedAt().toString();
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(inputDate);
                // Форматируем в нужный формат
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = zonedDateTime.format(formatter);
                jiraConfig.setLastDateIssue(formattedDate);
            }
            System.out.println(jiraConfig.getLastDateIssue() + " ->  дата последней задачи в jira");
        }

        list.forEach(issueSummary -> {
            IssueDTO issueDto = jiraClient.getIssue(issueSummary.getId())
                    .blockOptional()
                    .orElse(null);

            if (issueDto != null) {
                IssueEntity issueEntity = issueMapper.toEntity(issueDto);
                if(!issueRepo.existsByKey(issueDto.getKey())) {
                    issueRepo.save(issueEntity);
                    System.out.println("Задача " + issueEntity.getKey() + " добавлена!");
                }
                else{
                    IssueEntity issue =  issueRepo.findByKey(issueDto.getKey());
                    issue.setStatus(issueEntity.getStatus());
                    issue.setUpdatedAt(issueEntity.getUpdatedAt());
                    issueRepo.save(issue);
                    System.out.println("Задача " + issue.getKey() + " обновлена!");
                }
            }
        });
    }
}
