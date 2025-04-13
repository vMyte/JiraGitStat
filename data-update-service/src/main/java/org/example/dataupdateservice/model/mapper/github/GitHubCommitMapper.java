package org.example.dataupdateservice.model.mapper.github;

import org.example.dataupdateservice.model.dto.CommitDTO;
import org.example.dataupdateservice.model.entity.Commit;
import org.example.dataupdateservice.model.entity.Repos;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class GitHubCommitMapper {

    public Commit toEntity(CommitDTO commitDTO, Repos repos) {
        Commit commit = new Commit();
        commit.setRepositoryId(repos.getId());
        commit.setAuthor(commitDTO.getCommit().getCommitter().getName());
        commit.setMassage(commitDTO.getCommit().getMessage());
        commit.setSha(commitDTO.getSha());
        commit.setCreatedAt(String.valueOf(LocalDateTime.parse(
                String.valueOf(commitDTO.getCommit().getCommitter().getDate()),
                DateTimeFormatter.ISO_DATE_TIME
        )));
        return commit;
    }

}
