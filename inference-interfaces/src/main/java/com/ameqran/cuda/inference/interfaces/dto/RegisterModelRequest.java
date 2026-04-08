package com.ameqran.cuda.inference.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterModelRequest(@NotBlank String name,
                                   @NotBlank String format,
                                   @NotBlank String onnxBase64) {
}
