package com.traintogain.backend.catalog.seed;

import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.EquipmentType;
import com.traintogain.backend.catalog.model.ExerciseCatalog;

import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExerciseCatalogSeeder implements CommandLineRunner {

    private final ExerciseCatalogRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) {
            return;
        }

        List<ExerciseCatalog> exercises = List.of(

                ExerciseCatalog.builder()
                        .id("bench_press")
                        .name("Bankdrücken")
                        .bodyRegion(BodyRegion.BRUST)
                        .equipment(EquipmentType.LANGHANTEL)
                        .imageUrl("/images/exercises/bench_press.png")
                        .animationUrl("/animations/bench_press.gif")
                        .build(),

                ExerciseCatalog.builder()
                        .id("incline_bench_press")
                        .name("Schrägbankdrücken")
                        .bodyRegion(BodyRegion.BRUST)
                        .equipment(EquipmentType.LANGHANTEL)
                        .imageUrl("/images/exercises/incline_bench_press.png")
                        .animationUrl("/animations/incline_bench_press.gif")
                        .build(),

                ExerciseCatalog.builder()
                        .id("butterfly_machine")
                        .name("Butterfly Maschine")
                        .bodyRegion(BodyRegion.BRUST)
                        .equipment(EquipmentType.MASCHINE)
                        .imageUrl("/images/exercises/butterfly_machine.png")
                        .animationUrl("/animations/butterfly_machine.gif")
                        .build()

        );

        repository.saveAll(exercises);
    }
}