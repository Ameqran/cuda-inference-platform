package com.ameqran.cuda.inference.interfaces.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubmitInferenceJobRequest(@NotNull UUID modelId,
                                        @NotEmpty float[] inputData,
                                        @NotEmpty int[] inputShape) {
}
