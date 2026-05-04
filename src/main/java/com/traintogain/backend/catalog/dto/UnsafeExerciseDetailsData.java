package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.Execution;
import com.traintogain.backend.catalog.model.Progression;
import com.traintogain.backend.catalog.model.Safety;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UnsafeExerciseDetailsData {

    private Execution execution;

    private Progression progression;

    private Safety safety;

    @Builder.Default
    private List<String> instructions = List.of();

    @Builder.Default
    private List<String> tips = List.of();

    @Builder.Default
    private List<String> commonMistakes = List.of();
}