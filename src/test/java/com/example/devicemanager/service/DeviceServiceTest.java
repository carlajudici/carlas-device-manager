package com.example.devicemanager.service;

import com.example.devicemanager.exception.BusinessException;
import com.example.devicemanager.exception.NotFoundException;
import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;
import com.example.devicemanager.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository repository;

    @InjectMocks
    private DeviceService service;

    private Device device;

    @BeforeEach
    void setUp() {
        device = Device.builder()
                .id(1L)
                .name("Device A")
                .brand("Brand X")
                .state(DeviceState.AVAILABLE)
                .build();
    }

    @Test
    void shouldCreateDevice() {
        when(repository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Device created = service.create(device);
        assertNotNull(created);
        verify(repository).save(device);
    }

    @Test
    void shouldReturnAllDevices() {
        Device device2 = Device.builder()
                .id(2L)
                .name("Device B")
                .brand("Brand Y")
                .state(DeviceState.AVAILABLE)
                .build();
        when(repository.findAll()).thenReturn(List.of(device, device2));
        List<Device> result = service.getAll();
        assertEquals(2, result.size());
        verify(repository).findAll();
    }



    @Test
    void shouldReturnDeviceById() {
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        Device result = service.getById(1L);
        assertEquals(device, result);
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeviceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(1L));
        verify(repository).findById(1L);
    }


    @Test
    void shouldDeleteDevice() {
        device.setState(DeviceState.AVAILABLE);
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        service.delete(1L);
        verify(repository).delete(device);
    }

    @Test
    void shouldThrowBusinessExceptionWhenDeleteInUseDevice() {
        device.setState(DeviceState.IN_USE);
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        assertThrows(BusinessException.class, () -> service.delete(1L));
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenUpdateInUseDeviceWithDifferentNameOrBrand() {
        Device existing = Device.builder()
                .id(1L)
                .name("Device A")
                .brand("Brand X")
                .state(DeviceState.IN_USE)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        Device update = Device.builder()
                .id(1L)
                .name("Outro Nome")
                .brand("Brand X")
                .state(DeviceState.IN_USE)
                .build();

        assertThrows(BusinessException.class, () -> service.update(1L, update));
        verify(repository, never()).save(any());
    }
}