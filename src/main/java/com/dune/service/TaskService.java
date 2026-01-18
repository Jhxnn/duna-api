    package com.dune.service;

    import com.dune.dto.TaskDto;
    import com.dune.dto.TaskUpdateDto;
    import com.dune.exception.BadRequestException;
    import com.dune.messaging.producer.TaskProducer;
    import com.dune.model.Task;
    import com.dune.repository.ProjectRepository;
    import com.dune.repository.TaskRepository;
    import org.springframework.beans.BeanUtils;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;

    @Service
    public class TaskService {

        private final TaskRepository taskRepository;
        private final ProjectRepository projectRepository;
        private final TaskProducer taskProducer;

        public TaskService(TaskRepository taskRepository, TaskProducer taskProducer, ProjectRepository projectRepository) {
            this.taskProducer = taskProducer;
            this.taskRepository = taskRepository;
            this.projectRepository = projectRepository;
        }

        public void processCreate(TaskDto taskDto){
            Task task = new Task();
            BeanUtils.copyProperties(taskDto, task);
            task.setCreatedAt(LocalDateTime.now());
            task.setPointValue(task.getDetails().length());
            task.setSize(0);
            task.setProject(projectRepository.findById(taskDto.projectId()).orElse(null));
            taskRepository.save(task);
        }
        public void requestCreate(TaskDto taskDto){
            if(!projectRepository.existsById(taskDto.projectId())){
                throw new BadRequestException("Project with ID " + taskDto.projectId() + " does not exist.");
            }
            taskProducer.sendToSave(taskDto);
        }
        public void processUpdate(TaskUpdateDto taskDto){
            Task task = new Task();
            BeanUtils.copyProperties(taskDto, task);
            taskRepository.save(task);
        }
        public void requestUpdate(TaskUpdateDto taskDto) {
            taskProducer.sendToUpdate(taskDto);
        }
    }
