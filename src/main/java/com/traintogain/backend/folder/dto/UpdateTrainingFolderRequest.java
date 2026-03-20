package com.traintogain.backend.folder.dto;

import com.traintogain.backend.common.BodyRegion;
import jakarta.validation.constraints.Min;

public class UpdateTrainingFolderRequest {

    private String name;
    private BodyRegion bodyRegion;

    @Min(value = 0, message = "Reihenfolge muss >= 0 sein")
    private Integer order;

    public String getName() {
        return name;
    }

    public BodyRegion getBodyRegion() {
        return bodyRegion;
    }

    public Integer getOrder() {
        return order;
    }
}