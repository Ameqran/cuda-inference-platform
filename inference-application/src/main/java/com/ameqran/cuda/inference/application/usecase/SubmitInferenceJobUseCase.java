package com.ameqran.cuda.inference.application.usecase;

import com.ameqran.cuda.inference.application.command.SubmitInferenceJobCommand;
import com.ameqran.cuda.inference.application.event.DomainEventPublisher;
import com.ameqran.cuda.inference.application.exception.ModelNotFoundException;
import com.ameqran.cuda.inference.application.port.InputTensorValidator;
import com.ameqran.cuda.inference.domain.aggregate.InferenceJob;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.event.InferenceJobQueuedEvent;
import com.ameqran.cuda.inference.domain.repository.InferenceJobRepository;
import com.ameqran.cuda.inference.domain.repository.ModelRepository;
import com.ameqran.cuda.inference.domain.value.DType;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.Tensor;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

import java.time.Instant;

public final class SubmitInferenceJobUseCase {

    private final ModelRepository modelRepository;
    private final InferenceJobRepository jobRepository;
    private final InputTensorValidator inputTensorValidator;
    private final DomainEventPublisher eventPublisher;

    public SubmitInferenceJobUseCase(ModelRepository modelRepository,
                                     InferenceJobRepository jobRepository,
                                     InputTensorValidator inputTensorValidator,
                                     DomainEventPublisher eventPublisher) {
        this.modelRepository = modelRepository;
        this.jobRepository = jobRepository;
        this.inputTensorValidator = inputTensorValidator;
        this.eventPublisher = eventPublisher;
    }

    public JobId handle(SubmitInferenceJobCommand command) {
        Model model = modelRepository.findById(command.modelId())
                .orElseThrow(() -> new ModelNotFoundException("Model not found: " + command.modelId().value()));

        TensorPayload input = new TensorPayload(new Tensor(command.inputData(), command.inputShape(), DType.FLOAT32));
        inputTensorValidator.validate(model, input);

        JobId jobId = JobId.newId();
        InferenceJob job = InferenceJob.queue(jobId, command.modelId(), input);
        jobRepository.save(job);

        eventPublisher.publish(new InferenceJobQueuedEvent(jobId, command.modelId(), Instant.now()));
        return jobId;
    }
}
