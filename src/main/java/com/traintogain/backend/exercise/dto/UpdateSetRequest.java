package com.traintogain.backend.exercise.dto;

public record UpdateSetRequest(
        Double weight,
        Integer repetitions,
        Boolean completed
) {}
