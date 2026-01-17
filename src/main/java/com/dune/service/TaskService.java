    package com.dune.service;

    import com.dune.dto.TaskDto;
    import com.dune.messaging.producer.TaskProducer;
    import com.dune.model.Task;
    import com.dune.repository.TaskRepository;
    import org.springframework.beans.BeanUtils;
    import org.springframework.stereotype.Service;
    import tools.jackson.databind.util.BeanUtil;

    @Service
    public class TaskService {

        private final TaskRepository taskRepository;
        private final TaskProducer taskProducer;

        public TaskService(TaskRepository taskRepository, TaskProducer taskProducer) {
            this.taskProducer = taskProducer;
            this.taskRepository = taskRepository;
        }

        public void processCreate(TaskDto taskDto){
            Task task = new Task();
            BeanUtils.copyProperties(taskDto, task);
            taskRepository.save(task);
        }
        public void requestCreate(TaskDto taskDto){
            taskProducer.sendToSave(taskDto);
        }
        public void processUpdate(TaskDto taskDto){
            Task task = new Task();
            BeanUtils.copyProperties(taskDto, task);
            taskRepository.save(task);
        }
        public void requestUpdate(TaskDto taskDto) {
            taskProducer.sendToUpdate(taskDto);
        }
    }
