package com.traintogain.backend.catalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExerciseMediaService {

    private final String baseUrl;

    public ExerciseMediaService(
            @Value("${media.base-url}") String baseUrl
    ) {
        this.baseUrl = baseUrl;
    }

    public String buildImage(String id, String file) {
        return resolve(id, file);
    }

    public String buildAnimation(String id, String file) {
        return resolve(id, file);
    }

    public String buildThumbnail(String id, String file) {
        return resolve(id, file);
    }

    private String resolve(String id, String file) {

        if (isBlank(baseUrl)) {
            throw new IllegalStateException("media.base-url missing");
        }

        if (isBlank(id)) {
            throw new IllegalStateException("exercise id missing");
        }

        if (isBlank(file)) {
            return null;
        }

        return build(baseUrl, "exercises/" + id + "/" + file);
    }

    private String build(String base, String path) {

        String cleanBase = base.endsWith("/")
                ? base.substring(0, base.length() - 1)
                : base;

        String cleanPath = path.startsWith("/")
                ? path.substring(1)
                : path;

        return cleanBase + "/" + cleanPath;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}