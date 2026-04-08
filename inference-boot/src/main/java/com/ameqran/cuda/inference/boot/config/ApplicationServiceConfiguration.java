package com.ameqran.cuda.inference.boot.config;

import com.ameqran.cuda.inference.application.event.DomainEventPublisher;
import com.ameqran.cuda.inference.application.port.InputTensorValidator;
import com.ameqran.cuda.inference.application.port.ModelBinaryStore;
import com.ameqran.cuda.inference.application.port.ModelGraphParser;
import com.ameqran.cuda.inference.application.service.DefaultGpuExecutorService;
import com.ameqran.cuda.inference.application.service.DefaultJobDispatcher;
import com.ameqran.cuda.inference.application.service.GpuExecutorService;
import com.ameqran.cuda.inference.application.service.JobDispatcher;
import com.ameqran.cuda.inference.application.usecase.GetJobResultHandler;
import com.ameqran.cuda.inference.application.usecase.ListModelsUseCase;
import com.ameqran.cuda.inference.application.usecase.RegisterModelUseCase;
import com.ameqran.cuda.inference.application.usecase.SubmitInferenceJobUseCase;
import com.ameqran.cuda.inference.boot.event.QueuedInferenceJobListener;
import com.ameqran.cuda.inference.boot.service.ObservedInferencePipelineService;
import com.ameqran.cuda.inference.domain.repository.GpuDeviceRepository;
import com.ameqran.cuda.inference.domain.repository.InferenceJobRepository;
import com.ameqran.cuda.inference.domain.repository.ModelRepository;
import com.ameqran.cuda.inference.domain.service.InferencePipelineService;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaInferencePipelineService;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaKernelLauncher;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaRuntime;
import com.ameqran.cuda.inference.interfaces.stream.JobStatusEventListener;
import com.ameqran.cuda.inference.interfaces.stream.JobStatusStreamService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class ApplicationServiceConfiguration {

    @Bean(name = "cudaPipelineService")
    public InferencePipelineService cudaPipelineService(CudaRuntime cudaRuntime, CudaKernelLauncher kernelLauncher) {
        return new CudaInferencePipelineService(cudaRuntime, kernelLauncher);
    }

    @Bean
    @Primary
    public InferencePipelineService inferencePipelineService(@Qualifier("cudaPipelineService") InferencePipelineService cudaPipelineService,
                                                             MeterRegistry meterRegistry) {
        return new ObservedInferencePipelineService(cudaPipelineService, meterRegistry);
    }

    @Bean
    public SubmitInferenceJobUseCase submitInferenceJobUseCase(ModelRepository modelRepository,
                                                               InferenceJobRepository jobRepository,
                                                               InputTensorValidator inputTensorValidator,
                                                               DomainEventPublisher eventPublisher) {
        return new SubmitInferenceJobUseCase(modelRepository, jobRepository, inputTensorValidator, eventPublisher);
    }

    @Bean
    public GetJobResultHandler getJobResultHandler(InferenceJobRepository jobRepository) {
        return new GetJobResultHandler(jobRepository);
    }

    @Bean
    public RegisterModelUseCase registerModelUseCase(ModelRepository modelRepository,
                                                     ModelGraphParser modelGraphParser,
                                                     ModelBinaryStore modelBinaryStore,
                                                     DomainEventPublisher eventPublisher) {
        return new RegisterModelUseCase(modelRepository, modelGraphParser, modelBinaryStore, eventPublisher);
    }

    @Bean
    public ListModelsUseCase listModelsUseCase(ModelRepository modelRepository) {
        return new ListModelsUseCase(modelRepository);
    }

    @Bean
    public GpuExecutorService gpuExecutorService(TaskExecutor gpuTaskExecutor,
                                                 InferenceJobRepository jobRepository,
                                                 ModelRepository modelRepository,
                                                 GpuDeviceRepository gpuDeviceRepository,
                                                 InferencePipelineService inferencePipelineService,
                                                 DomainEventPublisher eventPublisher) {
        return new DefaultGpuExecutorService(gpuTaskExecutor, jobRepository, modelRepository, gpuDeviceRepository,
                inferencePipelineService, eventPublisher);
    }

    @Bean
    public JobDispatcher jobDispatcher(GpuDeviceRepository gpuDeviceRepository, GpuExecutorService gpuExecutorService) {
        return new DefaultJobDispatcher(gpuDeviceRepository, gpuExecutorService);
    }

    @Bean
    public JobStatusStreamService jobStatusStreamService() {
        return new JobStatusStreamService();
    }

    @Bean
    public JobStatusEventListener jobStatusEventListener(JobStatusStreamService streamService,
                                                         GetJobResultHandler getJobResultHandler) {
        return new JobStatusEventListener(streamService, getJobResultHandler);
    }

    @Bean
    public QueuedInferenceJobListener queuedInferenceJobListener(JobDispatcher jobDispatcher) {
        return new QueuedInferenceJobListener(jobDispatcher);
    }
}
