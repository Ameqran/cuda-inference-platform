package com.ameqran.cuda.inference.interfaces.api;

import com.ameqran.cuda.inference.domain.repository.GpuDeviceRepository;
import com.ameqran.cuda.inference.interfaces.dto.GpuDeviceDto;
import com.ameqran.cuda.inference.interfaces.mapper.ApiMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final GpuDeviceRepository gpuDeviceRepository;
    private final ApiMapper mapper = new ApiMapper();

    public DeviceController(GpuDeviceRepository gpuDeviceRepository) {
        this.gpuDeviceRepository = gpuDeviceRepository;
    }

    @GetMapping
    public List<GpuDeviceDto> list() {
        return gpuDeviceRepository.findAll().stream().map(mapper::toGpuDeviceDto).toList();
    }
}
