package com.ameqran.cuda.inference.boot.benchmark;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.aggregate.ModelFormat;
import com.ameqran.cuda.inference.domain.layer.LayerGraph;
import com.ameqran.cuda.inference.domain.layer.LayerNode;
import com.ameqran.cuda.inference.domain.value.DType;
import com.ameqran.cuda.inference.domain.value.ModelId;
import com.ameqran.cuda.inference.domain.value.Tensor;
import com.ameqran.cuda.inference.domain.value.TensorPayload;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaInferencePipelineService;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaKernelLauncher;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaRuntime;
import com.ameqran.cuda.inference.infrastructure.cuda.MockCudaInferencePipelineService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PanamaCudaBenchmark {

    @Param({"1", "32", "128", "512"})
    int batchSize;

    private Model model;
    private GpuDevice gpuDevice;
    private TensorPayload input;
    private CudaInferencePipelineService cudaService;
    private MockCudaInferencePipelineService mockService;

    @Setup
    public void setup() {
        model = new Model(
                ModelId.newId(),
                "benchmark-model",
                ModelFormat.ONNX,
                new LayerGraph(List.of(new LayerNode("input", "Input", List.of(), List.of("x")))),
                Instant.now()
        );
        gpuDevice = new GpuDevice(UUID.randomUUID(), 0, 16L * 1024 * 1024 * 1024, 16L * 1024 * 1024 * 1024);

        int features = 128;
        int totalElements = batchSize * features;
        float[] data = new float[totalElements];
        for (int i = 0; i < totalElements; i++) {
            data[i] = i % 17;
        }
        input = new TensorPayload(new Tensor(data, new int[]{batchSize, features}, DType.FLOAT32));

        cudaService = new CudaInferencePipelineService(new CudaRuntime(), new CudaKernelLauncher());
        mockService = new MockCudaInferencePipelineService();
    }

    @Benchmark
    public TensorPayload panamaCudaPath() {
        return cudaService.run(model, input, gpuDevice);
    }

    @Benchmark
    public TensorPayload mockCpuFallback() {
        return mockService.run(model, input, gpuDevice);
    }
}
