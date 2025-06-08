package com.example.devicemanager.service;

import com.example.devicemanager.exception.BusinessException;
import com.example.devicemanager.exception.NotFoundException;
import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;
import com.example.devicemanager.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for business logic related to Devices.
 */
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;

    public Device create(Device device) {
        device.setCreationTime(LocalDateTime.now());
        return repository.save(device);
    }

    public Device update(Long id, Device input) {
        Device existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        // Enforce business rules
        if (existing.getState() == DeviceState.IN_USE) {
            if (!existing.getName().equals(input.getName()) ||
                !existing.getBrand().equals(input.getBrand())) {
                throw new BusinessException("Cannot update name or brand when device is in use");
            }
        }

        // Prevent updating creationTime
        input.setId(id);
        input.setCreationTime(existing.getCreationTime());
        return repository.save(input);
    }

    public List<Device> getAll() {
        return repository.findAll();
    }

    public Device getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
    }

    public List<Device> getByBrand(String brand) {
        return repository.findByBrand(brand);
    }

    public List<Device> getByState(DeviceState state) {
        return repository.findByState(state);
    }

    public void delete(Long id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));
        if (device.getState() == DeviceState.IN_USE) {
            throw new BusinessException("Cannot delete a device that is in use");
        }
        repository.deleteById(id);
    }
}
