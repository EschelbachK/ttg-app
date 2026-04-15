package com.traintogain.backend.exercise.dto;

import jakarta.validation.constraints.Min;

public record UpdateSetRequest(
        @Min(0) Double weight,
        @Min(0) Integer repetitions,
        Boolean completed
) {}