package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.model.entity.Repos;
import org.example.dataupdateservice.model.mapper.github.GitHubRepoMapper;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RepoLoader {
    private final GitHubConfig gitHubConfig;
    private final RepositoryRepo repositoryRep;
    private final GitHubRepoMapper gitHubRepoMapper;

    public void initRepository() {
        if (!repositoryRep.existsByName(gitHubConfig.getRepos())) {
            Repos repo = gitHubRepoMapper.toEntity();
            repositoryRep.save(repo);
            System.out.println("Добавление репозиторий прошло успешно!");
        }
        else{
            System.out.println("Уже есть соответствующая запись в бд!");
        }

    }
}
