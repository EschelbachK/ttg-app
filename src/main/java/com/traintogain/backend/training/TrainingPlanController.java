package com.traintogain.backend.training;

import com.traintogain.backend.training.dto.UpdateTrainingPlanRequest;
import com.traintogain.backend.training.dto.CreateTrainingPlanRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @PostMapping
    public ResponseEntity<TrainingPlan> createPlan(
            @Valid @RequestBody CreateTrainingPlanRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                trainingPlanService.createPlan(authentication.getName(), request.title())
        );
    }

    @GetMapping
    public List<TrainingPlan> getPlans(Authentication authentication) {
        return trainingPlanService.getPlansForUser(authentication.getName());
    }

    @GetMapping("/archived")
    public List<TrainingPlan> getArchivedPlans(Authentication authentication) {
        return trainingPlanService.getArchivedPlansForUser(authentication.getName());
    }

    @PatchMapping("/{id}")
    public TrainingPlan updatePlan(
            @PathVariable String id,
            @Valid @RequestBody UpdateTrainingPlanRequest request,
            Authentication authentication
    ) {
        return trainingPlanService.updatePlan(id, authentication.getName(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable String id,
            Authentication authentication
    ) {
        trainingPlanService.deletePlan(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}