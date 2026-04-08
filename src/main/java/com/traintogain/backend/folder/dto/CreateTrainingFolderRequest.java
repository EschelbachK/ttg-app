package com.traintogain.backend.folder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateTrainingFolderRequest(

        @NotBlank(message = "Name darf nicht leer sein")
        String name,

        @Min(value = 0, message = "Reihenfolge muss >= 0 sein")
        int order

) {}