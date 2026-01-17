package com.dune.service;


import com.dune.repository.ProjectRepository;
import com.dune.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectService (UserRepository userRepository, ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }



}
