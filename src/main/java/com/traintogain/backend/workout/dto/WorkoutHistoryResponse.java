package com.traintogain.backend.workout.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutHistoryResponse {

    private String exerciseId;
    private List<SetData> lastSets;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetData {
        private double weight;
        private int reps;
    }
}