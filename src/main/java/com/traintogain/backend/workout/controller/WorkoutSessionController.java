package com.traintogain.backend.workout.controller;

import com.traintogain.backend.workout.domain.WorkoutSession;
import com.traintogain.backend.workout.dto.AddSetRequest;
import com.traintogain.backend.workout.dto.StartWorkoutRequest;
import com.traintogain.backend.workout.service.WorkoutSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout")
@RequiredArgsConstructor
public class WorkoutSessionController {

    private final WorkoutSessionService service;

    @PostMapping("/start")
    public WorkoutSession start(@RequestBody StartWorkoutRequest request) {
        return service.startWorkout(request.getUserId());
    }

    @PostMapping("/finish")
    public WorkoutSession finish(@RequestBody StartWorkoutRequest request) {
        return service.finishWorkout(request.getUserId());
    }

    @GetMapping
    public List<WorkoutSession> get(@RequestParam String userId) {
        return service.getWorkouts(userId);
    }

    @PostMapping("/set")
    public WorkoutSession addSet(
            @RequestParam String userId,
            @RequestBody AddSetRequest request
    ) {
        return service.addSet(userId, request);
    }
}