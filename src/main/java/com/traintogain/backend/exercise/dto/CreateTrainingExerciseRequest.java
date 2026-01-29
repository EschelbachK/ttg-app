package com.traintogain.backend.exercise.dto;

import com.traintogain.backend.common.BodyRegion;

import java.util.List;

public record CreateTrainingExerciseRequest(
        String folderId,
        String name,
        BodyRegion bodyRegion,
        List<SetEntryRequest> sets
) {}