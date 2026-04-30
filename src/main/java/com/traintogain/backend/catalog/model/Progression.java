package com.traintogain.backend.catalog.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Progression {

    @Builder.Default
    private List<String> regressions = List.of();

    @Builder.Default
    private List<String> progressions = List.of();
}