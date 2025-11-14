package com.bibavix.service.impl;

import com.bibavix.dto.PriorityDTO;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.Priority;
import com.bibavix.repository.PriorityRepository;
import com.bibavix.util.mapper.PriorityMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriorityServiceImplTest {

    @InjectMocks
    PriorityServiceImpl priorityService;

    @Mock
    PriorityRepository priorityRepository;

    @Mock
    PriorityMapper priorityMapper;

    private Priority priority;
    private PriorityDTO priorityDTO;

    @BeforeEach
    void setUp() {
        priority = new Priority();
        priority.setPriorityId(1);
        priority.setPriorityName("High");

        priorityDTO = new PriorityDTO();
        priorityDTO.setPriorityId(1);
        priorityDTO.setPriorityName("High");
    }

    @Test
    void shouldReturnPriorityDTOWhenCreatePriority() {
        when(priorityMapper.toEntity(priorityDTO)).thenReturn(priority);
        when(priorityRepository.save(priority)).thenReturn(priority);
        when(priorityMapper.toDTO(priority)).thenReturn(priorityDTO);

        PriorityDTO createdPriority = priorityService.createPriority(priorityDTO);

        Assertions.assertNotNull(createdPriority);
        Assertions.assertEquals("High", createdPriority.getPriorityName());
        verify(priorityMapper, times(1)).toEntity(any());
        verify(priorityRepository, times(1)).save(any());
        verify(priorityMapper, times(1)).toDTO(any());
    }

    @Test
    void shouldReturnPriorityListWhenGetAllPriorities() {
        Priority priority2 = new Priority();
        priority2.setPriorityId(2);
        priority2.setPriorityName("Low");

        PriorityDTO priorityDTO2 = new PriorityDTO();
        priorityDTO2.setPriorityId(2);
        priorityDTO2.setPriorityName("Low");

        List<Priority> priorityList = List.of(priority, priority2);
        when(priorityRepository.findAll()).thenReturn(priorityList);
        when(priorityMapper.toDTO(priority)).thenReturn(priorityDTO);
        when(priorityMapper.toDTO(priority2)).thenReturn(priorityDTO2);

        List<PriorityDTO> priorityDTOS = priorityService.getAllPriorities();

        Assertions.assertNotNull(priorityDTOS);
        Assertions.assertFalse(priorityDTOS.isEmpty());
        Assertions.assertEquals(2, priorityDTOS.size());
        verify(priorityRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnPriorityWhenGetPriorityById() {
        when(priorityRepository.findById(1)).thenReturn(Optional.of(priority));
        when(priorityMapper.toDTO(priority)).thenReturn(priorityDTO);

        PriorityDTO foundPriority = priorityService.getPriorityById(1);

        Assertions.assertNotNull(foundPriority);
        Assertions.assertEquals("High", foundPriority.getPriorityName());
        verify(priorityRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowPriorityNotFoundExceptionWhenGetPriorityById() {
        when(priorityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                priorityService.getPriorityById(1));
    }

    @Test
    void shouldUpdatePriorityWhenUpdatePriority() {
        PriorityDTO updatedDTO = new PriorityDTO();
        updatedDTO.setPriorityId(1);
        updatedDTO.setPriorityName("Medium");

        when(priorityRepository.findById(1)).thenReturn(Optional.of(priority));
        when(priorityRepository.save(priority)).thenReturn(priority);
        when(priorityMapper.toDTO(priority)).thenReturn(updatedDTO);

        PriorityDTO result = priorityService.updatePriority(updatedDTO);

        Assertions.assertNotNull(result);
        verify(priorityMapper).updatePriorityFromDTO(updatedDTO, priority);
        verify(priorityRepository).save(priority);
    }

    @Test
    void shouldThrowPriorityNotFoundExceptionWhenUpdatePriorityNotFound() {
        when(priorityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                priorityService.updatePriority(priorityDTO));
    }

    @Test
    void verifyDeletePriority() {
        when(priorityRepository.findById(1)).thenReturn(Optional.of(priority));
        when(priorityMapper.toDTO(priority)).thenReturn(priorityDTO);

        priorityService.deletePriority(1);

        verify(priorityRepository, times(1)).deleteById(1);
        verify(priorityRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowPriorityNotFoundExceptionWhenDeletePriorityNotFound() {
        when(priorityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                priorityService.deletePriority(1));
    }
}

