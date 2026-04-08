package com.ameqran.cuda.inference.infrastructure.persistence.adapter;

import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.repository.GpuDeviceRepository;
import com.ameqran.cuda.inference.infrastructure.persistence.mapper.GpuDeviceMapper;
import com.ameqran.cuda.inference.infrastructure.persistence.repository.SpringDataGpuDeviceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaGpuDeviceRepository implements GpuDeviceRepository {

    private final SpringDataGpuDeviceRepository springRepository;
    private final GpuDeviceMapper mapper;

    public JpaGpuDeviceRepository(SpringDataGpuDeviceRepository springRepository) {
        this.springRepository = springRepository;
        this.mapper = new GpuDeviceMapper();
    }

    @Override
    public Optional<GpuDevice> findById(UUID deviceId) {
        return springRepository.findById(deviceId).map(mapper::toDomain);
    }

    @Override
    public Optional<GpuDevice> findBestAvailable() {
        return springRepository.findFirstByOrderByFreeMemoryBytesDesc().map(mapper::toDomain);
    }

    @Override
    public List<GpuDevice> findAll() {
        return springRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void save(GpuDevice gpuDevice) {
        springRepository.save(mapper.toEntity(gpuDevice));
    }
}
