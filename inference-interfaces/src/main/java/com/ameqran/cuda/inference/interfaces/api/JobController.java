package com.ameqran.cuda.inference.interfaces.api;

import com.ameqran.cuda.inference.application.command.SubmitInferenceJobCommand;
import com.ameqran.cuda.inference.application.query.GetJobResultQuery;
import com.ameqran.cuda.inference.application.usecase.GetJobResultHandler;
import com.ameqran.cuda.inference.application.usecase.SubmitInferenceJobUseCase;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.ModelId;
import com.ameqran.cuda.inference.interfaces.dto.JobStatusDto;
import com.ameqran.cuda.inference.interfaces.dto.SubmitInferenceJobRequest;
import com.ameqran.cuda.inference.interfaces.mapper.ApiMapper;
import com.ameqran.cuda.inference.interfaces.stream.JobStatusStreamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final SubmitInferenceJobUseCase submitInferenceJobUseCase;
    private final GetJobResultHandler getJobResultHandler;
    private final JobStatusStreamService streamService;
    private final ApiMapper mapper = new ApiMapper();

    public JobController(SubmitInferenceJobUseCase submitInferenceJobUseCase,
                         GetJobResultHandler getJobResultHandler,
                         JobStatusStreamService streamService) {
        this.submitInferenceJobUseCase = submitInferenceJobUseCase;
        this.getJobResultHandler = getJobResultHandler;
        this.streamService = streamService;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> submit(@Valid @RequestBody SubmitInferenceJobRequest request) {
        JobId jobId = submitInferenceJobUseCase.handle(
                new SubmitInferenceJobCommand(
                        new ModelId(request.modelId()),
                        request.inputData(),
                        request.inputShape()
                )
        );

        return ResponseEntity.accepted().body(Map.of("jobId", jobId.value()));
    }

    @GetMapping("/{id}")
    public JobStatusDto getById(@PathVariable("id") UUID id) {
        return mapper.toJobStatusDto(getJobResultHandler.handle(new GetJobResultQuery(new JobId(id))));
    }

    @GetMapping("/{id}/stream")
    public SseEmitter stream(@PathVariable("id") UUID id) {
        return streamService.openStream(new JobId(id));
    }
}
