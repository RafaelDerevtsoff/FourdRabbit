//package com.fourd.rabbit.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//    @Value("${rabbitmq.json.queue.name}")
//    private String jsonQueue;
//    @Value("${rabbitmq.exchange.name}")
//    private String exchange;
//    @Value("${rabbitmq.json.routing.key}")
//    private String jsonRoutingKey;
//    @Value("${rabbitmq.lesson.routing.key}")
//    private String lessonsRoutingKey;
//    @Value("${rabbitmq.lesson.update.routing.key}")
//    private String lessonsUpdateRoutingKey;
//    @Value("spring.rabbitmq.host")
//    private String host;
//    @Value("${rabbit.lesson.queue.name}")
//    private String lessonQueue;
//    @Value("${rabbit.update.lesson.queue.name}")
//    private String lessonUpdateQueue;
//
//
//
//
//
//    @Bean
//    public Declarables topicBindings() {
//        Queue topicQueue1 = new Queue(jsonQueue);
//        Queue topicQueue2 = new Queue(lessonQueue);
//        Queue topicQueue3 = new Queue(lessonUpdateQueue);
//
//        TopicExchange topicExchange = new TopicExchange(exchange);
//
//        return new Declarables(
//                topicQueue1,
//                topicQueue2,
//                topicQueue3,
//                topicExchange,
//                BindingBuilder
//                        .bind(topicQueue1)
//                        .to(topicExchange)
//                        .with(jsonRoutingKey),
//                BindingBuilder
//                        .bind(topicQueue2)
//                        .to(topicExchange)
//                        .with(lessonsRoutingKey),
//                BindingBuilder
//                        .bind(topicQueue3)
//                        .to(topicExchange)
//                        .with(lessonsUpdateRoutingKey)
//        );
//    }
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter());
//        return rabbitTemplate;
//    }
//
//
//}
