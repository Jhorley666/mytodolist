package com.bibavix.service;

import java.util.UUID;

import com.bibavix.dto.PriorityDTO;

import java.util.List;

public interface PriorityService {
    PriorityDTO createPriority(PriorityDTO priorityDTO);

    List<PriorityDTO> getAllPriorities();

    PriorityDTO getPriorityById(UUID priorityId);

    PriorityDTO updatePriority(PriorityDTO priorityDTO);

    void deletePriority(UUID priorityId);
}
