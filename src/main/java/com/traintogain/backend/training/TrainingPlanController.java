package com.traintogain.backend.training;

import com.traintogain.backend.training.dto.CreateTrainingPlanRequest;
import com.traintogain.backend.training.dto.UpdateTrainingPlanRequest;
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

    // 🔐 userId kommt aus JWT
    @PostMapping
    public TrainingPlan createPlan(
            Authentication authentication,
            @RequestBody CreateTrainingPlanRequest request
    ) {
        String userId = authentication.getName();

        return trainingPlanService.createPlan(
                userId,
                request.name()
        );
    }

    // 🔐 userId kommt aus JWT
    @GetMapping
    public List<TrainingPlan> getPlans(Authentication authentication) {
        String userId = authentication.getName();
        return trainingPlanService.getPlansForUser(userId);
    }

    @PatchMapping("/{id}")
    public TrainingPlan updatePlan(
            @PathVariable String id,
            @RequestBody UpdateTrainingPlanRequest request
    ) {
        return trainingPlanService.updatePlan(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable String id) {
        trainingPlanService.deletePlan(id);
    }
}