package com.traintogain.backend.workout.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSession {

    private String id;

    private String exerciseId;

    private String name;

    private int order;

    private List<SetLog> sets;

    public static ExerciseSession create(String exerciseId, String name, int order) {
        return ExerciseSession.builder()
                .id(UUID.randomUUID().toString())
                .exerciseId(exerciseId)
                .name(name)
                .order(order)
                .sets(new java.util.ArrayList<>())
                .build();
    }
}