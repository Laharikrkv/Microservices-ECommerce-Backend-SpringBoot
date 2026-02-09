package com.example.email.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.events.OrderPlacedEvent;
import com.example.email.service.EmailSenderService;

@Component
public class OrderEventConsumer {

    private final EmailSenderService emailSenderService;

    public OrderEventConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "order-placed-topic", groupId = "email-service", containerFactory = "orderEventKafkaListenerContainerFactory")
    public void consumeOrderEvent(OrderPlacedEvent event) {
        emailSenderService.sendOrderMail(event);
    }
}
