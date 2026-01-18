    package com.dune.service;

    import com.dune.dto.TaskDto;
    import com.dune.dto.TaskUpdateDto;
    import com.dune.exception.BadRequestException;
    import com.dune.messaging.producer.TaskProducer;
    import com.dune.model.Project;
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
            Project project = projectRepository.findById(taskDto.projectId()).orElse(null);
            if(project == null){
                throw new IllegalStateException("Project with ID " + taskDto.projectId() + " does not exist.");
            }
            task.setCreatedAt(LocalDateTime.now());
            task.setPointValue(task.getDetails().length()  + project.getDescription().length() / 10);
            task.setSize(0);
            task.setProject(project);
            taskRepository.save(task);
        }
        public void requestCreate(TaskDto taskDto){
            if(!projectRepository.existsById(taskDto.projectId())){
                throw new BadRequestException("Project with ID " + taskDto.projectId() + " does not exist.");
            }
            taskProducer.sendToSave(taskDto);
        }
        public void processUpdate(TaskUpdateDto dto) {
            Task task = taskRepository.findById(dto.taskId())
                    .orElseThrow(() ->
                            new IllegalStateException("Task does not exist in processing"));

            BeanUtils.copyProperties(dto, task, "id", "createdAt", "project");

            if (dto.projectId() != null) {
                Project project = projectRepository.findById(dto.projectId())
                        .orElseThrow(() ->
                                new IllegalStateException("Project does not exist in processing"));
                task.setProject(project);
            }

            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
        }

        public void requestUpdate(TaskUpdateDto dto) {
            if (dto.taskId() == null) {
                throw new BadRequestException("Task id is required for update");
            }

            if (!taskRepository.existsById(dto.taskId())) {
                throw new BadRequestException("Task does not exist");
            }

            if (dto.projectId() != null &&
                    !projectRepository.existsById(dto.projectId())) {
                throw new BadRequestException("Project does not exist");
            }

            taskProducer.sendToUpdate(dto);
        }

    }
