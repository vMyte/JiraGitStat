package org.example.dataupdateservice.model.mapper.github;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.model.entity.Repos;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GitHubRepoMapper {
    private final GitHubConfig gitHubConfig;

    public Repos toEntity(){
        Repos repo = new Repos();
        repo.setName(gitHubConfig.getRepos());
        repo.setOwner(gitHubConfig.getOwner());
        String url = gitHubConfig.getBaseurl();
        url = url.replace("api.","");
        repo.setUrl(url + "/" +
                gitHubConfig.getOwner() + "/" +
                gitHubConfig.getRepos());
        repo.setUpdatedAt(String.valueOf(LocalDateTime.now()));
        return repo;
    }
}
