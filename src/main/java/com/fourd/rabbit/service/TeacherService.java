package com.fourd.rabbit.service;


import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.dto.UpdateLessonRequest;
import reactor.core.publisher.Mono;

public interface TeacherService  {
    Mono<Teacher> createNewTeacher(Teacher teacher);
    Mono<Teacher> findByUsernameTeacher(String username);
    Mono<Teacher> updateLesson(UpdateLessonRequest updatedLessons);


    public Mono<Teacher> createNewLesson(CreateLessonsRequest lessons);

}
