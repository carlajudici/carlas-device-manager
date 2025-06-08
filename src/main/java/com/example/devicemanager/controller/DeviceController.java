package com.example.devicemanager.controller;

import com.example.devicemanager.dto.DeviceRequest;
import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;
import com.example.devicemanager.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Device> create(@RequestBody DeviceRequest request) {
        Device device = new Device();
        device.setName(request.getName());
        device.setBrand(request.getBrand());
        device.setState(request.getState());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(device));
    }

    @Operation(summary = "Update a device")
    @PatchMapping("/{id}")
    public ResponseEntity<Device> update(@PathVariable Long id, @RequestBody DeviceRequest request) {
        Device device = new Device();
        device.setName(request.getName());
        device.setBrand(request.getBrand());
        device.setState(request.getState());
        return ResponseEntity.ok(service.update(id, device));
    }

    @Operation(summary = "Get all devices")
    @GetMapping
    public ResponseEntity<List<Device>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get device by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Device> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get devices by brand")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Device>> getByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(service.getByBrand(brand));
    }

    @Operation(summary = "Get devices by state")
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Device>> getByState(@PathVariable DeviceState state) {
        return ResponseEntity.ok(service.getByState(state));
    }

    @Operation(summary = "Delete a device by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
