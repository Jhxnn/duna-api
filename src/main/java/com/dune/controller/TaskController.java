package com.dune.controller;

import com.dune.dto.TaskDto;
import com.dune.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

