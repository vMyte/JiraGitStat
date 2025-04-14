package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.client.jira.JiraClient;
import org.example.dataupdateservice.model.dto.IssueDTO;
import org.example.dataupdateservice.model.dto.IssueDetailsDTO;
import org.example.dataupdateservice.model.entity.IssueEntity;
import org.example.dataupdateservice.model.mapper.jira.JiraIssueMapper;
import org.example.dataupdateservice.repository.IssueRepo;
import org.example.dataupdateservice.repository.UserMappingRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueLoader {
    private final JiraClient jiraClient;
    private final IssueRepo issueRepo;
    private final JiraIssueMapper issueMapper;
    private final UserMappingRepo userMappingRepo;

    public void loadIssues() {
        IssueDetailsDTO details = jiraClient.getIssueDetails()
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Failed to load issues"));

        details.getIssues().forEach(issueSummary -> {
            IssueDTO issueDto = jiraClient.getIssue(issueSummary.getId())
                    .blockOptional()
                    .orElse(null);

            //обработка ситуации с отсутствием в user_mapping пользователя(нет commit в github или назначенных на него проблем jira)
            if (issueDto != null && userMappingRepo.existsByJiraUsername(issueDto.getFields().getAssignee().getName())) {
                issueRepo.save(issueMapper.toEntity(issueDto));
            }
        });
    }
}
