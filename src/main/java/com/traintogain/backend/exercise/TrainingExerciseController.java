package com.traintogain.backend.exercise;

import com.traintogain.backend.training.dto.CreateTrainingExerciseRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-exercises")
public class TrainingExerciseController {

    private final TrainingExerciseService trainingExerciseService;

    public TrainingExerciseController(TrainingExerciseService trainingExerciseService) {
        this.trainingExerciseService = trainingExerciseService;
    }

    @PostMapping
    public TrainingExercise addExercise(
            @Valid @RequestBody CreateTrainingExerciseRequest request
    ) {
        return trainingExerciseService.addExercise(request);
    }

    @GetMapping
    public List<TrainingExercise> getExercises(
            @RequestParam String folderId
    ) {
        return trainingExerciseService.getExercisesByFolder(folderId);
    }

    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable String id) {
        trainingExerciseService.deleteExercise(id);
    }
}