package com.ameqran.cuda.inference.infrastructure.onnx;

import com.ameqran.cuda.inference.infrastructure.cuda.CudaRuntime;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class WeightUploader {

    private final CudaRuntime cudaRuntime;

    public WeightUploader(CudaRuntime cudaRuntime) {
        this.cudaRuntime = cudaRuntime;
    }

    public MemorySegment upload(float[] weights, Arena arena) {
        MemorySegment hostWeights = arena.allocate(ValueLayout.JAVA_FLOAT, weights.length);
        for (int i = 0; i < weights.length; i++) {
            hostWeights.setAtIndex(ValueLayout.JAVA_FLOAT, i, weights[i]);
        }

        MemorySegment deviceWeights = cudaRuntime.cudaMalloc((long) weights.length * Float.BYTES, arena);
        cudaRuntime.cudaMemcpy(deviceWeights, hostWeights, (long) weights.length * Float.BYTES);
        return deviceWeights;
    }
}
