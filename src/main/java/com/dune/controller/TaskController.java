package com.dune.controller;

import com.dune.dto.TaskDto;
import com.dune.dto.TaskResponseDto;
import com.dune.dto.TaskUpdateDto;
import com.dune.model.enums.TaskPriority;
import com.dune.model.enums.TaskStatus;
import com.dune.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/tasks")
@RestController
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createTask(@RequestBody TaskDto taskDto) {
        taskService.requestCreate(taskDto);
        return ResponseEntity.ok(Map.of("status", "Task creation requested successfully"));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateTask(@RequestBody TaskUpdateDto taskDto) {
        taskService.requestUpdate(taskDto);
        return ResponseEntity.ok(Map.of("status", "Task update requested successfully"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable("id") UUID taskId, @RequestBody TaskStatus status) {
        TaskResponseDto updatedTask = taskService.updateStatus(taskId, status);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<TaskResponseDto> updateTaskPriority(@PathVariable("id") UUID taskId, @RequestBody TaskPriority priority) {
        TaskResponseDto updatedTask = taskService.updatePriority(taskId, priority);
        return ResponseEntity.ok(updatedTask);
    }

}

