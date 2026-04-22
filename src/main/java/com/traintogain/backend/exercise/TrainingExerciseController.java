package com.traintogain.backend.exercise;

import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
import com.traintogain.backend.exercise.dto.UpdateTrainingExerciseRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training-plans/{planId}/folders/{folderId}/exercises")
public class TrainingExerciseController {

    private final TrainingExerciseService trainingExerciseService;

    public TrainingExerciseController(TrainingExerciseService trainingExerciseService) {
        this.trainingExerciseService = trainingExerciseService;
    }

    @PostMapping
    public ResponseEntity<TrainingExercise> addExercise(
            @PathVariable String planId,
            @PathVariable String folderId,
            @Valid @RequestBody CreateTrainingExerciseRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                trainingExerciseService.addExercise(
                        authentication.getName(),
                        planId,
                        folderId,
                        request
                )
        );
    }

    @GetMapping
    public Page<TrainingExercise> getExercises(
            @PathVariable String planId,
            @PathVariable String folderId,
            Pageable pageable,
            Authentication authentication
    ) {
        return trainingExerciseService.getExercisesByFolder(
                authentication.getName(),
                planId,
                folderId,
                pageable
        );
    }

    // 🔥 FIX: UPDATE ENDPOINT
    @PutMapping("/{exerciseId}")
    public ResponseEntity<TrainingExercise> updateExercise(
            @PathVariable String planId,
            @PathVariable String folderId,
            @PathVariable String exerciseId,
            @Valid @RequestBody UpdateTrainingExerciseRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                trainingExerciseService.updateExercise(
                        authentication.getName(),
                        planId,
                        folderId,
                        exerciseId,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(
            @PathVariable String id,
            Authentication authentication
    ) {
        trainingExerciseService.deleteExercise(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}