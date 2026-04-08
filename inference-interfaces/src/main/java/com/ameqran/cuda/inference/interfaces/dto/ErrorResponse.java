package com.ameqran.cuda.inference.interfaces.dto;

import java.time.Instant;

public record ErrorResponse(int status, String message, Instant timestamp) {
}
