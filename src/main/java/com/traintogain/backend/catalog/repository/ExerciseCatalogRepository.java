package com.traintogain.backend.catalog.repository;

import com.traintogain.backend.catalog.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExerciseCatalogRepository extends MongoRepository<ExerciseCatalog, String> {

    Page<ExerciseCatalog> findAll(Pageable pageable);

    List<ExerciseCatalog> findByBodyRegion(BodyRegion bodyRegion);

    List<ExerciseCatalog> findByEquipment(EquipmentType equipment);

    List<ExerciseCatalog> findByMovementPattern(MovementPattern movementPattern);

    List<ExerciseCatalog> findByBodyRegionAndEquipment(
            BodyRegion bodyRegion,
            EquipmentType equipment
    );

    List<ExerciseCatalog> findByBodyRegionAndMovementPattern(
            BodyRegion bodyRegion,
            MovementPattern movementPattern
    );

    List<ExerciseCatalog> findByEquipmentAndMovementPattern(
            EquipmentType equipment,
            MovementPattern movementPattern
    );

    List<ExerciseCatalog> findByBodyRegionAndEquipmentAndMovementPattern(
            BodyRegion bodyRegion,
            EquipmentType equipment,
            MovementPattern movementPattern
    );

    List<ExerciseCatalog> findByNameContainingIgnoreCase(String name);

}