package com.ameqran.cuda.inference.infrastructure.persistence.repository;

import com.ameqran.cuda.inference.infrastructure.persistence.entity.InferenceJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataInferenceJobRepository extends JpaRepository<InferenceJobEntity, UUID> {
}
