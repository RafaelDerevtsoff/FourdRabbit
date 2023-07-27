package com.fourd.rabbit.service;


import com.fourd.rabbit.document.Teacher;
import reactor.core.publisher.Mono;

public interface TeacherService  {
    Mono<Teacher> createNewTeacher(Teacher teacher);
    Mono<Teacher> findByUsernameTeacher(String username);
}
