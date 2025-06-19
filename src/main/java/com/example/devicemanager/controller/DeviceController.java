package com.example.devicemanager.controller;

import com.example.devicemanager.dto.DeviceRequestDTO;
import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;
import com.example.devicemanager.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Device API.
 */
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService service;

    @Operation(summary = "Create a new device")
    @PostMapping
    public ResponseEntity<Device> create(@RequestBody DeviceRequestDTO request) {
        Device device = new Device();
        device.setName(request.getName());
        device.setBrand(request.getBrand());
        device.setState(request.getState());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(device));
    }

    @Operation(summary = "Update a device")
    @PatchMapping("/{id}")
    public ResponseEntity<Device> update(@PathVariable Long id, @RequestBody DeviceRequestDTO request) {
        Device device = new Device();
        device.setName(request.getName());
        device.setBrand(request.getBrand());
        device.setState(request.getState());
        return ResponseEntity.ok(service.update(id, device));
    }

    @Operation(summary = "Get all devices")
    @GetMapping("/all")
    public ResponseEntity<List<Device>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get devices filtered by state and brand with pagination")
    @GetMapping
    public Page<Device> getDevices(
            @RequestParam(required = false) DeviceState state,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return service.getDevices(state, brand, pageable);
    }

    @Operation(summary = "Get device by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Device> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    @Operation(summary = "Delete a device by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
