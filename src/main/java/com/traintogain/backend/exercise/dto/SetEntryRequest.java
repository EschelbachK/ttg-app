package com.traintogain.backend.exercise.dto;

import jakarta.validation.constraints.Min;

public record SetEntryRequest(

        @Min(value = 0, message = "Reihenfolge muss >= 0 sein")
        int order,

        @Min(value = 0, message = "Gewicht muss >= 0 sein")
        double weight,

        @Min(value = 1, message = "Wiederholungen müssen >= 1 sein")
        int repetitions

) {}