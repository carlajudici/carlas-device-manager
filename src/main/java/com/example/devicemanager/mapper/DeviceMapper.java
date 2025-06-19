package com.example.devicemanager.mapper;

import com.example.devicemanager.dto.DeviceRequestDTO;
import com.example.devicemanager.dto.DeviceResponseDTO;
import com.example.devicemanager.model.Device;

public class DeviceMapper {

    public static Device toEntity(DeviceRequestDTO dto) {
        Device device = new Device();
        device.setName(dto.getName());
        device.setBrand(dto.getBrand());
        device.setState(dto.getState());
        return device;
    }

    public static DeviceResponseDTO toResponseDTO(Device device) {
        DeviceResponseDTO dto = new DeviceResponseDTO();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setBrand(device.getBrand());
        dto.setState(device.getState().name());
        dto.setCreationTime(device.getCreationTime());
        return dto;
    }
}
