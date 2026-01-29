package com.traintogain.backend.exercise;

import com.traintogain.backend.common.BodyRegion;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-exercises")
public class TrainingExerciseController {

    private final TrainingExerciseService exerciseService;

    public TrainingExerciseController(
            TrainingExerciseService exerciseService
    ) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public TrainingExercise addExercise(
            @RequestParam String folderId,
            @RequestParam String name,
            @RequestParam BodyRegion bodyRegion,
            @RequestBody List<SetEntry> sets
    ) {
        return exerciseService.addExercise(
                folderId,
                name,
                bodyRegion,
                sets
        );
    }

    @GetMapping
    public List<TrainingExercise> getExercises(
            @RequestParam String folderId
    ) {
        return exerciseService.getExercisesForFolder(folderId);
    }
}