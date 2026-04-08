package com.ameqran.cuda.inference.domain.aggregate;

public enum JobStatus {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED;

    public boolean terminal() {
        return this == COMPLETED || this == FAILED;
    }
}
