package com.bibavix.util.mapper;

import com.bibavix.dto.PriorityDTO;
import com.bibavix.model.Priority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PriorityMapper {

    @Mapping(target = "priorityId", ignore = true)
    @Mapping(target = "priorityName", source = "priorityName")
    Priority toEntity(PriorityDTO priorityDTO);

    PriorityDTO toDTO(Priority priority);

    @Mapping(target = "priorityId", ignore = true)
    @Mapping(target = "priorityName", source = "priorityName")
    void updatePriorityFromDTO(PriorityDTO priorityDTO, @MappingTarget Priority priority);
}

