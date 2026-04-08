package com.ameqran.cuda.inference.boot.config;

import com.ameqran.cuda.inference.domain.repository.GpuDeviceRepository;
import com.ameqran.cuda.inference.interfaces.stream.JobStatusStreamService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfiguration {

    @Bean
    public Gauge jobsQueuedGauge(JobStatusStreamService streamService, MeterRegistry meterRegistry) {
        return Gauge.builder("jobs_queued_gauge", streamService, JobStatusStreamService::queuedJobsGaugeValue)
                .description("Number of queued jobs waiting for GPU execution")
                .register(meterRegistry);
    }

    @Bean
    public Gauge gpuMemoryUsedGauge(GpuDeviceRepository gpuDeviceRepository, MeterRegistry meterRegistry) {
        return Gauge.builder("gpu_memory_used_bytes", gpuDeviceRepository,
                        repository -> repository.findAll().stream()
                                .mapToDouble(device -> device.totalMemoryBytes() - device.freeMemoryBytes())
                                .sum())
                .description("GPU memory used across all registered devices")
                .register(meterRegistry);
    }
}
