package com.traintogain.backend.catalog.repository;

import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.EquipmentType;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExerciseCatalogRepository extends MongoRepository<ExerciseCatalog, String> {

    List<ExerciseCatalog> findByBodyRegion(BodyRegion bodyRegion);

    List<ExerciseCatalog> findByNameContainingIgnoreCase(String name);

    List<ExerciseCatalog> findByEquipment(EquipmentType equipment);

    List<ExerciseCatalog> findByBodyRegionAndEquipment(
            BodyRegion bodyRegion,
            EquipmentType equipment
    );
}