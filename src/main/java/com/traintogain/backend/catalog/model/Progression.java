package com.traintogain.backend.catalog.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Progression {
    private List<String> regressions;
    private List<String> progressions;
}