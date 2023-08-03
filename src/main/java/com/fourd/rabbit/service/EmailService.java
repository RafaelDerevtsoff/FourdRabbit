package com.fourd.rabbit.service;

import com.fourd.rabbit.document.Teacher;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<String> sendEmail(Teacher teacher);
}
