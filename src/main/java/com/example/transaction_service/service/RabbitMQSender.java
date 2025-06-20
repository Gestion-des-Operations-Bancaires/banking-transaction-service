package com.example.transaction_service.service;

import com.example.transaction_service.rabbit_mq_model.TransactionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Queue queue;
    private static final Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void send(TransactionEvent transactionEvent) {
        rabbitTemplate.convertAndSend(queue.getName(), transactionEvent);
        logger.info("Sending Message to the Queue : " + transactionEvent);
    }
}