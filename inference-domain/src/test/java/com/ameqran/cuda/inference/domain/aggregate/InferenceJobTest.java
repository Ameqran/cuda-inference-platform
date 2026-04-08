package com.ameqran.cuda.inference.domain.aggregate;

import com.ameqran.cuda.inference.domain.exception.DomainException;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.ModelId;
import com.ameqran.cuda.inference.domain.value.Tensor;
import com.ameqran.cuda.inference.domain.value.TensorPayload;
import com.ameqran.cuda.inference.domain.value.DType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InferenceJobTest {

    @Test
    void shouldFollowQueuedRunningCompletedStateMachine() {
        InferenceJob job = InferenceJob.queue(
                JobId.newId(),
                ModelId.newId(),
                new TensorPayload(new Tensor(new float[]{1f, 2f}, new int[]{1, 2}, DType.FLOAT32))
        );

        assertEquals(JobStatus.QUEUED, job.status());

        job.markRunning();
        assertEquals(JobStatus.RUNNING, job.status());

        job.markCompleted(new TensorPayload(new Tensor(new float[]{3f, 4f}, new int[]{1, 2}, DType.FLOAT32)));

        assertEquals(JobStatus.COMPLETED, job.status());
        assertNotNull(job.completedAt());
    }

    @Test
    void shouldRejectInvalidStateTransitionToCompleted() {
        InferenceJob job = InferenceJob.queue(
                JobId.newId(),
                ModelId.newId(),
                new TensorPayload(new Tensor(new float[]{1f}, new int[]{1, 1}, DType.FLOAT32))
        );

        assertThrows(DomainException.class, () ->
                job.markCompleted(new TensorPayload(new Tensor(new float[]{1f}, new int[]{1, 1}, DType.FLOAT32)))
        );
    }
}
