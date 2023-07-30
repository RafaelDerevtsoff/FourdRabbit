package com.fourd.rabbit.service;


import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import reactor.core.publisher.Mono;

public interface TeacherService  {
    Mono<Teacher> createNewTeacher(Teacher teacher);
    Mono<Teacher> findByUsernameTeacher(String username);
    Mono<Teacher> updateLesson(CreateLessonsRequest updatedLessons);


    public Mono<Teacher> createNewLesson(CreateLessonsRequest lessons);

}
