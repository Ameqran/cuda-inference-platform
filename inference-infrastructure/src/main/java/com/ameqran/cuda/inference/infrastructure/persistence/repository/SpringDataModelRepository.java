package com.ameqran.cuda.inference.infrastructure.persistence.repository;

import com.ameqran.cuda.inference.infrastructure.persistence.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataModelRepository extends JpaRepository<ModelEntity, UUID> {
}
