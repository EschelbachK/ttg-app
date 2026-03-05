package com.traintogain.backend.catalog.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTrainingExerciseRequest {

    @NotBlank
    private String folderId;

    @NotBlank
    private String name;

    @NotEmpty
    @Valid
    private List<CreateSetRequest> sets;

}