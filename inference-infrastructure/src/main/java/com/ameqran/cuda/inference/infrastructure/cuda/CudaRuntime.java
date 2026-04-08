package com.ameqran.cuda.inference.infrastructure.cuda;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class CudaRuntime {

    public MemorySegment cudaMalloc(long bytes, Arena arena) {
        if (bytes <= 0) {
            throw new IllegalArgumentException("cudaMalloc requires bytes > 0");
        }
        return arena.allocate(bytes);
    }

    public void cudaFree(MemorySegment segment) {
        if (segment == null) {
            throw new IllegalArgumentException("cudaFree requires non-null segment");
        }
    }

    public void cudaMemcpy(MemorySegment destination, MemorySegment source, long bytes) {
        if (destination == null || source == null) {
            throw new IllegalArgumentException("cudaMemcpy requires source and destination");
        }
        destination.asSlice(0, bytes).copyFrom(source.asSlice(0, bytes));
    }

    public void cudaDeviceSynchronize() {
    }

    public GpuDeviceProperties cudaGetDeviceProperties(int cudaDeviceIndex) {
        if (cudaDeviceIndex < 0) {
            throw new IllegalArgumentException("cudaDeviceIndex must be >= 0");
        }
        return new GpuDeviceProperties(cudaDeviceIndex, "mock-gpu-" + cudaDeviceIndex, 16L * 1024 * 1024 * 1024);
    }

    public record GpuDeviceProperties(int index, String name, long totalMemoryBytes) {
    }
}
