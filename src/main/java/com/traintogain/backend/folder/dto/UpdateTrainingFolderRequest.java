package com.traintogain.backend.folder.dto;

import com.traintogain.backend.common.BodyRegion;

public class UpdateTrainingFolderRequest {

    private String name;
    private BodyRegion bodyRegion;
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