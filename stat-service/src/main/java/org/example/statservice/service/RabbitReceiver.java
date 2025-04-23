package org.example.statservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitReceiver {

    @RabbitListener(queues = {"mainQueue"})
    public void  receive(String message){
        //log.info(message + " -> сообщение из RabbitMQ ");
    }
}
