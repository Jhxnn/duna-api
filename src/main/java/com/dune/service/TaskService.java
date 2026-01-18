    package com.dune.service;

    import com.dune.dto.TaskDto;
    import com.dune.dto.TaskUpdateDto;
    import com.dune.messaging.producer.TaskProducer;
    import com.dune.model.Task;
    import com.dune.repository.ProjectRepository;
    import com.dune.repository.TaskRepository;
    import org.apache.coyote.BadRequestException;
    import org.springframework.beans.BeanUtils;
    import org.springframework.stereotype.Service;

    import java.util.UUID;


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
            task.setProject(projectRepository.findById(taskDto.projectId()).orElse(null));
            taskRepository.save(task);
        }
        public void requestCreate(TaskDto taskDto){
            if(!projectRepository.existsById(taskDto.projectId())){
                System.out.println("Project with ID " + taskDto.projectId() + " does not exist.");
                return;
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
