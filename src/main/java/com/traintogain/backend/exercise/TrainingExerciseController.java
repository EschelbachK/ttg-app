package com.traintogain.backend.exercise;

import com.traintogain.backend.training.dto.CreateTrainingExerciseRequest;
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
        String userId = authentication.getName();
        return ResponseEntity.ok(trainingExerciseService.addExercise(userId, request));
    }

    @GetMapping
    public List<TrainingExercise> getExercises(
            @RequestParam String folderId,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        return trainingExerciseService.getExercisesByFolder(userId, folderId);
    }

    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable String id) {
        trainingExerciseService.deleteExercise(id);
    }
}