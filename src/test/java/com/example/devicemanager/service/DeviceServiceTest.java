package com.example.devicemanager.service;

import com.example.devicemanager.exception.BusinessException;
import com.example.devicemanager.exception.NotFoundException;
import com.example.devicemanager.model.Device;
import com.example.devicemanager.model.DeviceState;
import com.example.devicemanager.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        device = new Device();
        device.setId(1L);
        device.setName("Device A");
        device.setBrand("Brand X");
        device.setState(DeviceState.AVAILABLE);
    }

    @Test
    void testCreate() {
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Device created = service.create(device);
        assertNotNull(created.getCreationTime());
        verify(repository, times(1)).save(device);
    }

    @Test
    void testUpdate_Success() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Device A");
        existing.setBrand("Brand X");
        existing.setState(DeviceState.IN_USE);
        existing.setCreationTime(LocalDateTime.now().minusDays(1));

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Mantendo o mesmo nome e marca para dispositivo em uso
        device.setName("Device A");
        device.setBrand("Brand X");
        device.setState(DeviceState.IN_USE);

        Device updated = service.update(1L, device);
        assertEquals(existing.getCreationTime(), updated.getCreationTime());

        ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);
        verify(repository).save(captor.capture());
        Device saved = captor.getValue();
        assertEquals("Device A", saved.getName());
        assertEquals("Brand X", saved.getBrand());
    }

    @Test
    void testUpdate_BusinessException() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Device A");
        existing.setBrand("Brand X");
        existing.setState(DeviceState.IN_USE);
        existing.setCreationTime(LocalDateTime.now().minusDays(1));

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        // Tentativa de alteração do nome para dispositivo em uso
        device.setName("Novo Nome");
        device.setBrand("Brand X");

        assertThrows(BusinessException.class, () -> service.update(1L, device));
        verify(repository, never()).save(any());
    }

    @Test
    void testGetAll() {
        Device device2 = new Device();
        device2.setId(2L);
        device2.setName("Device B");
        device2.setBrand("Brand Y");
        device2.setState(DeviceState.AVAILABLE);

        List<Device> devices = Arrays.asList(device, device2);
        when(repository.findAll()).thenReturn(devices);

        List<Device> result = service.getAll();
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        Device result = service.getById(1L);
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetByBrand() {
        when(repository.findByBrand("Brand X")).thenReturn(Arrays.asList(device));
        List<Device> result = service.getByBrand("Brand X");
        assertEquals(1, result.size());
        verify(repository, times(1)).findByBrand("Brand X");
    }

    @Test
    void testGetByState() {
        when(repository.findByState(DeviceState.AVAILABLE)).thenReturn(Arrays.asList(device));
        List<Device> result = service.getByState(DeviceState.AVAILABLE);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByState(DeviceState.AVAILABLE);
    }

    @Test
    void testDelete_Success() {
        device.setState(DeviceState.AVAILABLE);
        when(repository.findById(1L)).thenReturn(Optional.of(device));

        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_BusinessException() {
        device.setState(DeviceState.IN_USE);
        when(repository.findById(1L)).thenReturn(Optional.of(device));
        assertThrows(BusinessException.class, () -> service.delete(1L));
        verify(repository, never()).deleteById(anyLong());
    }
}