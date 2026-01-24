package com.dune.service;


import com.dune.dto.DuneResponseDto;
import com.dune.model.Dune;
import com.dune.model.User;
import com.dune.repository.DuneRepository;
import org.springframework.stereotype.Service;

@Service
public class DuneService {

    private final TaskService taskService;

    private final ProjectService projectService;

    private final DuneRepository duneRepository;

    public DuneService(TaskService taskService, ProjectService projectService, DuneRepository duneRepository) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.duneRepository = duneRepository;
    }

    public DuneResponseDto getDuneSummary(){
        Dune dune = duneRepository.findByUser(new User());
        return new DuneResponseDto(dune.getTotalPoints());
    }

    public void addPointsToDune(int points){
        Dune dune = duneRepository.findByUser(new User());
        dune.setTotalPoints(dune.getTotalPoints() + points);
        duneRepository.save(dune);
    }

    public void removePointsToDune(int points){
        Dune dune = duneRepository.findByUser(new User());
        dune.setTotalPoints(dune.getTotalPoints() - points);
        duneRepository.save(dune);
    }
}
