package com.fourd.rabbit.service.impl;

import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.dto.UpdateLessonRequest;
import com.fourd.rabbit.repository.TeacherRepository;
import com.fourd.rabbit.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Mono<Teacher> createNewTeacher(Teacher teacher) {
        return teacherRepository.insert(teacher);
    }

    @Override
    public Mono<Teacher> findByUsernameTeacher(String username) {
        return teacherRepository.findByUsername(username);
    }

    @Override
    public Mono<Teacher> updateLesson(UpdateLessonRequest updatedLessons) {

        return teacherRepository.findByUsername(updatedLessons.getTeacher())
                .flatMap(teacher -> {
                    HashMap<String, Lesson> teacherLessons = new HashMap<>(teacher.getLessons());
                    updatedLessons.getUpdatedLessons().forEach((title, lesson) -> {
                        if (teacherLessons.containsKey(title)) {
                            teacherLessons.replace(lesson.getTitle(), lesson);
                        }
                    });
                    teacher.setLessons(teacherLessons);
                    return teacherRepository.save(teacher);
                }).doOnSuccess(teacher -> {
                    LOGGER.info("Lesson Updated");
                })
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Mono<Teacher> createNewLesson(CreateLessonsRequest lessons) {
        return teacherRepository.findByUsername(lessons.getTeacher()).flatMap(teacher -> {
            lessons.getLessons().forEach(teacher.getLessons()::putIfAbsent);
            return teacherRepository.save(teacher);
        });
    }
}
