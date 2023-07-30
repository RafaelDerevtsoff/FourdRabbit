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
    private static Lesson lesson = new Lesson("Random Title",
            "Random description",
            Date.from(Instant.now()));
    private Logger LOGGER = LoggerFactory.getLogger(TeacherRepositoryTest.class);

    @BeforeEach
    public void setup() {
        List<Lesson> lessons = new LinkedList<>();
        lessons.add(lesson);
        Teacher teacherSaved = new Teacher(id,
                "Test",
                "password",
                false,
                new LinkedList<String>(),
                lessons);
        repository.save(teacherSaved).subscribe();
    }
//    @AfterEach
//    public void cleaning(){
//        repository.deleteById(id).subscribe();
//    }

    @Test
    public void save() {
        List<Lesson> lessons = new LinkedList<>();
        lessons.add(lesson);
        StepVerifier.create(repository.findByUsername("Test"))
                .expectNextMatches(teacher -> {
                    LOGGER.info("Test Passou");
                    return teacher.getUsername().equals("Test");
                })
                .expectComplete()
                .verify();
    }
    public Mono<Teacher> updateLesson(CreateLessonsRequest updatedLessons) {
        HashMap<String, Lesson> lessons = new LinkedHashMap<>();
        return repository.findByUsername(updatedLessons.getTeacher())
                .flatMap(teacher -> {
                    teacher.getLessons().forEach(lesson -> {
                        lessons.put(lesson.getTitle(), lesson);
                    });
                    updatedLessons.getLessons().forEach(lesson -> {
                        if (lessons.containsKey(lesson.getTitle())) {
                            lessons.replace(lesson.getTitle(), lesson);
                        }
                    });
                    teacher.setLessons(lessons.values().stream().toList());
                    return repository.save(teacher);
                }).doOnSuccess(teacher -> {
                    LOGGER.info("Lesson Updated");
                });
    }
    @Test
    public void update() {
        List<Lesson> lessons = new LinkedList<>();
        lessons.add(new Lesson("Test","new Description",Date.from(Instant.now().plusSeconds(120L))));
        CreateLessonsRequest updated = new CreateLessonsRequest("Test",lessons);
        StepVerifier.create(updateLesson(updated))
                .expectNextMatches(teacher -> {
                    LOGGER.info("Test Passou");
                    return teacher.getUsername().equals("Test");
                })
                .expectComplete()
                .verify();
    }
}
