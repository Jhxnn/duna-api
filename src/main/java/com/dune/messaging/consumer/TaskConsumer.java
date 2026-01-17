package com.dune.messaging.consumer;

import com.dune.dto.TaskDto;
import com.dune.service.TaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TaskConsumer {

    private final TaskService taskService;

    public TaskConsumer(TaskService taskService) {
        this.taskService = taskService;
    }

    @RabbitListener(queues = "task.save.queue")
    public void receiveSave(TaskDto dto) {
        taskService.processCreate(dto);
    }

    @RabbitListener(queues = "task.update.queue")
    public void receiveUpdate(TaskDto dto) {
        taskService.processUpdate(dto);
    }
}
