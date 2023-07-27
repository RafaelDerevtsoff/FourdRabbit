package com.fourd.rabbit.service;


import com.fourd.rabbit.document.Lesson;
import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TeacherService  {
    Mono<Teacher> createNewTeacher(Teacher teacher);
    Mono<Teacher> findByUsernameTeacher(String username);
    void updateLesson(CreateLessonsRequest updatedLessons);


    public Mono<Teacher> createNewLesson(CreateLessonsRequest lessons);

}
