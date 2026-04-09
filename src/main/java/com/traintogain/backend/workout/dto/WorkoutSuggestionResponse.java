package com.traintogain.backend.workout.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSuggestionResponse {

    private double suggestedWeight;
    private int suggestedReps;
}