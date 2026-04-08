package com.ameqran.cuda.inference.application.service;

import com.ameqran.cuda.inference.application.exception.GpuOutOfMemoryException;
import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.event.InferenceJobQueuedEvent;
import com.ameqran.cuda.inference.domain.repository.GpuDeviceRepository;

public final class DefaultJobDispatcher implements JobDispatcher {

    private final GpuDeviceRepository gpuDeviceRepository;
    private final GpuExecutorService gpuExecutorService;

    public DefaultJobDispatcher(GpuDeviceRepository gpuDeviceRepository, GpuExecutorService gpuExecutorService) {
        this.gpuDeviceRepository = gpuDeviceRepository;
        this.gpuExecutorService = gpuExecutorService;
    }

    @Override
    public void dispatch(InferenceJobQueuedEvent event) {
        GpuDevice device = gpuDeviceRepository.findBestAvailable()
                .orElseThrow(() -> new GpuOutOfMemoryException("No available GPU device to process job " + event.jobId().value()));

        gpuExecutorService.submit(event.jobId(), device);
    }
}
