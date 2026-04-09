package com.traintogain.backend.workout.controller;

import com.traintogain.backend.workout.domain.WorkoutSession;
import com.traintogain.backend.workout.dto.*;
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

    @PostMapping("/start/plan")
    public WorkoutSession startWithPlan(@RequestBody StartWorkoutWithPlanRequest request) {
        return service.startWorkoutWithPlan(request);
    }

    @GetMapping("/active")
    public WorkoutSession active(@RequestParam String userId) {
        return service.getActiveWorkout(userId);
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
    public WorkoutSession addSet(@RequestParam String userId, @RequestBody AddSetRequest request) {
        return service.addSet(userId, request);
    }

    @PutMapping("/set")
    public WorkoutSession updateSet(@RequestParam String userId, @RequestBody UpdateSetRequest request) {
        return service.updateSet(userId, request);
    }

    @DeleteMapping("/set/{setId}")
    public WorkoutSession deleteSet(@RequestParam String userId, @PathVariable String setId) {
        return service.deleteSet(userId, setId);
    }

    @PutMapping("/exercise/reorder")
    public WorkoutSession reorder(@RequestParam String userId, @RequestBody ReorderExerciseRequest request) {
        return service.reorderExercises(userId, request);
    }

    @PostMapping("/rest/start/{setId}")
    public WorkoutSession startRest(@RequestParam String userId, @PathVariable String setId) {
        return service.startRest(userId, setId);
    }

    @PostMapping("/rest/finish/{setId}")
    public WorkoutSession finishRest(@RequestParam String userId, @PathVariable String setId) {
        return service.finishRest(userId, setId);
    }
}