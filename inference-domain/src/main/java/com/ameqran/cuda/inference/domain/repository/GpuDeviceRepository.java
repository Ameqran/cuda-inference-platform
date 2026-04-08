package com.ameqran.cuda.inference.domain.repository;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GpuDeviceRepository {

    Optional<GpuDevice> findById(UUID deviceId);

    Optional<GpuDevice> findBestAvailable();

    List<GpuDevice> findAll();

    void save(GpuDevice gpuDevice);
}
