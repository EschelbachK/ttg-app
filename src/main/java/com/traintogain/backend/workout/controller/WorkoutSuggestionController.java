package com.traintogain.backend.workout.controller;

import com.traintogain.backend.workout.dto.WorkoutSuggestionResponse;
import com.traintogain.backend.workout.service.WorkoutSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout/suggestion")
@RequiredArgsConstructor
public class WorkoutSuggestionController {

    private final WorkoutSuggestionService service;

    @GetMapping
    public WorkoutSuggestionResponse get(
            @RequestParam String userId,
            @RequestParam String exerciseId
    ) {
        return service.getSuggestion(userId, exerciseId);
    }
}