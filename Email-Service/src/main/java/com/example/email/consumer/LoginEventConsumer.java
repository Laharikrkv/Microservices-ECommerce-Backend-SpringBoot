package com.example.email.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;


import com.example.events.LoginEvent;
import com.example.email.service.EmailSenderService;

@Component
public class LoginEventConsumer {
	
    private final EmailSenderService emailSenderService;
    private static final Logger log= LoggerFactory.getLogger(LoginEventConsumer.class);
    
    public LoginEventConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    
    @KafkaListener( topics = "user-login-topic", groupId ="email-service", containerFactory = "loginEventKafkaListenerContainerFactory")
    public void consumeLoginEvent(LoginEvent event) {
    	log.info("Publishing login event to Kafka: {}", event);
    	
        emailSenderService.sendLoginMail(
            event.getEmail(),
            event.getUsername()
        );
    }
}
