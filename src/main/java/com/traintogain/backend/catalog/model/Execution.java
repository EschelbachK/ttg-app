package com.traintogain.backend.catalog.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Execution {

    private String tempo;
    private RangeOfMotion rangeOfMotion;
    private SpeedType speedType;

    public boolean isValidTempo() {
        if (tempo == null || tempo.isBlank()) return false;
        String[] parts = tempo.split("-");
        if (parts.length != 3) return false;
        for (String p : parts) {
            try {
                int v = Integer.parseInt(p);
                if (v < 0 || v > 20) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}