package com.traintogain.backend.folder;

import com.traintogain.backend.folder.dto.CreateTrainingFolderRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-plans/{planId}/folders")
public class TrainingFolderController {

    private final TrainingFolderService folderService;

    public TrainingFolderController(TrainingFolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public TrainingFolder createFolder(
            @PathVariable String planId,
            @Valid @RequestBody CreateTrainingFolderRequest request,
            Authentication authentication
    ) {
        return folderService.createFolder(
                authentication.getName(),
                planId,
                request.name(),
                null,
                request.order()
        );
    }

    @GetMapping
    public List<TrainingFolder> getFolders(
            @PathVariable String planId,
            Authentication authentication
    ) {
        return folderService.getFoldersForPlan(authentication.getName(), planId);
    }

    @DeleteMapping("/{id}")
    public void deleteFolder(
            @PathVariable String id,
            Authentication authentication
    ) {
        folderService.deleteFolder(id, authentication.getName());
    }
}