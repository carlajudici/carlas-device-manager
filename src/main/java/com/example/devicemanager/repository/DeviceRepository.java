package com.example.devicemanager.repository;

import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * Repository for managing Device persistence.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Page<Device> findByState(DeviceState state, Pageable pageable);

    Page<Device> findByBrand(String brand, Pageable pageable);

    Page<Device> findByStateAndBrand(DeviceState state, String brand, Pageable pageable);
}
