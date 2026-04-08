package com.ameqran.cuda.inference.application.command;

import com.ameqran.cuda.inference.domain.aggregate.ModelFormat;

public record RegisterModelCommand(String name, ModelFormat format, byte[] onnxBytes) {

    public RegisterModelCommand {
        if (name == null || name.isBlank() || format == null || onnxBytes == null || onnxBytes.length == 0) {
            throw new IllegalArgumentException("RegisterModelCommand is invalid");
        }
    }
}
