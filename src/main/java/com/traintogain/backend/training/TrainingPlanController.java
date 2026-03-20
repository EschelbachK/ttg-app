package com.traintogain.backend.training;

import com.traintogain.backend.training.dto.UpdateTrainingPlanRequest;
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
    public TrainingPlan createPlan(
            @RequestParam String title,
            Authentication authentication
    ) {
        return trainingPlanService.createPlan(authentication.getName(), title);
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
            @RequestBody UpdateTrainingPlanRequest request
    ) {
        return trainingPlanService.updatePlan(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(
            @PathVariable String id,
            Authentication authentication
    ) {
        trainingPlanService.deletePlan(id, authentication.getName());
    }
}