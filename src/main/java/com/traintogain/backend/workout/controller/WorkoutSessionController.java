package com.traintogain.backend.workout.controller;

import com.traintogain.backend.workout.model.SetLog;
import com.traintogain.backend.workout.model.WorkoutSession;
import com.traintogain.backend.workout.service.WorkoutSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutSessionController {

    private final WorkoutSessionService service;

    @PostMapping("/start")
    public WorkoutSession startWorkout(@RequestParam String userId) {
        return service.startWorkout(userId);
    }

    @PostMapping("/finish")
    public WorkoutSession finishWorkout(@RequestBody WorkoutSession session) {
        return service.saveWorkout(session);
    }

    @GetMapping
    public List<WorkoutSession> getWorkouts(@RequestParam String userId) {
        return service.getUserWorkouts(userId);
    }

    @GetMapping("/exercises/{exerciseId}/sets")
    public List<SetLog> getSets(
            @RequestParam String userId,
            @PathVariable String exerciseId
    ) {
        return service.getSets(userId, exerciseId);
    }

    @PostMapping("/exercises/{exerciseId}/sets")
    public List<SetLog> addSet(
            @RequestParam String userId,
            @PathVariable String exerciseId,
            @RequestBody SetLog set
    ) {
        return service.addSet(userId, exerciseId, set);
    }
}