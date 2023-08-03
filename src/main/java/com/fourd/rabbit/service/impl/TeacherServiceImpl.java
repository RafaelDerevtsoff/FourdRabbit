package com.fourd.rabbit.service.impl;

import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.dto.UpdateLessonRequest;
import com.fourd.rabbit.helper.RedisHelper;
import com.fourd.rabbit.repository.TeacherRepository;
import com.fourd.rabbit.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherRepository teacherRepository;
    private final ReactiveRedisTemplate<String, Teacher> redisTemplate;

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
    private final RabbitTemplate rabbitTemplate;
    public TeacherServiceImpl(TeacherRepository teacherRepository, ReactiveRedisTemplate<String, Teacher> redisTemplate, RabbitTemplate rabbitTemplate) {
        this.teacherRepository = teacherRepository;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }



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
        ReactiveValueOperations<String, Teacher> ops = redisTemplate.opsForValue();
        String key = RedisHelper.generateKey(updatedLessons.getTeacher(),"findByUsername");
        return ops.get(key)
                .switchIfEmpty(teacherRepository.findByUsername(updatedLessons.getTeacher()))
                .flatMap(teacher -> updateLessonAndSave(updatedLessons, teacher))
                .doOnSuccess(teacher -> {
                    ops.set(key,teacher, Duration.ofMinutes(15)).subscribe();
                    LOGGER.info("Lesson successfully Updated : " + updatedLessons.getUpdatedLessons());
                })
                .doOnError(e -> {
                    LOGGER.info("Unable to update lessons : " + updatedLessons.getUpdatedLessons());
                    rabbitTemplate.convertAndSend(updateLessonXDeadLetterExchange, updateLessonXDeadLetterRoutingKey, updatedLessons);
                });
    }

    private Mono<Teacher> updateLessonAndSave(UpdateLessonRequest updatedLessons, Teacher teacher) {
        HashMap<String, Lesson> teacherLessons = new HashMap<>(teacher.getLessons());
        updatedLessons.getUpdatedLessons().forEach((title, lesson) -> {
            if(teacherLessons.containsKey(title)){
                teacherLessons.replace(title,lesson);
            }
        });
        teacher.setLessons(teacherLessons);
        return teacherRepository.save(teacher);
    }

    @Override
    public Mono<Teacher> createNewLesson(CreateLessonsRequest lessons) {
        ReactiveValueOperations<String, Teacher> ops = redisTemplate.opsForValue();
        String key = RedisHelper.generateKey(lessons.getTeacher(),"findByUsername");
        return ops.get(key)
                .switchIfEmpty(teacherRepository.findByUsername(lessons.getTeacher()))
                .flatMap(teacher -> {
                    lessons.getLessons().forEach(teacher.getLessons()::putIfAbsent);
                    return teacherRepository.save(teacher);
                })
                .doOnSuccess(teacher -> {
                    ops.set(key,teacher, Duration.ofMinutes(15)).subscribe();
                    LOGGER.info("Lesson successfully Updated : " + lessons.getLessons().values());
                })
                .doOnError(e -> {
                    LOGGER.error("Unable to create lesson : " + lessons.getLessons());
                    rabbitTemplate.convertAndSend(createLessonXDeadLetterExchange, createLessonXDeadLetterRoutingKey, lessons);
                });
    }
}
