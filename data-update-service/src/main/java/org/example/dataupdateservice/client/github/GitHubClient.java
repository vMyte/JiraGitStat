package org.example.dataupdateservice.client.github;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.model.dto.CommitDTO;
import org.example.dataupdateservice.model.dto.UserGitHubDTO;
import org.example.dataupdateservice.model.entity.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List ;

@Component
@RequiredArgsConstructor
public class GitHubClient {
  private final WebClient gitHubWebClient;
  private final GitHubConfig gitHubConfig;

    public Mono<List<CommitDTO>> getCommits() {
      return gitHubWebClient.get()
              .uri("/repos/{owner}/{repo}/commits",
                      gitHubConfig.getOwner(),gitHubConfig.getRepos())
              .retrieve()
              .bodyToMono(new ParameterizedTypeReference<List<CommitDTO>>() {});
    }

  public Mono<List<UserGitHubDTO>> getUsers() {
    return gitHubWebClient.get()
            .uri("/repos/{owner}/{repo}/commits",
                    gitHubConfig.getOwner(),
                    gitHubConfig.getRepos())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<UserGitHubDTO>>() {});
  }

  public Mono<List<CommitDTO>> getNewCommits(String date) {
    return gitHubWebClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/repos/{owner}/{repo}/commits")
                    .queryParam("since", date) // Добавляем параметр 'since'
                    .build(gitHubConfig.getOwner(), gitHubConfig.getRepos()))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<CommitDTO>>() {});
  }
}


