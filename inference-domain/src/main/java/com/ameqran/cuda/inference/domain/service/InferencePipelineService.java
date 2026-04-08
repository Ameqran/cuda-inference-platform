package com.ameqran.cuda.inference.domain.service;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

public interface InferencePipelineService {

    TensorPayload run(Model model, TensorPayload input, GpuDevice gpuDevice);
}
