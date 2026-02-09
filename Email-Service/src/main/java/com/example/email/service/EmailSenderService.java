package com.example.email.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.events.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    
    private final JavaMailSender mailSender;

    public void sendLoginMail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Login Successful");
        message.setText(
            "Hi " + username +
            ",\n\nYou logged in successfully.\n\nOnline Grocery Store"
        );
        mailSender.send(message);
    }

    public void sendOrderMail(OrderPlacedEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("Order Placed: " + event.getOrderId());
        message.setText(
            "Your order has been placed.\n" +
            "Order ID: " + event.getOrderId() + "\n" +
            "Amount: ₹" + event.getPrice()
        );
        mailSender.send(message);
    }
}
