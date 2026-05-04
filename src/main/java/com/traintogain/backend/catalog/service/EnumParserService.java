package com.traintogain.backend.catalog.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnumParserService {

    public <T extends Enum<T>> List<T> parse(List<String> values, Class<T> clazz) {

        if (values == null || values.isEmpty()) {
            return List.of();
        }

        List<T> result = new ArrayList<>();
        List<String> invalid = new ArrayList<>();

        for (String raw : values) {

            String normalized = normalize(raw);

            try {
                result.add(Enum.valueOf(clazz, normalized));
            } catch (Exception e) {
                invalid.add(raw);
            }
        }

        if (!invalid.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "invalid " + clazz.getSimpleName() + ": " + invalid
            );
        }

        return result;
    }

    private String normalize(String value) {

        if (value == null) return null;

        return value.trim()
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase();
    }
}