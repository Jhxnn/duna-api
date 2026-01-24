package com.dune.controller;

import com.dune.dto.DuneResponseDto;
import com.dune.service.DuneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dunes")
public class DuneController {

    private final DuneService duneService;

    public DuneController(DuneService duneService) {
        this.duneService = duneService;
    }

    @GetMapping
    public ResponseEntity<DuneResponseDto> getDuneSummary() {
        DuneResponseDto duneSummary = duneService.getDuneSummary();
        return ResponseEntity.ok(duneSummary);
    }
}
