    package com.dune.service;

    import com.dune.dto.TaskDto;
    import com.dune.dto.TaskResponseDto;
    import com.dune.dto.TaskUpdateDto;
    import com.dune.exception.BadRequestException;
    import com.dune.messaging.producer.TaskProducer;
    import com.dune.model.Project;
    import com.dune.model.Task;
    import com.dune.model.enums.TaskPriority;
    import com.dune.model.enums.TaskStatus;
    import com.dune.repository.ProjectRepository;
    import com.dune.repository.TaskRepository;
    import org.springframework.beans.BeanUtils;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Service
    public class TaskService {

        private final TaskRepository taskRepository;
        private final ProjectRepository projectRepository;
        private final TaskProducer taskProducer;
        private final DuneService duneService;

        public TaskService(TaskRepository taskRepository,DuneService duneService, TaskProducer taskProducer, ProjectRepository projectRepository) {
            this.taskProducer = taskProducer;
            this.taskRepository = taskRepository;
            this.duneService = duneService;
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

        public TaskResponseDto updateStatus(UUID taskId, TaskStatus status){
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalStateException("Task with ID " + taskId + " does not exist."));
            if(status.equals(TaskStatus.DONE)){
                duneService.addPointsToDune(task.getPointValue());
            }
            if(task.getStatus() == TaskStatus.DONE && !status.equals(TaskStatus.DONE)){
                duneService.removePointsToDune(task.getPointValue());
            }
            task.setStatus(status);
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
            return new TaskResponseDto(task.getTaskId(), task.getTitle(), task.getDetails(),task.getStatus(), task.getPriority(), task.getProject().getName());
        }

        public TaskResponseDto updatePriority(UUID taskId, TaskPriority priority){
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalStateException("Task with ID " + taskId + " does not exist."));
            task.setPriority(priority);
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
            return new TaskResponseDto(task.getTaskId(), task.getTitle(), task.getDetails(),task.getStatus(), task.getPriority(), task.getProject().getName());
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
