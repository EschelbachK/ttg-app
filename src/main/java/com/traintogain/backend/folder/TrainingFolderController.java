package com.traintogain.backend.folder;

import com.traintogain.backend.common.BodyRegion;
import com.traintogain.backend.folder.dto.CreateTrainingFolderRequest;
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
            @RequestBody CreateTrainingFolderRequest request
    ) {
        return folderService.createFolder(
                request.trainingPlanId(),
                request.name(),
                request.bodyRegion(),
                request.order()
        );
    }

    @GetMapping
    public List<TrainingFolder> getFolders(
            @RequestParam String trainingPlanId
    ) {
        return folderService.getFoldersForPlan(trainingPlanId);
    }
}
