package com.traintogain.backend.exercise.dto;

public record SetEntryRequest(
        int order,
        double weight,
        int repetitions
) {}