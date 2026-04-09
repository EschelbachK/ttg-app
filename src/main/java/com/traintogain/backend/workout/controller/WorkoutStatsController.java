package com.traintogain.backend.workout.controller;

import com.traintogain.backend.workout.dto.WorkoutSummaryResponse;
import com.traintogain.backend.workout.service.WorkoutStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout/stats")
@RequiredArgsConstructor
public class WorkoutStatsController {

    private final WorkoutStatsService service;

    @GetMapping("/{sessionId}")
    public WorkoutSummaryResponse get(@PathVariable String sessionId) {
        return service.getSummary(sessionId);
    }
}