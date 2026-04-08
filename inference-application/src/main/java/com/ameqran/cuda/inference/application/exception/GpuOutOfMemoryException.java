package com.ameqran.cuda.inference.application.exception;

public class GpuOutOfMemoryException extends RuntimeException {

    public GpuOutOfMemoryException(String message) {
        super(message);
    }
}
