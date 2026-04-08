package com.ameqran.cuda.inference.infrastructure.persistence.mapper;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.infrastructure.persistence.entity.GpuDeviceEntity;

public final class GpuDeviceMapper {

    public GpuDeviceEntity toEntity(GpuDevice gpuDevice) {
        GpuDeviceEntity entity = new GpuDeviceEntity();
        entity.setId(gpuDevice.deviceId());
        entity.setCudaIndex(gpuDevice.cudaDeviceIndex());
        entity.setTotalMemoryBytes(gpuDevice.totalMemoryBytes());
        entity.setFreeMemoryBytes(gpuDevice.freeMemoryBytes());
        return entity;
    }

    public GpuDevice toDomain(GpuDeviceEntity entity) {
        return new GpuDevice(entity.getId(), entity.getCudaIndex(), entity.getTotalMemoryBytes(), entity.getFreeMemoryBytes());
    }
}
