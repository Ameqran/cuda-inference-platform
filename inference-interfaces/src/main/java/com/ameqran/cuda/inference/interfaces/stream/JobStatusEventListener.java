package com.ameqran.cuda.inference.interfaces.stream;

import com.ameqran.cuda.inference.application.query.GetJobResultQuery;
import com.ameqran.cuda.inference.application.query.JobResultView;
import com.ameqran.cuda.inference.application.usecase.GetJobResultHandler;
import com.ameqran.cuda.inference.domain.event.InferenceJobCompletedEvent;
import com.ameqran.cuda.inference.domain.event.InferenceJobFailedEvent;
import com.ameqran.cuda.inference.domain.event.InferenceJobQueuedEvent;
import org.springframework.context.event.EventListener;

public class JobStatusEventListener {

    private final JobStatusStreamService streamService;
    private final GetJobResultHandler getJobResultHandler;

    public JobStatusEventListener(JobStatusStreamService streamService, GetJobResultHandler getJobResultHandler) {
        this.streamService = streamService;
        this.getJobResultHandler = getJobResultHandler;
    }

    @EventListener
    public void onQueued(InferenceJobQueuedEvent event) {
        streamService.publishQueued(event.jobId());
    }

    @EventListener
    public void onCompleted(InferenceJobCompletedEvent event) {
        JobResultView result = getJobResultHandler.handle(new GetJobResultQuery(event.jobId()));
        streamService.publishResult(result);
    }

    @EventListener
    public void onFailed(InferenceJobFailedEvent event) {
        JobResultView result = getJobResultHandler.handle(new GetJobResultQuery(event.jobId()));
        streamService.publishResult(result);
    }
}
