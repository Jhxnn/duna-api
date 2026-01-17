package com.dune.dto;

import com.dune.model.enums.TaskPriority;
import com.dune.model.enums.TaskStatus;

public record TaskResponseDto(String title, String description, TaskStatus status, TaskPriority priority, String projectName) {
}
