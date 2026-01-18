package com.dune.messaging.producer;

import com.dune.dto.TaskDto;
import com.dune.dto.TaskUpdateDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskProducer {

    private final RabbitTemplate rabbitTemplate;

    public TaskProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToSave(TaskDto task) {
        rabbitTemplate.convertAndSend(
                "app.exchange",
                "task.save",
                task
        );
    }

        public void sendToUpdate(TaskUpdateDto task) {
            rabbitTemplate.convertAndSend(
                    "app.exchange",
                    "task.update",
                    task
            );
        }
}

