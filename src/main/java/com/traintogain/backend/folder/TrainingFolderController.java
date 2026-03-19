package com.traintogain.backend.folder;

import com.traintogain.backend.folder.dto.CreateTrainingFolderRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-folders")
public class TrainingFolderController {

    private final TrainingFolderService folderService;

    public TrainingFolderController(TrainingFolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public TrainingFolder createFolder(
            @RequestBody CreateTrainingFolderRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return folderService.createFolder(
                userId,
                request.trainingPlanId(),
                request.name(),
                request.bodyRegion(),
                request.order()
        );
    }

    @GetMapping
    public List<TrainingFolder> getFolders(
            @RequestParam String trainingPlanId,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return folderService.getFoldersForPlan(userId, trainingPlanId);
    }
}