package com.ameqran.cuda.inference.interfaces.dto;

import java.time.Instant;
import java.util.UUID;

public record ModelDto(UUID id, String name, String format, int layerCount, Instant registeredAt) {
}
