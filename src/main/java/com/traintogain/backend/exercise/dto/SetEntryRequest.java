package com.traintogain.backend.exercise.dto;

import jakarta.validation.constraints.Min;

public record SetEntryRequest(

        @Min(0)
        double weight,

        @Min(1)
        int repetitions
) {}