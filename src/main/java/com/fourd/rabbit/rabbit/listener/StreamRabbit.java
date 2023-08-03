package com.fourd.rabbit.rabbit.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.dto.UpdateLessonRequest;
import com.fourd.rabbit.service.EmailService;
import com.fourd.rabbit.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class StreamRabbit {
    private final Logger log = LoggerFactory.getLogger(StreamRabbit.class);
    private final TeacherService teacherService;
    private final EmailService emailService;
    @Value("${ratelimit}")
    private int rateLimit;

    public StreamRabbit(TeacherService teacherService, EmailService emailService) {
        this.teacherService = teacherService;
        this.emailService = emailService;
    }

    @Bean
    public Consumer<Message<String>> createNewTeacher() {
        ObjectMapper objectMapper = new ObjectMapper();
        final String subject = "User Created";
        final String body = "user created ";
        return teacherMessage -> Flux.just(teacherMessage)
                .limitRate(rateLimit)
                .doOnNext(
                        message -> {
                            try {
                                final String payload = teacherMessage.getPayload();
                                Teacher teacherFromPayload = objectMapper.readValue(payload, Teacher.class);
                                teacherService.createNewTeacher(teacherFromPayload)
                                        .doOnSuccess(teacher -> {
                                            emailService
                                                    .sendEmail(teacher.getEmail(), subject, body + teacher.getUsername())
                                                    .subscribe();
                                        }).subscribe();
                            } catch (Exception e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                ).subscribe();
    }

    @Bean
    public Consumer<Message<String>> createLesson() {
        ObjectMapper objectMapper = new ObjectMapper();
        return teacherMessage -> Flux.just(teacherMessage)
                .limitRate(rateLimit)
                .doOnNext(message -> {
                    log.info("[CONSUMING FROM QUEUE CREATE LESSON] :: START");
                    final String payload = teacherMessage.getPayload();
                    try {
                        CreateLessonsRequest newLesson = objectMapper.readValue(payload, CreateLessonsRequest.class);
                        teacherService.createNewLesson(newLesson).subscribe();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnComplete(() -> {
                    log.info("[CONSUMING FROM QUEUE CREATE LESSON] :: END");
                })
                .subscribe();
    }

    @Bean
    public Consumer<Message<String>> updateLesson() {
        ObjectMapper objectMapper = new ObjectMapper();
        return updateLessonRequestFlux -> Flux.just(updateLessonRequestFlux)
                .limitRate(rateLimit)
                .doOnNext(updateLessonRequest -> {
                    log.info("[CONSUMING FROM QUEUE UPDATE LESSON] :: START");
                    final String payload = updateLessonRequest.getPayload();
                    try {
                        UpdateLessonRequest newLesson = objectMapper.readValue(payload, UpdateLessonRequest.class);
                        teacherService.updateLesson(newLesson).subscribe();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnComplete(() -> log.info("[CONSUMING FROM QUEUE UPDATE LESSON] :: END")).subscribe();
    }
}
