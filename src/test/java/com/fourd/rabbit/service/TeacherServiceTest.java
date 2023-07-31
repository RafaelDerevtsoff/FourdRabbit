package com.fourd.rabbit.service;

import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static reactor.core.publisher.Mono.when;

//@SpringBootTest
//public class TeacherServiceTest {
//    @Autowired
//    private TeacherRepository repository;
//    @Autowired
//    private TeacherService teacherService;
//
//    private String id = UUID.randomUUID().toString();
//    private static Lesson lesson = new Lesson("Random Title",
//            "Random description",
//            Date.from(Instant.now()));
//    private Logger LOGGER = LoggerFactory.getLogger(TeacherServiceTest.class);
//    @BeforeEach
//    public void setup() {
//        List<Lesson> lessons = new LinkedList<>();
//        lessons.add(lesson);
//        Teacher teacherSaved = new Teacher(id,
//                "Test",
//                "password",
//                false,
//                new LinkedList<String>(),
//                lessons);
//        repository.save(teacherSaved).subscribe();
//    }

//    @Test
//    public void updateLesson() {
//        List<Lesson> newLessons =  new LinkedList<Lesson>();
//        Lesson newLesson = new Lesson("Random Title","new description",Date.from(Instant.now()));
//        newLessons.add(newLesson);
//        CreateLessonsRequest createLessonsRequest = new CreateLessonsRequest("Test",newLessons);
//        StepVerifier
//                .create(teacherService.updateLesson(createLessonsRequest))
//                .expectNextMatches(t -> {
//                    LOGGER.info("This is the new Description \"" + t.getLessons().stream().findFirst().get().getDescription() + "\"");
//                    return t.getLessons().stream().findFirst().get().getDescription().equals("new description");
//                })
//                .expectComplete()
//                .verify();
//    }
//}
