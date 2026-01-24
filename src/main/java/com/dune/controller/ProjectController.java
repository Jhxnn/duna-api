package com.dune.controller;

import com.dune.dto.ProjectDto;
import com.dune.dto.ProjectResponseDto;
import com.dune.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController{

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        List<ProjectResponseDto> projects = projectService.findAllProjects();
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable("id") UUID projectId) {
        ProjectResponseDto project = projectService.findProjectById(projectId);
        return ResponseEntity.ok(project);
    }
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectResponseDto createdProject = projectService.createProject(projectDto);
        return ResponseEntity.ok(createdProject);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable("id") UUID projectId, @RequestBody ProjectDto projectDto) {
        ProjectResponseDto updatedProject = projectService.updateProject(projectDto, projectId);
        return ResponseEntity.ok(updatedProject);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
