package com.ameqran.cuda.inference.interfaces.dto;

public record TensorDto(float[] data, int[] shape, String dtype) {
}
