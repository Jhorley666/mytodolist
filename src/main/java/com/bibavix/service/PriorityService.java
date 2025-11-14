package com.bibavix.service;

import com.bibavix.dto.PriorityDTO;

import java.util.List;

public interface PriorityService {
    PriorityDTO createPriority(PriorityDTO priorityDTO);
    List<PriorityDTO> getAllPriorities();
    PriorityDTO getPriorityById(Integer priorityId);
    PriorityDTO updatePriority(PriorityDTO priorityDTO);
    void deletePriority(Integer priorityId);
}

