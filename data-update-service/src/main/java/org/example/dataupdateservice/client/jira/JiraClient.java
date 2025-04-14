package org.example.dataupdateservice.client.jira;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.model.dto.CommitDTO;
import org.example.dataupdateservice.model.dto.IssueDTO;
import org.example.dataupdateservice.model.dto.IssueDetailsDTO;
import org.example.dataupdateservice.model.dto.UserJiraDTO;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JiraClient {
    private final WebClient jiraWebClient;

    public Mono<List<UserJiraDTO>> getUsers() {
        return jiraWebClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJiraDTO>>() {});
    }

    public Mono<IssueDetailsDTO> getIssueDetails() {
        return jiraWebClient.get()
                .uri("/search")
                .retrieve()
                .bodyToMono(IssueDetailsDTO.class);
    }

    public Mono<IssueDTO> getIssue(Long id) {
        return jiraWebClient.get()
                .uri("/issue/{id}",id)
                .retrieve()
                .bodyToMono(IssueDTO.class);
    }

}
