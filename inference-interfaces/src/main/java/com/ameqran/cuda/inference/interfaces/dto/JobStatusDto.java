package com.ameqran.cuda.inference.interfaces.dto;

public record JobStatusDto(String status, TensorDto result, String error) {
}
