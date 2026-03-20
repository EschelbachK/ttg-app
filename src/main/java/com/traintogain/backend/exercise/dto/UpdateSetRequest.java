package com.traintogain.backend.exercise.dto;

import jakarta.validation.constraints.Min;

public record UpdateSetRequest(

        @Min(value = 0, message = "Gewicht muss >= 0 sein")
        Double weight,

        @Min(value = 1, message = "Wiederholungen müssen >= 1 sein")
        Integer repetitions,

        Boolean completed

) {}