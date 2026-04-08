package com.ameqran.cuda.inference.application.port;

import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

public interface InputTensorValidator {

    void validate(Model model, TensorPayload tensorPayload);
}
