package com.fourd.rabbit.repository;

import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@EnableReactiveMongoRepositories
public class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository repository;
    private String id = UUID.randomUUID().toString();
    private static Lesson lesson = new Lesson("Random Title", "Random description", Date.from(Instant.now()));
    private Logger LOGGER = LoggerFactory.getLogger(TeacherRepositoryTest.class);

    Teacher teacherSaved = new Teacher("Test", "password", false, "emailmock", List.of(), new HashMap<>());


    @BeforeEach
    public void setup() {
        repository.save(teacherSaved)
                .doOnSuccess(t -> {
                    id = t.getId();
                        }
                ).subscribe();
    }

    @AfterEach
    public void cleaning() {
        repository.delete(teacherSaved).subscribe();
    }

    @Test
    public void findByUsernameTest() {
        StepVerifier.create(repository.findByUsername("Test"))
                .expectNextMatches(teacher -> {
                    LOGGER.info("Test Passou");
                    return teacher.getUsername().equals("Test");
                })
                .expectComplete()
                .verify();
    }


}
