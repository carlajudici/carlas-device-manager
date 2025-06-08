package com.example.devicemanager.repository;

import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for managing Device persistence.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByBrand(String brand);
    List<Device> findByState(DeviceState state);
}
