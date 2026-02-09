package com.example.email;




import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.events.LoginEvent;
import com.example.events.OrderPlacedEvent;

import java.util.HashMap;
import java.util.Map;




@Configuration
public class KafkaConsumerConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    
    @Bean
    public ConsumerFactory<String, LoginEvent> loginEventConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        
        JsonDeserializer<LoginEvent> jsonDeserializer = new JsonDeserializer<>(LoginEvent.class);
        jsonDeserializer.addTrustedPackages("com.example.events");
        jsonDeserializer.setUseTypeHeaders(false);
        
        return new DefaultKafkaConsumerFactory<>(
            config,
            new StringDeserializer(),
            jsonDeserializer
        );
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LoginEvent> loginEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LoginEvent> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(loginEventConsumerFactory());
        return factory;
    }
    
    @Bean
    public ConsumerFactory<String, OrderPlacedEvent> orderEventConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        
        JsonDeserializer<OrderPlacedEvent> jsonDeserializer = new JsonDeserializer<>(OrderPlacedEvent.class);
        jsonDeserializer.addTrustedPackages("com.example.events");
        jsonDeserializer.setUseTypeHeaders(false);
        
        return new DefaultKafkaConsumerFactory<>(
            config,
            new StringDeserializer(),
            jsonDeserializer
        );
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderPlacedEvent> orderEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderPlacedEvent> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderEventConsumerFactory());
        return factory;
    }
}