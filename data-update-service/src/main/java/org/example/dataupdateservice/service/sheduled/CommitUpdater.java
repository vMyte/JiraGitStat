package org.example.dataupdateservice.service.sheduled;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.client.github.GitHubClient;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.model.entity.Commit;
import org.example.dataupdateservice.model.entity.Repos;
import org.example.dataupdateservice.model.mapper.github.GitHubCommitMapper;
import org.example.dataupdateservice.repository.CommitRepo;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.example.dataupdateservice.repository.UserMappingRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommitUpdater {
    private final GitHubClient gitHubClient;
    private final GitHubCommitMapper commitMapper;
    private final CommitRepo commitRepo;
    private final GitHubConfig gitHubConfig;
    private final RepositoryRepo repositoryRepo;
    private final UserMappingRepo userMappingRepo;

    public void updateCommits(){
        Repos repo = repositoryRepo.findByOwnerAndName(
                gitHubConfig.getOwner(),
                gitHubConfig.getRepos()
        ).orElseThrow();

       // System.out.println(gitHubConfig.getLastDateCommits() + " -> last date commits");
        gitHubClient.getNewCommits(gitHubConfig.getLastDateCommits())
                .subscribe(commits -> {
                    List<Commit> entities = commits.stream()
                            .map(dto -> commitMapper.toEntity(dto, repo))
                            .toList();

                    //Каждый раз при возможности обновляем дату последнего коммита
                    if(!entities.isEmpty()){
                        gitHubConfig.setLastDateCommits(entities.get(0).getCreatedAt());
                        repo.setUpdatedAt(String.valueOf(LocalDateTime.now()));
                        repositoryRepo.save(repo);
                    }

                    for(var elem: entities){
                        if(!commitRepo.existsBySha(elem.getSha())){
                            commitRepo.save(elem);
                            System.out.println("Коммит " + elem.getSha() + " был добавлен!");
                        }
                    }

                    System.out.println("Дата последнего коммита: " + gitHubConfig.getLastDateCommits());
                });
    }

}
