package com.ameqran.cuda.inference.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "inference_jobs")
public class InferenceJobEntity {

    @Id
    private UUID id;

    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    @Column(nullable = false)
    private String status;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "input_shape", columnDefinition = "integer[]")
    private Integer[] inputShape;

    @Column(name = "input_data", columnDefinition = "bytea")
    private byte[] inputData;

    @Column(name = "result_data", columnDefinition = "bytea")
    private byte[] resultData;

    @Column(name = "submitted_at", nullable = false)
    private OffsetDateTime submittedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "error_msg")
    private String errorMsg;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getModelId() { return modelId; }
    public void setModelId(UUID modelId) { this.modelId = modelId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer[] getInputShape() { return inputShape; }
    public void setInputShape(Integer[] inputShape) { this.inputShape = inputShape; }
    public byte[] getInputData() { return inputData; }
    public void setInputData(byte[] inputData) { this.inputData = inputData; }
    public byte[] getResultData() { return resultData; }
    public void setResultData(byte[] resultData) { this.resultData = resultData; }
    public OffsetDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(OffsetDateTime submittedAt) { this.submittedAt = submittedAt; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }
    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
}
