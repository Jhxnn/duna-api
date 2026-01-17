package com.dune.dto;

import com.dune.model.enums.TaskPriority;
import com.dune.model.enums.TaskStatus;

import java.util.UUID;

public record TaskDto(String title, String details, TaskStatus status, TaskPriority priority, UUID projectId) {
}
