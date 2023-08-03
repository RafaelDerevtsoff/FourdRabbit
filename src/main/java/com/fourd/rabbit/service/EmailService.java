package com.fourd.rabbit.service;

import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<String> sendEmail(String recipientEmail, String subject, String body);
}
