package com.dune.job;

import com.dune.model.Task;
import com.dune.model.enums.TaskStatus;
import com.dune.repository.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TaskSizeJob {

    private final TaskRepository taskRepository;

    public TaskSizeJob(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // roda 1x por dia à meia-noite
    @Scheduled(cron = "0 0 0 * * *")
    public void increaseTaskSizeByDay() {

        List<Task> tasks = taskRepository.findByStatus(TaskStatus.IN_PROGRESS);

        LocalDateTime now = LocalDateTime.now();

        for (Task task : tasks) {

            if (task.getCreatedAt() == null) continue;

            long daysOpen = ChronoUnit.DAYS.between(
                    task.getCreatedAt().toLocalDate(),
                    now.toLocalDate()
            );

            int baseIncrease = switch (task.getPriority()) {
                case LOW -> 1;
                case MEDIUM -> 2;
                case HIGH -> 3;
            };

            int newSize = (int) (daysOpen * baseIncrease);

            task.setSize(newSize);
            task.setUpdatedAt(now);
        }

        taskRepository.saveAll(tasks);
    }
}
