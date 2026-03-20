package com.traintogain.backend.exercise;

import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<TrainingExercise> addExercise(
            @Valid @RequestBody CreateTrainingExerciseRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                trainingExerciseService.addExercise(authentication.getName(), request)
        );
    }

    @GetMapping
    public List<TrainingExercise> getExercises(
            @RequestParam String folderId,
            Authentication authentication
    ) {
        return trainingExerciseService.getExercisesByFolder(
                authentication.getName(),
                folderId
        );
    }

    @DeleteMapping("/{id}")
    public void deleteExercise(
            @PathVariable String id,
            Authentication authentication
    ) {
        trainingExerciseService.deleteExercise(id, authentication.getName());
    }
}