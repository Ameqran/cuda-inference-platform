package com.ameqran.cuda.inference.application.service;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.value.JobId;

public interface GpuExecutorService {

    void submit(JobId jobId, GpuDevice device);
}
