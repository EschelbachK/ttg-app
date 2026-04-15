package com.traintogain.backend.exercise.dto;

public record SetEntryRequest(
        double weight,
        int repetitions
) {}