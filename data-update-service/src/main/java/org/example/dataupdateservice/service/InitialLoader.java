package org.example.dataupdateservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class  InitialLoader {
    private final IssueLoader issueLoader;
    private final UserMappingLoader userMappingInitializer;
    private final CommitLoader commitLoader;
    private final RepoLoader repoInitializer;
   // private final Queue queue;
   // private final RabbitAdmin rabbitAdmin;
    private final MessageSender messageSender;

    @PostConstruct
    public void saveInitialInformation(){
        //rabbitAdmin.declareQueue(queue);

        repoInitializer.initRepository();
        userMappingInitializer.loadUserMapping();
        issueLoader.loadIssues();
        commitLoader.loadCommits();

        System.out.println("Данные обновились...");
       // messageSender.sendMassage();
    }

}
