package org.example.dataupdateservice.service;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {
    private final AmqpTemplate amqpTemplate;
    private final RepositoryRepo repositoryRepo;
    private final GitHubConfig gitHubConfig;

    @Value("${queue.name}")
    private String queueName;

    public void sendMassage(){
      amqpTemplate.convertAndSend( queueName,Long.toString(repositoryRepo.findByOwnerAndName(gitHubConfig.getOwner(), gitHubConfig.getRepos()).orElseThrow().getId()));
    }
}
