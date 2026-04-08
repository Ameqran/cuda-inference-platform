package com.ameqran.cuda.inference.boot.service;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.service.InferencePipelineService;
import com.ameqran.cuda.inference.domain.value.TensorPayload;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

public class ObservedInferencePipelineService implements InferencePipelineService {

    private final InferencePipelineService delegate;
    private final Timer latencyTimer;

    public ObservedInferencePipelineService(InferencePipelineService delegate, MeterRegistry meterRegistry) {
        this.delegate = delegate;
        this.latencyTimer = Timer.builder("inference_latency_p99")
                .publishPercentileHistogram()
                .publishPercentiles(0.99)
                .register(meterRegistry);
    }

    @Override
    @Retry(name = "gpuExecution")
    @CircuitBreaker(name = "gpuExecution")
    public TensorPayload run(Model model, TensorPayload input, GpuDevice gpuDevice) {
        long start = System.nanoTime();
        try {
            return delegate.run(model, input, gpuDevice);
        } finally {
            latencyTimer.record(System.nanoTime() - start, java.util.concurrent.TimeUnit.NANOSECONDS);
        }
    }
}
