package com.ameqran.cuda.inference.infrastructure.cuda;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.service.InferencePipelineService;
import com.ameqran.cuda.inference.domain.value.Tensor;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class CudaInferencePipelineService implements InferencePipelineService {

    private final CudaRuntime cudaRuntime;
    private final CudaKernelLauncher kernelLauncher;

    public CudaInferencePipelineService(CudaRuntime cudaRuntime, CudaKernelLauncher kernelLauncher) {
        this.cudaRuntime = cudaRuntime;
        this.kernelLauncher = kernelLauncher;
    }

    @Override
    public TensorPayload run(Model model, TensorPayload input, GpuDevice gpuDevice) {
        int elementCount = input.tensor().data().length;
        long requestedBytes = (long) elementCount * Float.BYTES * 2;
        if (!gpuDevice.canReserve(requestedBytes)) {
            throw new CudaException("Not enough free GPU memory to execute model " + model.modelId().value());
        }

        gpuDevice.reserve(requestedBytes);
        try (Arena arena = Arena.ofShared()) {
            MemorySegment hostInput = arena.allocate(ValueLayout.JAVA_FLOAT, elementCount);
            for (int i = 0; i < elementCount; i++) {
                hostInput.setAtIndex(ValueLayout.JAVA_FLOAT, i, input.tensor().data()[i]);
            }

            MemorySegment deviceInput = cudaRuntime.cudaMalloc((long) elementCount * Float.BYTES, arena);
            MemorySegment deviceOutput = cudaRuntime.cudaMalloc((long) elementCount * Float.BYTES, arena);
            cudaRuntime.cudaMemcpy(deviceInput, hostInput, (long) elementCount * Float.BYTES);

            kernelLauncher.reluForward(deviceInput, deviceOutput, elementCount);
            cudaRuntime.cudaDeviceSynchronize();

            float[] resultData = new float[elementCount];
            for (int i = 0; i < elementCount; i++) {
                resultData[i] = deviceOutput.getAtIndex(ValueLayout.JAVA_FLOAT, i);
            }

            return new TensorPayload(new Tensor(resultData, input.tensor().shape(), input.tensor().dtype()));
        } finally {
            gpuDevice.release(requestedBytes);
        }
    }
}
