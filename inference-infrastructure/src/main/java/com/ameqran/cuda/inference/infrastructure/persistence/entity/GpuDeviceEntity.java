package com.ameqran.cuda.inference.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "gpu_devices")
public class GpuDeviceEntity {

    @Id
    private UUID id;

    @Column(name = "cuda_index", nullable = false)
    private int cudaIndex;

    @Column(name = "total_memory", nullable = false)
    private long totalMemoryBytes;

    @Column(name = "free_memory", nullable = false)
    private long freeMemoryBytes;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public int getCudaIndex() { return cudaIndex; }
    public void setCudaIndex(int cudaIndex) { this.cudaIndex = cudaIndex; }
    public long getTotalMemoryBytes() { return totalMemoryBytes; }
    public void setTotalMemoryBytes(long totalMemoryBytes) { this.totalMemoryBytes = totalMemoryBytes; }
    public long getFreeMemoryBytes() { return freeMemoryBytes; }
    public void setFreeMemoryBytes(long freeMemoryBytes) { this.freeMemoryBytes = freeMemoryBytes; }
}
