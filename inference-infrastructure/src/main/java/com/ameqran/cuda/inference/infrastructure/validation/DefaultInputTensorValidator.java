package com.ameqran.cuda.inference.infrastructure.validation;

import com.ameqran.cuda.inference.application.port.InputTensorValidator;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.value.BatchSize;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

public class DefaultInputTensorValidator implements InputTensorValidator {

    @Override
    public void validate(Model model, TensorPayload tensorPayload) {
        int[] shape = tensorPayload.tensor().shape();
        if (shape.length == 0) {
            throw new IllegalArgumentException("Input shape must contain at least one dimension");
        }

        new BatchSize(shape[0]);

        if (model.layers().size() == 0) {
            throw new IllegalArgumentException("Model has no layers");
        }
    }
}
