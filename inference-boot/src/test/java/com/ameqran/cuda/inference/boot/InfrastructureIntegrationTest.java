package com.ameqran.cuda.inference.boot;

import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.aggregate.ModelFormat;
import com.ameqran.cuda.inference.domain.layer.LayerGraph;
import com.ameqran.cuda.inference.domain.layer.LayerNode;
import com.ameqran.cuda.inference.domain.repository.ModelRepository;
import com.ameqran.cuda.inference.domain.value.ModelId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class InfrastructureIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("inference")
            .withUsername("inference")
            .withPassword("inference");

    @Autowired
    private ModelRepository modelRepository;

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("platform.cuda.mode", () -> "mock");
    }

    @Test
    void shouldPersistAndReloadModelThroughDomainRepository() {
        Model model = new Model(
                ModelId.newId(),
                "mnist-v1",
                ModelFormat.ONNX,
                new LayerGraph(List.of(new LayerNode("input", "Input", List.of(), List.of("x")))),
                Instant.now()
        );

        modelRepository.save(model);

        assertTrue(modelRepository.findById(model.modelId()).isPresent());
    }
}
