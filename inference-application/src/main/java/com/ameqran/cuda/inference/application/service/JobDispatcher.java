package com.ameqran.cuda.inference.application.service;

import com.ameqran.cuda.inference.domain.event.InferenceJobQueuedEvent;

public interface JobDispatcher {

    void dispatch(InferenceJobQueuedEvent event);
}
