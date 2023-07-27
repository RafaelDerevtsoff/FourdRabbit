package com.fourd.rabbit.repository;


import com.fourd.rabbit.document.Teacher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TeacherRepository extends ReactiveMongoRepository<Teacher, String> {
    Mono<Teacher> findByUsername(String name);
    Mono<Teacher> findByUsernameAndId(String username,String Id);
}

