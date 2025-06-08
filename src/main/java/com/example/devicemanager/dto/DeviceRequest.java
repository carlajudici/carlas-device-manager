package com.example.devicemanager.dto;

import com.example.devicemanager.model.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    private DeviceState state;
}
