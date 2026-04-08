package com.ameqran.cuda.inference.domain.event;

import com.ameqran.cuda.inference.domain.value.ModelId;

import java.time.Instant;

public record ModelRegisteredEvent(ModelId modelId, Instant registeredAt) {
}
