package com.traintogain.backend.training.dto;

import jakarta.validation.constraints.Min;

public record CreateSetRequest(

        @Min(value = 0, message = "Weight must be >= 0")
        double weight,

        @Min(value = 1, message = "Reps must be >= 1")
        int reps

) {}