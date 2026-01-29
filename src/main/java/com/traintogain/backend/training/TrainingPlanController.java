package com.traintogain.backend.training;

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
            @RequestParam String userId,
            @RequestParam String name
    ) {
        return trainingPlanService.createPlan(userId, name);
    }

    @GetMapping
    public List<TrainingPlan> getPlans(@RequestParam String userId) {
        return trainingPlanService.getPlansForUser(userId);
    }
}