package com.traintogain.backend.workout.domain;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetLog {

    private String id;

    private double weight;

    private int reps;

    private Instant createdAt;

    private Instant restStartedAt;

    private int restSeconds;

    public static SetLog create(double weight, int reps) {
        return SetLog.builder()
                .id(UUID.randomUUID().toString())
                .weight(weight)
                .reps(reps)
                .createdAt(Instant.now())
                .build();
    }
}