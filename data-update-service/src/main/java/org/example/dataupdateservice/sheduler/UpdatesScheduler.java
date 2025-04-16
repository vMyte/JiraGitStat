package org.example.dataupdateservice.sheduler;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.service.UserMappingLoader;
import org.example.dataupdateservice.service.sheduled.CommitUpdater;
import org.example.dataupdateservice.service.sheduled.IssueUpdater;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class UpdatesScheduler {
    private final UserMappingLoader userMappingUpdater;
    private final CommitUpdater commitUpdater;
    private final IssueUpdater issueUpdater;

    @Scheduled(cron = "*/30 * * * * *")
    public void loadUpdate(){
        userMappingUpdater.loadUserMapping();
        issueUpdater.updateIssues();
        commitUpdater.updateCommits();
    }
}
