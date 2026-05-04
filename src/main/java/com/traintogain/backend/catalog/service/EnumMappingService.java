package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.dto.EnumOptionResponse;
import com.traintogain.backend.catalog.model.*;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EnumMappingService {

    public List<EnumOptionResponse> mapBodyRegions() {
        return Arrays.stream(BodyRegion.values())
                .map(e -> map(e.name(), e.getDisplayName()))
                .toList();
    }

    public List<EnumOptionResponse> mapEquipment() {
        return Arrays.stream(EquipmentType.values())
                .map(e -> map(e.name(), e.getDisplayName()))
                .toList();
    }

    public List<EnumOptionResponse> mapPatterns() {
        return Arrays.stream(MovementPattern.values())
                .map(e -> map(e.name(), e.getDisplayName()))
                .toList();
    }

    private EnumOptionResponse map(String key, String label) {
        return EnumOptionResponse.builder()
                .key(key)
                .label(label != null ? label : key)
                .build();
    }
}