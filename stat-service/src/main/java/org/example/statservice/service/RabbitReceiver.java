package org.example.statservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RabbitReceiver {
     private final StatisticService statisticService;

     //@PostConstruct
    @RabbitListener(queues = {"mainQueue"})
    public void handleEvent(String message) {
        String[] words = message.split(" ");
        Long repoId = Long.parseLong(words[0]);

        // Обновляем статистику коммитов
        statisticService.updateCommitStatistics(repoId);

        // Обновляем статистику задач
        statisticService.updateIssueStatistics(repoId);
    }
}
