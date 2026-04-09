package com.traintogain.backend.workout.controller;

import com.traintogain.backend.workout.dto.WorkoutHistoryResponse;
import com.traintogain.backend.workout.service.WorkoutHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout/history")
@RequiredArgsConstructor
public class WorkoutHistoryController {

    private final WorkoutHistoryService service;

    @GetMapping
    public WorkoutHistoryResponse get(
            @RequestParam String userId,
            @RequestParam String exerciseId
    ) {
        return service.getHistory(userId, exerciseId);
    }
}