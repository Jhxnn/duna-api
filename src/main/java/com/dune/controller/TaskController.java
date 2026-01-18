package com.dune.controller;

import com.dune.dto.TaskDto;
import com.dune.dto.TaskUpdateDto;
import com.dune.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

}

