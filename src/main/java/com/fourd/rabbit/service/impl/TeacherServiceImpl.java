package com.fourd.rabbit.service.impl;

import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.dto.UpdateLessonRequest;
import com.fourd.rabbit.repository.TeacherRepository;
import com.fourd.rabbit.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
public class TeacherServiceImpl implements TeacherService {
    Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherRepository teacherRepository;
    @Value("${create.user.x-dead-letter-exchange}")
    private String createUserXDeadLetterExchange;
    @Value("${create.user.x-dead-letter-routing-key}")
    private String createUserXDeadLetterRoutingKey;
    @Value("${create.lesson.x-dead-letter-exchange}")
    private String createLessonXDeadLetterExchange;
    @Value("${create.lesson.x-dead-letter-routing-key}")
    private String createLessonXDeadLetterRoutingKey;
    @Value("${update.lesson.x-dead-letter-exchange}")
    private String updateLessonXDeadLetterExchange;
    @Value("${update.lesson.x-dead-letter-routing-key}")
    private String updateLessonXDeadLetterRoutingKey;

    public TeacherServiceImpl(TeacherRepository teacherRepository, RabbitTemplate rabbitTemplate) {
        this.teacherRepository = teacherRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    private final RabbitTemplate rabbitTemplate;


    @Override
    public Mono<Teacher> createNewTeacher(Teacher teacher) {
        return teacherRepository.insert(teacher)
                .doOnSuccess(t -> {
                    LOGGER.info("Teacher successfully created : " + t.getUsername());
                }).doOnError(e -> {
                    LOGGER.error("Unable to create teacher : " + teacher.getUsername());
                    rabbitTemplate.convertAndSend(createUserXDeadLetterExchange, createUserXDeadLetterRoutingKey, teacher);
                });
    }



    @Override
    public Mono<Teacher> updateLesson(UpdateLessonRequest updatedLessons) {
        return teacherRepository.findByUsername(updatedLessons.getTeacher())
                .flatMap(teacher -> {
                    HashMap<String, Lesson> teacherLessons = new HashMap<>(teacher.getLessons());
                    updatedLessons.getUpdatedLessons().forEach((title, lesson) -> {
                        if(teacherLessons.containsKey(title)){
                            teacherLessons.replace(title,lesson);
                        }
                    });
                    teacher.setLessons(teacherLessons);
                    return teacherRepository.save(teacher);
                }).doOnSuccess(teacher -> {
                    LOGGER.info("Lesson successfully Updated : " + updatedLessons.getUpdatedLessons());
                })
                .doOnError(e -> {
                    LOGGER.info("Unable to update lessons : " + updatedLessons.getUpdatedLessons());
                    rabbitTemplate.convertAndSend(updateLessonXDeadLetterExchange, updateLessonXDeadLetterRoutingKey, updatedLessons);
                });
    }

    @Override
    public Mono<Teacher> createNewLesson(CreateLessonsRequest lessons) {
        return teacherRepository.findByUsername(lessons.getTeacher())
                .flatMap(teacher -> {
                    lessons.getLessons().forEach(teacher.getLessons()::putIfAbsent);
                    return teacherRepository.save(teacher);
                })
                .doOnError(e -> {
                    LOGGER.error("Unable to create lesson : " + lessons.getLessons());
                    rabbitTemplate.convertAndSend(createLessonXDeadLetterExchange, createLessonXDeadLetterRoutingKey, lessons);
                });
    }
}
