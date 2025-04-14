package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.client.github.GitHubClient;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.model.dto.CommitDTO;
import org.example.dataupdateservice.model.entity.Commit;
import org.example.dataupdateservice.model.entity.Repos;
import org.example.dataupdateservice.model.mapper.github.GitHubCommitMapper;
import org.example.dataupdateservice.repository.CommitRepo;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.example.dataupdateservice.repository.UserMappingRepo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommitLoader {

    //private final UserMappingRepo userMappingRepo;
    private final GitHubClient gitHubClient;
    private final GitHubCommitMapper commitMapper;
    private final CommitRepo commitRepo;
    private final RepositoryRepo repositoryRepo;
    private final GitHubConfig gitHubConfig;

    public void loadCommits() {
        Repos repo = repositoryRepo.findByOwnerAndName(
                gitHubConfig.getOwner(),
                gitHubConfig.getRepos()
        ).orElseThrow();

        gitHubClient.getCommits()
                .subscribe(commits -> {
                    List<Commit> entities = commits.stream()
                            .map(dto -> commitMapper.toEntity(dto, repo))
                            .collect(Collectors.toList());
                    commitRepo.saveAll(entities);

                    repo.setUpdatedAt(String.valueOf(LocalDateTime.now()));
                    repositoryRepo.save(repo);
                });
    }
}