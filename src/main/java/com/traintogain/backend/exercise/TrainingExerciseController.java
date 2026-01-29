package com.traintogain.backend.exercise;

import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
import com.traintogain.backend.exercise.dto.UpdateSetRequest;
import com.traintogain.backend.exercise.dto.UpdateTrainingExerciseRequest;
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
            @RequestBody CreateTrainingExerciseRequest request
    ) {
        return exerciseService.addExercise(
                request.folderId(),
                request.name(),
                request.bodyRegion(),
                request.sets().stream()
                        .map(s -> new SetEntry(
                                s.order(),
                                s.weight(),
                                s.repetitions()
                        ))
                        .toList()
        );
    }

    @GetMapping
    public List<TrainingExercise> getExercises(
            @RequestParam String folderId
    ) {
        return exerciseService.getExercisesForFolder(folderId);
    }

    @GetMapping("/{id}")
    public TrainingExercise getExerciseById(@PathVariable String id) {
        return exerciseService.getExerciseById(id);
    }

    @PutMapping("/{id}")
    public TrainingExercise updateExercise(
            @PathVariable String id,
            @RequestBody UpdateTrainingExerciseRequest request
    ) {
        return exerciseService.updateExercise(id, request);
    }

    @PatchMapping("/{exerciseId}/sets/{order}")
    public SetEntry updateSet(
            @PathVariable String exerciseId,
            @PathVariable int order,
            @RequestBody UpdateSetRequest request
    ) {
        return exerciseService.updateSet(exerciseId, order, request);
    }

}