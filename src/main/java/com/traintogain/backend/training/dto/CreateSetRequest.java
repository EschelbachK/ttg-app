package com.traintogain.backend.training.dto;

import jakarta.validation.constraints.Min;

public record CreateSetRequest(

        @Min(value = 0, message = "Gewicht muss >= 0 sein")
        double weight,

        @Min(value = 1, message = "Wiederholungen müssen >= 1 sein")
        int reps

) {}