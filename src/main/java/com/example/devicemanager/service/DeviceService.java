package com.example.devicemanager.service;

import com.example.devicemanager.exception.BusinessException;
import com.example.devicemanager.exception.NotFoundException;
import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;
import com.example.devicemanager.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Device operations.
 * Handles business logic and transaction management.
 */
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;

    /**
     * Creates a new Device.
     */
    @Transactional
    public Device create(Device device) {
        device.setCreationTime(LocalDateTime.now());
        device.setState(DeviceState.AVAILABLE);
        return repository.save(device);
    }

    /**
     * Updates an existing Device by ID.
     */
    @Transactional
    public Device update(Long id, Device device) {
        Device existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));
                
        if (DeviceState.IN_USE.equals(device.getState())) {
            throw new BusinessException("Cannot delete a device that is in use");
        }

        existing.setName(device.getName());
        existing.setBrand(device.getBrand());
        existing.setState(device.getState());

        return repository.save(existing);
    }

    /**
     * Retrieves all devices.
     */
    @Transactional(readOnly = true)
    public List<Device> getAll() {
        return repository.findAll();
    }

    public Page<Device> getDevices(DeviceState state, String brand, Pageable pageable) {
        if (state != null && brand != null) {
            return repository.findByStateAndBrand(state, brand, pageable);
        } else if (state != null) {
            return repository.findByState(state, pageable);
        } else if (brand != null) {
            return repository.findByBrand(brand, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }
    /**
     * Retrieves a device by its ID.
     */
    @Transactional(readOnly = true)
    public Device getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));
    }

    /**
     * Deletes a device if it's not IN_USE.
     */
    @Transactional
    public void delete(Long id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        if (DeviceState.IN_USE.equals(device.getState())) {
            throw new BusinessException("Cannot delete a device that is in use");
        }

        repository.delete(device);
    }

    /**
     * Deactivates a device by setting its state to INACTIVE.
     */
    @Transactional
    public Device deactivate(Long id) {
        Device device = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        if (DeviceState.INACTIVE.equals(device.getState())) {
            throw new BusinessException("Device is already inactive");
        }

        device.setState(DeviceState.INACTIVE);
        return repository.save(device);
    }
}