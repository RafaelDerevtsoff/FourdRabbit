package com.fourd.rabbit.service;

import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.dto.UpdateLessonRequest;
import com.fourd.rabbit.repository.TeacherRepository;
import com.fourd.rabbit.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TeacherServiceTest {
    @InjectMocks
    private TeacherServiceImpl teacherService;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private ReactiveValueOperations<String, Teacher> ops;
    @Mock
    private ReactiveRedisTemplate<String, Teacher> redisTemplate;
    private String id = UUID.randomUUID().toString();
    private static Lesson lesson = new Lesson("Random Title", "Random description", Date.from(Instant.now()));
    private static Lesson updatedLesson = new Lesson("Random Title", "new description", Date.from(Instant.now()));
    private Lesson newLesson = new Lesson("New Lesson","new lesson description",Date.from(Instant.now()));
    private Logger LOGGER = LoggerFactory.getLogger(TeacherServiceTest.class);

    Teacher teacherSaved = new Teacher("Test", "password", false, "emailmock", List.of(), new HashMap<>());
    Teacher updated = new Teacher("Test", "password", false, "emailmock", List.of(), new HashMap<>());
    Teacher createdTeacherWithLessons = new Teacher("Test", "password", false, "emailmock", List.of(), new HashMap<>());
    CreateLessonsRequest newLessonRequest = new CreateLessonsRequest("Test", new HashMap<>());

    @BeforeEach
    public void setup() {
        teacherSaved.getLessons().put("Random Title", lesson);
        updated.getLessons().put("Random Title", updatedLesson);
        newLessonRequest.getLessons().put("New lesson",newLesson);
        createdTeacherWithLessons.setLessons(newLessonRequest.getLessons());
    }

    @Test
    public void updateLesson() {
        Lesson newLesson = new Lesson("Random Title", "new description", Date.from(Instant.now()));
        HashMap<String, Lesson> newLessons = new HashMap<>();
        newLessons.put(newLesson.getTitle(), lesson);
        when(teacherRepository.findByUsername(anyString())).thenReturn(Mono.just(teacherSaved));
        teacherSaved.setLessons(newLessons);
        when(redisTemplate.opsForValue()).thenReturn(ops);
        when(ops.set(anyString(), any(Teacher.class), any(Duration.class))).thenReturn(Mono.just(true));
        when(ops.get(anyString())).thenReturn(Mono.empty());
        when(teacherRepository.save(teacherSaved)).thenReturn(Mono.just(updated));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());
        UpdateLessonRequest createLessonsRequest = new UpdateLessonRequest("Test", newLessons);
        StepVerifier.create(teacherService.updateLesson(createLessonsRequest))
                .expectNextMatches(t -> {
                    return t.getLessons().get("Random Title").getDescription().equals("new description");
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void createNewLesson() {
        when(teacherRepository.findByUsername(anyString())).thenReturn(Mono.just(teacherSaved));
        when(redisTemplate.opsForValue()).thenReturn(ops);
        when(ops.set(anyString(), any(Teacher.class), any(Duration.class))).thenReturn(Mono.just(true));
        when(ops.get(anyString())).thenReturn(Mono.empty());
        when(teacherRepository.save(teacherSaved)).thenReturn(Mono.just(createdTeacherWithLessons));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        StepVerifier.create(teacherService.createNewLesson(newLessonRequest))
                .expectNextMatches(result -> result.getLessons().get("New lesson").getTitle().equals("New Lesson"))
                .verifyComplete();
    }
    @Test
    public void createNewTeacher(){
        when(teacherRepository.save(teacherSaved)).thenReturn(Mono.just(teacherSaved));
        when(teacherRepository.insert(teacherSaved)).thenReturn(Mono.just(teacherSaved));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        StepVerifier.create(teacherService.createNewTeacher(teacherSaved))
                .expectNext()
                .expectNextMatches(result -> result.getUsername().equals("Test"))
                .verifyComplete();
    }
}
