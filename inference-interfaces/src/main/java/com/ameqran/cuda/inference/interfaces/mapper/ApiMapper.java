package com.ameqran.cuda.inference.interfaces.mapper;

import com.ameqran.cuda.inference.application.query.JobResultView;
import com.ameqran.cuda.inference.application.query.ModelView;
import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.value.TensorPayload;
import com.ameqran.cuda.inference.interfaces.dto.GpuDeviceDto;
import com.ameqran.cuda.inference.interfaces.dto.JobStatusDto;
import com.ameqran.cuda.inference.interfaces.dto.ModelDto;
import com.ameqran.cuda.inference.interfaces.dto.TensorDto;

public final class ApiMapper {

    public JobStatusDto toJobStatusDto(JobResultView view) {
        return new JobStatusDto(
                view.status().name(),
                toTensorDto(view.result()),
                view.failureReason()
        );
    }

    public ModelDto toModelDto(ModelView view) {
        return new ModelDto(
                view.modelId().value(),
                view.name(),
                view.format().name(),
                view.layerCount(),
                view.registeredAt()
        );
    }

    public GpuDeviceDto toGpuDeviceDto(GpuDevice gpuDevice) {
        return new GpuDeviceDto(
                gpuDevice.deviceId(),
                gpuDevice.cudaDeviceIndex(),
                gpuDevice.totalMemoryBytes(),
                gpuDevice.freeMemoryBytes()
        );
    }

    public TensorDto toTensorDto(TensorPayload payload) {
        if (payload == null) {
            return null;
        }
        return new TensorDto(
                payload.tensor().data(),
                payload.tensor().shape(),
                payload.tensor().dtype().name()
        );
    }
}
