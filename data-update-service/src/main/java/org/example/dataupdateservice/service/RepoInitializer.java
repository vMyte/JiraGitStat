package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.model.entity.Repos;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RepoInitializer {
    private final GitHubConfig gitHubConfig;
    private final RepositoryRepo repositoryRep;

    public void initRepository() {
        if (!repositoryRep.existsByName(gitHubConfig.getRepos())) {
            Repos repo = new Repos();
            repo.setName(gitHubConfig.getRepos());
            repo.setOwner(gitHubConfig.getOwner());
            repo.setUrl(gitHubConfig.getBaseurl() + "/" +
                    gitHubConfig.getOwner() + "/" +
                    gitHubConfig.getRepos());
            repo.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            repositoryRep.save(repo);
            System.out.println("Добавление репозиторий прошло успешно!");
        }
        else{
            System.out.println("Уже есть соответствующая запись в бд!");
        }

    }
}
