package com.ameqran.cuda.inference.interfaces.stream;

import com.ameqran.cuda.inference.application.query.JobResultView;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.interfaces.dto.JobStatusDto;
import com.ameqran.cuda.inference.interfaces.mapper.ApiMapper;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class JobStatusStreamService {

    private final Map<UUID, List<SseEmitter>> emittersByJobId = new ConcurrentHashMap<>();
    private final AtomicInteger queuedJobsGauge = new AtomicInteger(0);
    private final ApiMapper mapper = new ApiMapper();

    public SseEmitter openStream(JobId jobId) {
        SseEmitter emitter = new SseEmitter(60_000L);
        emittersByJobId.computeIfAbsent(jobId.value(), ignored -> new ArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(jobId.value(), emitter));
        emitter.onTimeout(() -> removeEmitter(jobId.value(), emitter));
        return emitter;
    }

    public void publishQueued(JobId jobId) {
        queuedJobsGauge.incrementAndGet();
        publish(jobId.value(), new JobStatusDto("QUEUED", null, null));
    }

    public void publishResult(JobResultView jobResultView) {
        if ("COMPLETED".equals(jobResultView.status().name()) || "FAILED".equals(jobResultView.status().name())) {
            queuedJobsGauge.updateAndGet(current -> Math.max(current - 1, 0));
        }
        publish(jobResultView.jobId().value(), mapper.toJobStatusDto(jobResultView));
    }

    public int queuedJobsGaugeValue() {
        return queuedJobsGauge.get();
    }

    private void publish(UUID jobId, Object payload) {
        List<SseEmitter> emitters = emittersByJobId.get(jobId);
        if (emitters == null || emitters.isEmpty()) {
            return;
        }

        List<SseEmitter> disconnected = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("job-status").data(payload));
            } catch (IOException e) {
                disconnected.add(emitter);
            }
        }
        emitters.removeAll(disconnected);
    }

    private void removeEmitter(UUID jobId, SseEmitter emitter) {
        List<SseEmitter> emitters = emittersByJobId.get(jobId);
        if (emitters == null) {
            return;
        }
        emitters.remove(emitter);
        if (emitters.isEmpty()) {
            emittersByJobId.remove(jobId);
        }
    }
}
