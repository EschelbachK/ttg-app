package com.traintogain.backend.training;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.training.dto.UpdateTrainingPlanRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
    }

    public TrainingPlan createPlan(String userId, String title) {
        TrainingPlan plan = new TrainingPlan(userId, title);
        return trainingPlanRepository.save(plan);
    }

    public List<TrainingPlan> getPlansForUser(String userId) {
        return trainingPlanRepository.findByUserIdAndArchivedFalse(userId);
    }

    public List<TrainingPlan> getArchivedPlansForUser(String userId) {
        return trainingPlanRepository.findByUserIdAndArchivedTrue(userId);
    }

    public TrainingPlan updatePlan(String id, String userId, UpdateTrainingPlanRequest request) {
        TrainingPlan plan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        if (!plan.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Trainingsplan");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            plan.setTitle(request.getTitle());
        }

        if (request.getArchived() != null) {
            plan.setArchived(request.getArchived());
        }

        return trainingPlanRepository.save(plan);
    }

    public void deletePlan(String id, String userId) {
        TrainingPlan plan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        if (!plan.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Trainingsplan");
        }

        trainingPlanRepository.delete(plan);
    }
}