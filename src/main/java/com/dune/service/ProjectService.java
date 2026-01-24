package com.dune.service;


import com.dune.dto.ProjectDto;
import com.dune.dto.ProjectResponseDto;
import com.dune.model.Project;
import com.dune.repository.ProjectRepository;
import com.dune.repository.UserRepository;
import io.netty.channel.unix.UnixChannelUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectService (UserRepository userRepository, ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponseDto createProject(ProjectDto projectDto){
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        projectRepository.save(project);
        return new ProjectResponseDto(project.getName(), project.getDescription());
    }
    public List<ProjectResponseDto> findAllProjects(){
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> new ProjectResponseDto(project.getName(), project.getDescription()))
                .toList();
    }
    public void deleteProject(UUID projectId){
        if(!projectRepository.existsById(projectId)){
            throw new IllegalStateException("Project with ID " + projectId + " does not exist.");
        }
        projectRepository.deleteById(projectId);
    }
    public ProjectResponseDto findProjectById(UUID projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("Project with ID " + projectId + " does not exist."));
        return new ProjectResponseDto(project.getName(), project.getDescription());
    }
    public ProjectResponseDto updateProject(ProjectDto projectDto, UUID projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("Project with ID " + projectId + " does not exist."));
        BeanUtils.copyProperties(projectDto, project, "id");
        projectRepository.save(project);
        return new ProjectResponseDto(project.getName(), project.getDescription());
    }

}
