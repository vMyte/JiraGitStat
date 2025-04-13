package org.example.dataupdateservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitialLoader {
    private final IssueLoader issueLoader;
    private final UserMappingInitializer userMappingInitializer;
    private final CommitLoader commitLoader;
    private final RepoInitializer repoInitializer;


    @PostConstruct
    public void saveInitialInformation(){
        repoInitializer.initRepository();
        userMappingInitializer.loadUserMapping();
        issueLoader.loadIssues();
        commitLoader.loadCommits();
    }

}
