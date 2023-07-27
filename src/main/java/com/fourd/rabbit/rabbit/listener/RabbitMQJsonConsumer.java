package com.fourd.rabbit.rabbit.listener;


import com.fourd.rabbit.document.Teacher;
import com.fourd.rabbit.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RabbitMQJsonConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);

    private final TeacherService teacherService;

    public RabbitMQJsonConsumer(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
    public void consumeJsonMessage(Teacher teacher) {
        LOGGER.info("[CONSUMING FROM QUEUE] :: START");
        teacherService.createNewTeacher(teacher).subscribe();
        LOGGER.info("[CONSUMING FROM QUEUE] :: END");
    }
}
