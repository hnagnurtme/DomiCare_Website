package com.backend.domicare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.response.OverviewResponse;
import com.backend.domicare.service.OverviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OverviewController {
    private final OverviewService overviewService;

    @GetMapping("/overview")
    public ResponseEntity<OverviewResponse> getOverview() {
        return ResponseEntity.status(HttpStatus.OK).body(overviewService.getOverviews());
    }
}
