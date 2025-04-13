package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.model.dto.IssueDTO;
import org.example.dataupdateservice.model.dto.IssueDetailsDTO;
import org.example.dataupdateservice.model.entity.IssueEntity;
import org.example.dataupdateservice.repository.IssueRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueLoader {
    private final WebClient jiraWebClient;
    private final IssueRepo issueRepo;

   // @PostConstruct
    public void loadIssues() {
        IssueDetailsDTO response = jiraWebClient.get()
                .uri("/search")
                .retrieve()
                .bodyToMono(IssueDetailsDTO.class)
                .block();

     for (var elem: response.getIssues()){
         IssueDTO response1 = jiraWebClient.get()
                 .uri("/issue/{id}",elem.getId())
                 .retrieve()
                 .bodyToMono(IssueDTO.class)
                 .block();

         IssueEntity issue = new IssueEntity();
         issue.setKey(response1.getKey());
          issue.setSummary(response1.getFields().getSummary());
          issue.setStatus(response1.getFields().getStatus().getName());
          issue.setAssignee(response1.getFields().getAssignee().getName());
          issue.setCreatedAt(response1.getFields().getCreatedAt().toString());
         issue.setUpdatedAt(
                 Optional.ofNullable(response1.getFields().getClosedAt())
                         .map(Object::toString)
                         .orElse(null)
         );
       issueRepo.save(issue);
     }
    }
}
