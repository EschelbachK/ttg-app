package com.traintogain.backend.catalog.repository;

import com.traintogain.backend.catalog.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExerciseCatalogRepository extends MongoRepository<ExerciseCatalog, String> {

    Page<ExerciseCatalog> findByBodyRegion(BodyRegion bodyRegion, Pageable pageable);

    Page<ExerciseCatalog> findByEquipment(EquipmentType equipment, Pageable pageable);

    Page<ExerciseCatalog> findByMovementPattern(MovementPattern movementPattern, Pageable pageable);

    Page<ExerciseCatalog> findByBodyRegionAndEquipment(
            BodyRegion bodyRegion,
            EquipmentType equipment,
            Pageable pageable
    );

    Page<ExerciseCatalog> findByBodyRegionAndMovementPattern(
            BodyRegion bodyRegion,
            MovementPattern movementPattern,
            Pageable pageable
    );

    Page<ExerciseCatalog> findByEquipmentAndMovementPattern(
            EquipmentType equipment,
            MovementPattern movementPattern,
            Pageable pageable
    );

    Page<ExerciseCatalog> findByBodyRegionAndEquipmentAndMovementPattern(
            BodyRegion bodyRegion,
            EquipmentType equipment,
            MovementPattern movementPattern,
            Pageable pageable
    );
}