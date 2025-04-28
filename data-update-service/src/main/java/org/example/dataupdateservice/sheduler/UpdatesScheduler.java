package org.example.dataupdateservice.sheduler;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.example.dataupdateservice.service.MessageSender;
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
    private final MessageSender messageSender;

    @Scheduled(cron = "0 */2 * * * *")
    public void loadUpdate(){
       userMappingUpdater.loadUserMapping();
        issueUpdater.updateIssues();
        commitUpdater.updateCommits();

        System.out.println("Данные обновились...");
        messageSender.sendMassage();
    }
}
