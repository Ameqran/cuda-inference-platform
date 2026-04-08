package com.ameqran.cuda.inference.interfaces.dto;

import java.util.UUID;

public record GpuDeviceDto(UUID id, int cudaDeviceIndex, long totalMemoryBytes, long freeMemoryBytes) {
}
