package com.ameqran.cuda.inference.application.query;

import com.ameqran.cuda.inference.domain.aggregate.ModelFormat;
import com.ameqran.cuda.inference.domain.value.ModelId;

import java.time.Instant;

public record ModelView(ModelId modelId, String name, ModelFormat format, int layerCount, Instant registeredAt) {
}
