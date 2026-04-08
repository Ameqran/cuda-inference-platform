package com.ameqran.cuda.inference.boot.event;

import com.ameqran.cuda.inference.application.service.JobDispatcher;
import com.ameqran.cuda.inference.domain.event.InferenceJobQueuedEvent;
import org.springframework.context.event.EventListener;

public class QueuedInferenceJobListener {

    private final JobDispatcher jobDispatcher;

    public QueuedInferenceJobListener(JobDispatcher jobDispatcher) {
        this.jobDispatcher = jobDispatcher;
    }

    @EventListener
    public void onQueued(InferenceJobQueuedEvent event) {
        jobDispatcher.dispatch(event);
    }
}
