package com.traintogain.backend.folder;

import com.traintogain.backend.folder.dto.CreateTrainingFolderRequest;
import com.traintogain.backend.training.TrainingPlan;
import com.traintogain.backend.training.TrainingPlanRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-folders")
public class TrainingFolderController {

    private final TrainingFolderService folderService;
    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingFolderController(
            TrainingFolderService folderService,
            TrainingPlanRepository trainingPlanRepository
    ) {
        this.folderService = folderService;
        this.trainingPlanRepository = trainingPlanRepository;
    }

    @PostMapping
    public TrainingFolder createFolder(
            @RequestBody CreateTrainingFolderRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        TrainingPlan plan = trainingPlanRepository
                .findByUserIdAndArchivedFalse(userId)
                .stream()
                .findFirst()
                .orElseThrow();

        return folderService.createFolder(
                userId,
                plan.getId(),
                request.name(),
                null,
                request.order()
        );
    }

    @GetMapping
    public List<TrainingFolder> getFolders(
            Authentication authentication
    ) {
        String userId = authentication.getName();

        TrainingPlan plan = trainingPlanRepository
                .findByUserIdAndArchivedFalse(userId)
                .stream()
                .findFirst()
                .orElseThrow();

        return folderService.getFoldersForPlan(userId, plan.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteFolder(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        folderService.deleteFolder(id, userId);
    }
}