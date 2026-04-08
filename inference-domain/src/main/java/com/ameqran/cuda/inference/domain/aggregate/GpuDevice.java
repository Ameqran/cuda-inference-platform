package com.ameqran.cuda.inference.domain.aggregate;

import java.util.UUID;

public final class GpuDevice {

    private final UUID deviceId;
    private final int cudaDeviceIndex;
    private final long totalMemoryBytes;
    private long freeMemoryBytes;

    public GpuDevice(UUID deviceId, int cudaDeviceIndex, long totalMemoryBytes, long freeMemoryBytes) {
        if (deviceId == null) {
            throw new IllegalArgumentException("DeviceId cannot be null");
        }
        if (cudaDeviceIndex < 0) {
            throw new IllegalArgumentException("CUDA device index must be >= 0");
        }
        if (totalMemoryBytes <= 0 || freeMemoryBytes < 0 || freeMemoryBytes > totalMemoryBytes) {
            throw new IllegalArgumentException("GPU memory values are invalid");
        }

        this.deviceId = deviceId;
        this.cudaDeviceIndex = cudaDeviceIndex;
        this.totalMemoryBytes = totalMemoryBytes;
        this.freeMemoryBytes = freeMemoryBytes;
    }

    public UUID deviceId() {
        return deviceId;
    }

    public int cudaDeviceIndex() {
        return cudaDeviceIndex;
    }

    public long totalMemoryBytes() {
        return totalMemoryBytes;
    }

    public long freeMemoryBytes() {
        return freeMemoryBytes;
    }

    public boolean canReserve(long bytes) {
        return bytes > 0 && freeMemoryBytes >= bytes;
    }

    public void reserve(long bytes) {
        if (!canReserve(bytes)) {
            throw new IllegalArgumentException("Insufficient GPU memory");
        }
        freeMemoryBytes -= bytes;
    }

    public void release(long bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("Release bytes must be >= 0");
        }
        freeMemoryBytes = Math.min(totalMemoryBytes, freeMemoryBytes + bytes);
    }
}
