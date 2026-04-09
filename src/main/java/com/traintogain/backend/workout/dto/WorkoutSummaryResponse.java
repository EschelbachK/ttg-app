package com.traintogain.backend.workout.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSummaryResponse {

    private long durationSeconds;
    private double totalVolume;
    private int totalSets;
}