package com.bibavix.service.impl;

import com.bibavix.dto.PriorityDTO;
import java.util.UUID;
import com.bibavix.exception.ResourceNotFoundException;
import com.bibavix.model.Priority;
import com.bibavix.repository.PriorityRepository;
import com.bibavix.service.PriorityService;
import com.bibavix.util.mapper.PriorityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;
    private final PriorityMapper priorityMapper;

    @Override
    public PriorityDTO createPriority(PriorityDTO priorityDTO) {
        Priority priorityToSave = priorityMapper.toEntity(priorityDTO);
        Priority priority = priorityRepository.save(priorityToSave);
        return priorityMapper.toDTO(priority);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PriorityDTO> getAllPriorities() {
        List<Priority> priorityList = priorityRepository.findAll();
        return priorityList.stream()
                .map(priorityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PriorityDTO getPriorityById(UUID priorityId) {
        Priority priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new ResourceNotFoundException("Priority with id " + priorityId + " not found."));
        return priorityMapper.toDTO(priority);
    }

    @Override
    public PriorityDTO updatePriority(PriorityDTO priorityDTO) {
        UUID priorityId = priorityDTO.getPriorityId();
        Priority priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new ResourceNotFoundException("Priority with id " + priorityId + " not found."));
        priorityMapper.updatePriorityFromDTO(priorityDTO, priority);
        Priority updatedPriority = priorityRepository.save(priority);
        return priorityMapper.toDTO(updatedPriority);
    }

    @Override
    public void deletePriority(UUID priorityId) {
        getPriorityById(priorityId);
        priorityRepository.deleteById(priorityId);
    }
}
