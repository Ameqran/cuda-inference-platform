package com.ameqran.cuda.inference.application.command;

import com.ameqran.cuda.inference.domain.value.ModelId;

import java.util.Arrays;

public record SubmitInferenceJobCommand(ModelId modelId, float[] inputData, int[] inputShape) {

    public SubmitInferenceJobCommand {
        if (modelId == null || inputData == null || inputShape == null) {
            throw new IllegalArgumentException("SubmitInferenceJobCommand fields cannot be null");
        }
        inputData = Arrays.copyOf(inputData, inputData.length);
        inputShape = Arrays.copyOf(inputShape, inputShape.length);
    }
}
