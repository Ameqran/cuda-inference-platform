package com.ameqran.cuda.inference.infrastructure.persistence.repository;

import com.ameqran.cuda.inference.infrastructure.persistence.entity.GpuDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataGpuDeviceRepository extends JpaRepository<GpuDeviceEntity, UUID> {

    Optional<GpuDeviceEntity> findFirstByOrderByFreeMemoryBytesDesc();
}
