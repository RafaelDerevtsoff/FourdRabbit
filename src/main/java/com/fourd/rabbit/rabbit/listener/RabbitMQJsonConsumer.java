package com.fourd.rabbit.rabbit.listener;


import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.dto.CreateLessonsRequest;
import com.fourd.rabbit.dto.UpdateLessonRequest;
import com.fourd.rabbit.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);

    private final TeacherService teacherService;


    public RabbitMQJsonConsumer(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

//    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
//    public void createTeacher(Teacher teacher) {
//        teacherService.createNewTeacher(teacher)
//                .doOnSubscribe(subscription -> {
//                    LOGGER.info("[CONSUMING FROM QUEUE CREATE TEACHER] :: START");
//                })
//                .doOnSuccess(t -> {
//                    LOGGER.info("[CONSUMING FROM QUEUE  CREATE TEACHER] :: END");
//                })
//                .subscribe();
//    }

//    @RabbitListener(queues = {"${rabbit.lesson.queue.name}"})
//    public void createLesson(CreateLessonsRequest teacher) {
//        teacherService.createNewLesson(teacher)
//                .doOnSubscribe(subscription -> {
//                    LOGGER.info("[CONSUMING FROM QUEUE UPDATE LESSON] :: START");
//                })
//                .doOnSuccess(t -> {
//                    LOGGER.info("[CONSUMING FROM QUEUE UPDATE LESSON] :: END");
//                })
//                .subscribe();
//    }

//    @RabbitListener(queues = {"${rabbit.update.lesson.queue.name}"})
//    public void updateLesson(UpdateLessonRequest updatedLessons) {
//        teacherService.updateLesson(updatedLessons)
//                .doOnSubscribe(subscription -> {
//                    LOGGER.info("[CONSUMING FROM QUEUE UPDATE LESSON] :: START");
//                })
//                .doOnSuccess(teacher -> {
//                    LOGGER.info("[CONSUMING FROM QUEUE UPDATE LESSON] :: END");
//                })
//                .subscribe();
//
//    }


}
