package com.fourd.rabbit.service.impl;

import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.repository.TeacherRepository;
import com.fourd.rabbit.service.TeacherService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TeacherServiceImpl implements TeacherService {
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
        return null;
    }
}
