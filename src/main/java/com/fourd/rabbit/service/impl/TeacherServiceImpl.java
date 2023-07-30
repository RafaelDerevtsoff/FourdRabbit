package com.fourd.rabbit.service.impl;

import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
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
    Logger LOGGER   = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Mono<Teacher> createNewTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Mono<Teacher> findByUsernameTeacher(String username) {
        return teacherRepository.findByUsername(username);
    }

    @Override
    public Mono<Teacher> updateLesson(CreateLessonsRequest updatedLessons) {
        HashMap<String, Lesson> teacherLessons = new LinkedHashMap<>();
       return teacherRepository.findByUsername(updatedLessons.getTeacher())
                .flatMap(teacher -> {
                    teacher.getLessons().forEach(lesson -> {
                        teacherLessons.put(lesson.getTitle(), lesson);
                    });
                    updatedLessons.getLessons().forEach(lesson -> {
                        if (teacherLessons.containsKey(lesson.getTitle())) {
                            teacherLessons.replace(lesson.getTitle(), lesson);
                        }
                    });
                    teacher.setLessons(teacherLessons.values().stream().toList());
                    return teacherRepository.save(teacher);
                }).doOnSuccess(teacher -> {
                    LOGGER.info("Lesson Updated");
                });
    }

    @Override
    public Mono<Teacher> createNewLesson(CreateLessonsRequest lessons) {
        return teacherRepository.findByUsername(lessons.getTeacher()).flatMap( teacher ->{
            List<Lesson> newLessons =  teacher.getLessons();
            newLessons.addAll(lessons.getLessons());
            teacher.setLessons(newLessons);
            return teacherRepository.save(teacher);
        });
    }
}
