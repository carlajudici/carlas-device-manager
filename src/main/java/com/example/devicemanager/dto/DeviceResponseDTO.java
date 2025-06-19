package com.example.devicemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Response DTO for device data")
@Data
public class DeviceResponseDTO {
    private Long id;
    private String name;
    private String brand;
    private String state;
    private LocalDateTime creationTime;
}
