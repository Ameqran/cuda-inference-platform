package com.ameqran.cuda.inference.infrastructure.cuda;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.service.InferencePipelineService;
import com.ameqran.cuda.inference.domain.value.DType;
import com.ameqran.cuda.inference.domain.value.Tensor;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

public class MockCudaInferencePipelineService implements InferencePipelineService {

    @Override
    public TensorPayload run(Model model, TensorPayload input, GpuDevice gpuDevice) {
        float[] inputData = input.tensor().data();
        float[] output = new float[inputData.length];
        for (int i = 0; i < inputData.length; i++) {
            output[i] = inputData[i] * 0.5f;
        }
        return new TensorPayload(new Tensor(output, input.tensor().shape(), DType.FLOAT32));
    }
}
