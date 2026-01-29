package com.traintogain.backend.training;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
    }

    public TrainingPlan createPlan(String userId, String name) {
        TrainingPlan plan = new TrainingPlan(userId, name);
        return trainingPlanRepository.save(plan);
    }

    public List<TrainingPlan> getPlansForUser(String userId) {
        return trainingPlanRepository.findByUserId(userId);
    }
}