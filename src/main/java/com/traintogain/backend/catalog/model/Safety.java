package com.traintogain.backend.catalog.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Safety {

    private boolean spotterRequired;

    @Builder.Default
    private List<RiskType> commonRisks = List.of();
}