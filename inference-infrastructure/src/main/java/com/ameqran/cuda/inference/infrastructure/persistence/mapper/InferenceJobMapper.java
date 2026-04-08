package com.ameqran.cuda.inference.infrastructure.persistence.mapper;

import com.ameqran.cuda.inference.domain.aggregate.JobStatus;
import com.ameqran.cuda.inference.domain.aggregate.InferenceJob;
import com.ameqran.cuda.inference.domain.value.DType;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.ModelId;
import com.ameqran.cuda.inference.domain.value.Tensor;
import com.ameqran.cuda.inference.domain.value.TensorPayload;
import com.ameqran.cuda.inference.infrastructure.persistence.entity.InferenceJobEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public final class InferenceJobMapper {

    public InferenceJobEntity toEntity(InferenceJob job) {
        InferenceJobEntity entity = new InferenceJobEntity();
        entity.setId(job.jobId().value());
        entity.setModelId(job.modelId().value());
        entity.setStatus(job.status().name());
        entity.setInputShape(toBoxed(job.input().tensor().shape()));
        entity.setInputData(TensorBinaryCodec.encode(job.input().tensor().data()));
        entity.setResultData(job.result() == null ? null : TensorBinaryCodec.encode(job.result().tensor().data()));
        entity.setSubmittedAt(OffsetDateTime.ofInstant(job.submittedAt(), ZoneOffset.UTC));
        entity.setCompletedAt(job.completedAt() == null ? null : OffsetDateTime.ofInstant(job.completedAt(), ZoneOffset.UTC));
        entity.setErrorMsg(job.failureReason());
        return entity;
    }

    public InferenceJob toDomain(InferenceJobEntity entity) {
        int[] shape = toPrimitive(entity.getInputShape());
        float[] inputData = TensorBinaryCodec.decode(entity.getInputData());
        if (inputData.length == 0) {
            inputData = new float[Math.max(1, product(shape))];
        }

        TensorPayload input = new TensorPayload(new Tensor(inputData, shape, DType.FLOAT32));

        TensorPayload result = null;
        if (entity.getResultData() != null && entity.getResultData().length > 0) {
            float[] resultData = TensorBinaryCodec.decode(entity.getResultData());
            int[] resultShape = resultData.length == product(shape) ? shape : new int[]{1, resultData.length};
            result = new TensorPayload(new Tensor(resultData, resultShape, DType.FLOAT32));
        }

        return InferenceJob.rehydrate(
                new JobId(entity.getId()),
                new ModelId(entity.getModelId()),
                input,
                JobStatus.valueOf(entity.getStatus()),
                entity.getSubmittedAt().toInstant(),
                entity.getCompletedAt() == null ? null : entity.getCompletedAt().toInstant(),
                result,
                entity.getErrorMsg()
        );
    }

    private Integer[] toBoxed(int[] values) {
        Integer[] boxed = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            boxed[i] = values[i];
        }
        return boxed;
    }

    private int[] toPrimitive(Integer[] values) {
        if (values == null) {
            return new int[]{1, 1};
        }
        return Arrays.stream(values).mapToInt(Integer::intValue).toArray();
    }

    private int product(int[] shape) {
        int product = 1;
        for (int value : shape) {
            product *= value;
        }
        return product;
    }
}
